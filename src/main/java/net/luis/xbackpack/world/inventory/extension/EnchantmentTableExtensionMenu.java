package net.luis.xbackpack.world.inventory.extension;

import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.network.packet.extension.UpdateEnchantmentTablePacket;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.AbstractExtensionContainerMenu;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionSlot;
import net.luis.xbackpack.world.inventory.handler.EnchantingHandler;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Luis-st
 *
 */

public class EnchantmentTableExtensionMenu extends AbstractExtensionMenu {
	
	public static final ResourceLocation EMPTY_ENCHANTMENT = new ResourceLocation("enchantment_empty");
	
	private final EnchantingHandler handler;
	private final RandomSource rng = RandomSource.create();
	private final ResourceLocation[] enchantments = new ResourceLocation[] {
		EMPTY_ENCHANTMENT, EMPTY_ENCHANTMENT, EMPTY_ENCHANTMENT
	};
	private final int[] enchantmentLevels = new int[] {
		-1, -1, -1
	};
	private final int[] enchantingCosts = new int[] {
		0, 0, 0
	};
	private int enchantmentSeed;
	
	public EnchantmentTableExtensionMenu(AbstractExtensionContainerMenu menu, Player player) {
		super(menu, player, BackpackExtensions.ENCHANTMENT_TABLE.get());
		this.handler = BackpackProvider.get(this.player).getEnchantingHandler();
		this.enchantmentSeed = player.getEnchantmentSeed();
	}
	
	@Override
	public void open() {
		this.slotsChanged();
	}
	
	@Override
	public void addSlots(Consumer<Slot> consumer) {
		consumer.accept(new ExtensionSlot(this, this.handler.getPowerHandler(), 0, 235, 108) {
			@Override
			public boolean mayPlace(@NotNull ItemStack stack) {
				return stack.is(Tags.Items.BOOKSHELVES) || stack.getItem() instanceof BookItem;
			}
		});
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 0, 225, 130) {
			@Override
			public boolean mayPlace(@NotNull ItemStack stack) {
				return stack.isEnchantable() || stack.getItem() instanceof BookItem;
			}
			
			@Override
			public int getMaxStackSize() {
				return 1;
			}
		});
		consumer.accept(new ExtensionSlot(this, this.handler.getFuelHandler(), 0, 245, 130) {
			@Override
			public boolean mayPlace(@NotNull ItemStack stack) {
				return stack.is(Tags.Items.ENCHANTING_FUELS);
			}
		});
	}
	
	@Override
	public void slotsChanged() {
		ItemStack inputStack = this.handler.getInputHandler().getStackInSlot(0);
		if (!inputStack.isEmpty() && inputStack.isEnchantable()) {
			int power = this.calculatePower();
			this.rng.setSeed(this.enchantmentSeed);
			for (int row = 0; row < 3; ++row) {
				this.enchantingCosts[row] = EnchantmentHelper.getEnchantmentCost(this.rng, row, power, inputStack);
				this.enchantments[row] = EMPTY_ENCHANTMENT;
				this.enchantmentLevels[row] = -1;
				if (this.enchantingCosts[row] < row + 1) {
					this.enchantingCosts[row] = 0;
				}
				this.enchantingCosts[row] = ForgeEventFactory.onEnchantmentLevelSet(this.player.level(), this.player.blockPosition(), row, power, inputStack, this.enchantingCosts[row]);
			}
			for (int row = 0; row < 3; ++row) {
				if (this.enchantingCosts[row] > 0) {
					List<EnchantmentInstance> enchantments = this.getEnchantmentList(inputStack, row, this.enchantingCosts[row]);
					if (!enchantments.isEmpty()) {
						EnchantmentInstance instance = Util.getRandom(enchantments, this.rng);
						this.enchantments[row] = ForgeRegistries.ENCHANTMENTS.getKey(instance.enchantment);
						this.enchantmentLevels[row] = instance.level;
					}
				}
			}
		} else {
			for (int row = 0; row < 3; ++row) {
				this.enchantingCosts[row] = 0;
				this.enchantments[row] = EMPTY_ENCHANTMENT;
				this.enchantmentLevels[row] = -1;
			}
		}
		XBNetworkHandler.INSTANCE.sendToPlayer(this.player, new UpdateEnchantmentTablePacket(this.enchantments, this.enchantmentLevels, this.enchantingCosts, this.enchantmentSeed));
	}
	
	private int calculatePower() {
		ItemStack stack = this.handler.getPowerHandler().getStackInSlot(0);
		if (stack.isEmpty()) {
			return 0;
		} else if (stack.is(Items.BOOKSHELF)) {
			return Mth.clamp(stack.getCount(), 0, 15);
		} else if (stack.is(Items.BOOK)) {
			return Mth.clamp(stack.getCount() / 3, 0, 15);
		}
		return 0;
	}
	
	@Override
	public boolean clickMenuButton(Player player, int button) {
		if (2 >= button && button >= 0) {
			ItemStack inputStack = this.handler.getInputHandler().getStackInSlot(0);
			ItemStack fuelStack = this.handler.getFuelHandler().getStackInSlot(0);
			int requiredFuel = button + 1;
			if ((fuelStack.isEmpty() || fuelStack.getCount() < requiredFuel) && !player.getAbilities().instabuild) {
				return false;
			} else if (this.enchantingCosts[button] <= 0 || inputStack.isEmpty() || (player.experienceLevel < requiredFuel || player.experienceLevel < this.enchantingCosts[button]) && !player.getAbilities().instabuild) {
				return false;
			} else {
				ItemStack resultStack = inputStack;
				List<EnchantmentInstance> enchantments = this.getEnchantmentList(inputStack, button, this.enchantingCosts[button]);
				if (!enchantments.isEmpty()) {
					player.onEnchantmentPerformed(inputStack, requiredFuel);
					boolean isBook = inputStack.is(Items.BOOK);
					if (isBook) {
						resultStack = new ItemStack(Items.ENCHANTED_BOOK);
						CompoundTag tag = inputStack.getTag();
						if (tag != null) {
							resultStack.setTag(tag.copy());
						}
						this.handler.getInputHandler().setStackInSlot(0, resultStack);
					}
					for (EnchantmentInstance enchantment : enchantments) {
						if (isBook) {
							EnchantedBookItem.addEnchantment(resultStack, enchantment);
						} else {
							resultStack.enchant(enchantment.enchantment, enchantment.level);
						}
					}
					if (!player.getAbilities().instabuild) {
						fuelStack.shrink(requiredFuel);
						if (fuelStack.isEmpty()) {
							this.handler.getFuelHandler().setStackInSlot(0, ItemStack.EMPTY);
						}
					}
					player.awardStat(Stats.ENCHANT_ITEM);
					if (player instanceof ServerPlayer serverPlayer) {
						CriteriaTriggers.ENCHANTED_ITEM.trigger(serverPlayer, resultStack, requiredFuel);
						this.playSound(serverPlayer, serverPlayer.serverLevel());
					}
					this.enchantmentSeed = player.getEnchantmentSeed();
					this.slotsChanged();
				}
				return true;
			}
		} else {
			Util.logAndPauseIfInIde(player.getName() + " pressed invalid button id: " + button);
			return false;
		}
	}
	
	private @NotNull List<EnchantmentInstance> getEnchantmentList(ItemStack inputStack, int row, int enchantingCost) {
		this.rng.setSeed(this.enchantmentSeed + row);
		List<EnchantmentInstance> enchantments = EnchantmentHelper.selectEnchantment(this.rng, inputStack, enchantingCost, false);
		if (inputStack.is(Items.BOOK) && enchantments.size() > 1) {
			enchantments.remove(this.rng.nextInt(enchantments.size()));
		}
		return enchantments;
	}
	
	private void playSound(@NotNull ServerPlayer player, @NotNull ServerLevel level) {
		player.connection.send(new ClientboundSoundPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ENCHANTMENT_TABLE_USE), SoundSource.BLOCKS, player.getX(), player.getY(), player.getZ(), 1.0F, level.random.nextFloat() * 0.1F + 0.9F, level.random.nextLong()));
	}
	
	@Override
	public boolean quickMoveStack(ItemStack slotStack, int index) {
		if (908 >= index && index >= 0) { // from container
			if (slotStack.is(Tags.Items.BOOKSHELVES) || this.canQuickMoveBook()) {
				return this.menu.moveItemStackTo(slotStack, 941, 942); // into power
			} else if (slotStack.isEnchantable() || slotStack.getItem() instanceof BookItem) {
				return this.menu.moveItemStackTo(slotStack, 942, 943); // into input
			} else if (slotStack.is(Tags.Items.ENCHANTING_FUELS)) {
				return this.menu.moveItemStackTo(slotStack, 943, 944); // into fuel
			}
		} else if (index >= 943) { // from extension
			return this.movePreferredMenu(slotStack); // into container
		}
		return false;
	}
	
	private boolean canQuickMoveBook() {
		if (!this.handler.getInputHandler().getStackInSlot(0).isEmpty()) {
			ItemStack stack = this.handler.getPowerHandler().getStackInSlot(0);
			return stack.isEmpty() || (stack.is(Items.BOOK) && stack.getMaxStackSize() > stack.getCount());
		}
		return false;
	}
}
