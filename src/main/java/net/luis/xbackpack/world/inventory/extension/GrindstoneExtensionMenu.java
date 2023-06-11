package net.luis.xbackpack.world.inventory.extension;

import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.AbstractExtensionContainerMenu;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionSlot;
import net.luis.xbackpack.world.inventory.handler.CraftingHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.GrindstoneEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @author Luis-st
 *
 */

public class GrindstoneExtensionMenu extends AbstractExtensionMenu {
	
	private final CraftingHandler handler;
	private int xp = -1;
	
	public GrindstoneExtensionMenu(AbstractExtensionContainerMenu menu, Player player) {
		super(menu, player, BackpackExtensions.GRINDSTONE.get());
		this.handler = BackpackProvider.get(this.player).getGrindstoneHandler();
	}
	
	@Override
	public void open() {
		this.createResult();
	}
	
	@Override
	public void addSlots(@NotNull Consumer<Slot> consumer) {
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 0, 243, 172) {
			@Override
			public boolean mayPlace(@NotNull ItemStack stack) {
				return true;
			}
		});
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 1, 243, 193) {
			@Override
			public boolean mayPlace(@NotNull ItemStack stack) {
				return true;
			}
		});
		consumer.accept(new ExtensionSlot(this, this.handler.getResultHandler(), 0, 305, 187, false) {
			@Override
			public boolean mayPlace(@NotNull ItemStack stack) {
				return false;
			}
			
			@Override
			public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
				GrindstoneExtensionMenu.this.onTake(player, stack);
			}
		});
	}
	
	private void onTake(Player player, ItemStack stack) {
		if (player instanceof ServerPlayer serverPlayer) {
			GrindstoneEvent.OnTakeItem event = new GrindstoneEvent.OnTakeItem(this.handler.getInputHandler().getStackInSlot(0), this.handler.getInputHandler().getStackInSlot(1), this.getExperienceAmount(player.level()));
			if (MinecraftForge.EVENT_BUS.post(event)) {
				return;
			}
			player.giveExperiencePoints(event.getXp());
			this.playSound(serverPlayer, serverPlayer.serverLevel());
			this.handler.getInputHandler().setStackInSlot(0, event.getNewTopItem());
			this.handler.getInputHandler().setStackInSlot(1, event.getNewBottomItem());
		}
		this.menu.broadcastChanges();
	}
	
	private int getExperienceAmount(Level level) {
		if (this.xp > -1) {
			return this.xp;
		}
		int amount = 0;
		amount += this.getExperienceFromItem(this.handler.getInputHandler().getStackInSlot(0));
		amount += this.getExperienceFromItem(this.handler.getInputHandler().getStackInSlot(1));
		if (amount > 0) {
			int experience = (int) Math.ceil((double) amount / 2.0D);
			return experience + level.random.nextInt(experience);
		} else {
			return 0;
		}
	}
	
	private int getExperienceFromItem(ItemStack stack) {
		int experience = 0;
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
		for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			Enchantment enchantment = entry.getKey();
			if (!enchantment.isCurse()) {
				experience += enchantment.getMinCost(entry.getValue());
			}
		}
		return experience;
	}
	
	private void playSound(@NotNull ServerPlayer player, @NotNull ServerLevel level) {
		player.connection.send(new ClientboundSoundPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.GRINDSTONE_USE), SoundSource.BLOCKS, player.getX(), player.getY(), player.getZ(), 1.0F, level.random.nextFloat() * 0.1F + 0.9F, level.random.nextLong()));
	}
	
	@Override
	public void slotsChanged() {
		this.createResult();
	}
	
	private void createResult() {
		ItemStack topStack = this.handler.getInputHandler().getStackInSlot(0);
		ItemStack bottomStack = this.handler.getInputHandler().getStackInSlot(1);
		boolean hasInput = !topStack.isEmpty() || !bottomStack.isEmpty();
		boolean hasInputs = !topStack.isEmpty() && !bottomStack.isEmpty();
		this.xp = this.onGrindstoneChange(topStack, bottomStack, this.xp);
		if (this.xp != Integer.MIN_VALUE) {
			return;
		}
		if (!hasInput) {
			this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
		} else {
			boolean hasEnchantedBook = !topStack.isEmpty() && !topStack.is(Items.ENCHANTED_BOOK) && !topStack.isEnchanted() || !bottomStack.isEmpty() && !bottomStack.is(Items.ENCHANTED_BOOK) && !bottomStack.isEnchanted();
			if (topStack.getCount() > 1 || bottomStack.getCount() > 1) {
				this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
				this.menu.broadcastChanges();
				return;
			} else if (!hasInputs && hasEnchantedBook) {
				this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
				this.menu.broadcastChanges();
				return;
			}
			int count = 1;
			int damageValue;
			ItemStack resultStack;
			if (hasInputs) {
				if (!topStack.is(bottomStack.getItem())) {
					this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
					this.menu.broadcastChanges();
					return;
				}
				int k = topStack.getMaxDamage() - topStack.getDamageValue();
				int l = topStack.getMaxDamage() - bottomStack.getDamageValue();
				int i1 = k + l + topStack.getMaxDamage() * 5 / 100;
				damageValue = Math.max(topStack.getMaxDamage() - i1, 0);
				resultStack = this.mergeEnchants(topStack, bottomStack);
				if (!resultStack.isRepairable()) {
					damageValue = topStack.getDamageValue();
				}
				if (!resultStack.isDamageableItem() || !resultStack.isRepairable()) {
					if (!ItemStack.matches(topStack, bottomStack)) {
						this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
						this.menu.broadcastChanges();
						return;
					}
					count = 2;
				}
			} else {
				boolean topEmpty = !topStack.isEmpty();
				damageValue = topEmpty ? topStack.getDamageValue() : bottomStack.getDamageValue();
				resultStack = topEmpty ? topStack : bottomStack;
			}
			this.handler.getResultHandler().setStackInSlot(0, this.removeNonCurses(resultStack, damageValue, count));
		}
		this.menu.broadcastChanges();
	}
	
	private @NotNull ItemStack mergeEnchants(@NotNull ItemStack firstStack, ItemStack secondStack) {
		ItemStack resultStack = firstStack.copy();
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(secondStack);
		for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			Enchantment enchantment = entry.getKey();
			if (!enchantment.isCurse() || EnchantmentHelper.getTagEnchantmentLevel(enchantment, resultStack) == 0) {
				resultStack.enchant(enchantment, entry.getValue());
			}
		}
		return resultStack;
	}
	
	private @NotNull ItemStack removeNonCurses(@NotNull ItemStack inputStack, int damageValue, int count) {
		ItemStack resultStack = inputStack.copy();
		resultStack.removeTagKey("Enchantments");
		resultStack.removeTagKey("StoredEnchantments");
		if (damageValue > 0) {
			resultStack.setDamageValue(damageValue);
		} else {
			resultStack.removeTagKey("Damage");
		}
		resultStack.setCount(count);
		
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(inputStack).entrySet().stream().filter((entry) -> {
			return entry.getKey().isCurse();
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		EnchantmentHelper.setEnchantments(enchantments, resultStack);
		resultStack.setRepairCost(0);
		if (resultStack.is(Items.ENCHANTED_BOOK) && enchantments.size() == 0) {
			resultStack = new ItemStack(Items.BOOK);
			if (inputStack.hasCustomHoverName()) {
				resultStack.setHoverName(inputStack.getHoverName());
			}
		}
		for (int i = 0; i < enchantments.size(); ++i) {
			resultStack.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(resultStack.getBaseRepairCost()));
		}
		return resultStack;
	}
	
	private int onGrindstoneChange(ItemStack topStack, ItemStack bottomStack, int xp) {
		GrindstoneEvent.OnPlaceItem event = new GrindstoneEvent.OnPlaceItem(topStack, bottomStack, xp);
		if (MinecraftForge.EVENT_BUS.post(event)) {
			return event.getXp();
		}
		if (event.getOutput().isEmpty()) {
			return Integer.MIN_VALUE;
		}
		this.handler.getResultHandler().setStackInSlot(0, event.getOutput());
		return event.getXp();
	}
	
	@Override
	public boolean quickMoveStack(ItemStack slotStack, int index) {
		if (908 >= index && index >= 0) { // from container
			if (slotStack.isDamageableItem() || slotStack.is(Items.ENCHANTED_BOOK) || slotStack.isEnchanted()) {
				return this.menu.moveItemStackTo(slotStack, 951, 953); // into input
			}
		} else if (index == 953) { // from result
			return this.movePreferredMenu(slotStack); // into container
		}
		return false;
	}
}
