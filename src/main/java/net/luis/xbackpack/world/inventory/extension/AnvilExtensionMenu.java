package net.luis.xbackpack.world.inventory.extension;

import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.network.packet.extension.UpdateAnvilPacket;
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
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author Luis-st
 *
 */

public class AnvilExtensionMenu extends AbstractExtensionMenu {
	
	private final CraftingHandler handler;
	private int repairItemCountCost;
	private int cost;
	
	public AnvilExtensionMenu(AbstractExtensionContainerMenu menu, Player player) {
		super(menu, player, BackpackExtensions.ANVIL.get());
		this.handler = BackpackProvider.get(this.player).getAnvilHandler();
	}
	
	private static int calculateIncreasedRepairCost(int cost) {
		return cost * 2 + 1;
	}
	
	@Override
	public void open() {
		if (!this.handler.getInputHandler().getStackInSlot(0).isEmpty() || !this.handler.getInputHandler().getStackInSlot(1).isEmpty()) {
			this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
			this.createResult();
		}
	}
	
	@Override
	public void addSlots(@NotNull Consumer<Slot> consumer) {
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 0, 225, 73));
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 1, 260, 73));
		consumer.accept(new ExtensionSlot(this, this.handler.getResultHandler(), 0, 304, 73) {
			@Override
			public boolean mayPlace(@NotNull ItemStack stack) {
				return false;
			}
			
			@Override
			public boolean mayPickup(Player player) {
				return AnvilExtensionMenu.this.mayPickup(player);
			}
			
			@Override
			public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
				AnvilExtensionMenu.this.onTake(player);
				super.onTake(player, stack);
			}
		});
	}
	
	public boolean mayPickup(@NotNull Player player) {
		return (player.getAbilities().instabuild || player.experienceLevel >= this.cost) && this.cost > 0;
	}
	
	private void onTake(Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			if (!serverPlayer.getAbilities().instabuild) {
				serverPlayer.giveExperienceLevels(-this.cost);
			}
			this.handler.getInputHandler().setStackInSlot(0, ItemStack.EMPTY);
			if (this.repairItemCountCost > 0) {
				ItemStack rightStack = this.handler.getInputHandler().getStackInSlot(1);
				if (!rightStack.isEmpty() && rightStack.getCount() > this.repairItemCountCost) {
					rightStack.shrink(this.repairItemCountCost);
					this.handler.getInputHandler().setStackInSlot(1, rightStack);
				} else {
					this.handler.getInputHandler().setStackInSlot(1, ItemStack.EMPTY);
				}
			} else {
				this.handler.getInputHandler().setStackInSlot(1, ItemStack.EMPTY);
			}
			this.cost = 0;
			this.playSound(serverPlayer, serverPlayer.serverLevel());
		}
		this.menu.broadcastChanges();
		this.broadcastChanges();
		this.createResult();
	}
	
	private void playSound(ServerPlayer player, ServerLevel level) {
		player.connection.send(new ClientboundSoundPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ANVIL_USE), SoundSource.BLOCKS, player.getX(), player.getY(), player.getZ(), 1.0F, level.random.nextFloat() * 0.1F + 0.9F, level.random.nextLong()));
	}
	
	@Override
	public void slotsChanged() {
		this.createResult();
	}
	
	private void createResult() {
		ItemStack leftStack = this.handler.getInputHandler().getStackInSlot(0);
		this.cost = 1;
		int enchantCost = 0;
		int repairCost = 0;
		if (leftStack.isEmpty()) {
			this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
			this.cost = 0;
			this.broadcastChanges();
		} else {
			ItemStack resultStack = leftStack.copy();
			ItemStack rightStack = this.handler.getInputHandler().getStackInSlot(1);
			Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(resultStack);
			repairCost += leftStack.getBaseRepairCost() + (rightStack.isEmpty() ? 0 : rightStack.getBaseRepairCost());
			this.repairItemCountCost = 0;
			boolean enchantedBook = false;
			if (!this.onAnvilUpdate(leftStack, rightStack, repairCost)) {
				return;
			}
			if (!rightStack.isEmpty()) {
				enchantedBook = rightStack.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(rightStack).isEmpty();
				if (resultStack.isDamageableItem() && resultStack.getItem().isValidRepairItem(leftStack, rightStack)) {
					int damage = Math.min(resultStack.getDamageValue(), resultStack.getMaxDamage() / 4);
					if (damage <= 0) {
						this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
						this.cost = 0;
						this.broadcastChanges();
						return;
					}
					int currentRepairCost;
					for (currentRepairCost = 0; damage > 0 && currentRepairCost < rightStack.getCount(); ++currentRepairCost) {
						int currentDamage = resultStack.getDamageValue() - damage;
						resultStack.setDamageValue(currentDamage);
						++enchantCost;
						damage = Math.min(resultStack.getDamageValue(), resultStack.getMaxDamage() / 4);
					}
					this.repairItemCountCost = currentRepairCost;
				} else {
					if (!enchantedBook && (!resultStack.is(rightStack.getItem()) || !resultStack.isDamageableItem())) {
						this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
						this.cost = 0;
						this.broadcastChanges();
						return;
					}
					if (resultStack.isDamageableItem() && !enchantedBook) {
						int leftDamage = leftStack.getMaxDamage() - leftStack.getDamageValue();
						int rightDamage = rightStack.getMaxDamage() - rightStack.getDamageValue();
						int resultDamage = rightDamage + resultStack.getMaxDamage() * 12 / 100;
						int combindDamage = leftDamage + resultDamage;
						int damage = resultStack.getMaxDamage() - combindDamage;
						if (damage < 0) {
							damage = 0;
						}
						if (damage < resultStack.getDamageValue()) {
							resultStack.setDamageValue(damage);
							enchantCost += 2;
						}
					}
					Map<Enchantment, Integer> rightEnchantments = EnchantmentHelper.getEnchantments(rightStack);
					boolean canEnchant = false;
					boolean survival = false;
					for (Enchantment rightEnchantment : rightEnchantments.keySet()) {
						if (rightEnchantment != null) {
							int resultLevel = map.getOrDefault(rightEnchantment, 0);
							int rightLevel = rightEnchantments.get(rightEnchantment);
							rightLevel = resultLevel == rightLevel ? rightLevel + 1 : Math.max(rightLevel, resultLevel);
							boolean canEnchantOrCreative = rightEnchantment.canEnchant(leftStack);
							if (this.player.getAbilities().instabuild || leftStack.is(Items.ENCHANTED_BOOK)) {
								canEnchantOrCreative = true;
							}
							for (Enchantment enchantment : map.keySet()) {
								if (enchantment != rightEnchantment && !rightEnchantment.isCompatibleWith(enchantment)) {
									canEnchantOrCreative = false;
									++enchantCost;
								}
							}
							if (!canEnchantOrCreative) {
								survival = true;
							} else {
								canEnchant = true;
								if (rightLevel > rightEnchantment.getMaxLevel()) {
									rightLevel = rightEnchantment.getMaxLevel();
								}
								map.put(rightEnchantment, rightLevel);
								int rarityCost = switch (rightEnchantment.getRarity()) {
									case COMMON -> 1;
									case UNCOMMON -> 2;
									case RARE -> 4;
									case VERY_RARE -> 8;
								};
								if (enchantedBook) {
									rarityCost = Math.max(1, rarityCost / 2);
								}
								enchantCost += rarityCost * rightLevel;
								if (leftStack.getCount() > 1) {
									enchantCost = 40;
								}
							}
						}
					}
					if (survival && !canEnchant) {
						this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
						this.cost = 0;
						this.broadcastChanges();
						return;
					}
				}
			}
			if (enchantedBook && !resultStack.isBookEnchantable(rightStack)) {
				resultStack = ItemStack.EMPTY;
			}
			this.cost = repairCost + enchantCost;
			if (enchantCost <= 0) {
				resultStack = ItemStack.EMPTY;
			}
			if (this.cost >= 40 && !this.player.getAbilities().instabuild) {
				resultStack = ItemStack.EMPTY;
			}
			if (!resultStack.isEmpty()) {
				int baseRepairCost = resultStack.getBaseRepairCost();
				if (!rightStack.isEmpty() && baseRepairCost < rightStack.getBaseRepairCost()) {
					baseRepairCost = rightStack.getBaseRepairCost();
				}
				baseRepairCost = calculateIncreasedRepairCost(baseRepairCost);
				resultStack.setRepairCost(baseRepairCost);
				EnchantmentHelper.setEnchantments(map, resultStack);
			}
			this.handler.getResultHandler().setStackInSlot(0, resultStack);
			this.menu.broadcastChanges();
			this.broadcastChanges();
		}
	}
	
	private void broadcastChanges() {
		XBNetworkHandler.INSTANCE.sendToPlayer(this.player, new UpdateAnvilPacket(this.cost));
	}
	
	public int getCost() {
		return this.cost;
	}
	
	private boolean onAnvilUpdate(ItemStack leftStack, ItemStack rightStack, int repairCost) {
		AnvilUpdateEvent event = new AnvilUpdateEvent(leftStack, rightStack, leftStack.getDisplayName().getString(), repairCost, this.player);
		if (MinecraftForge.EVENT_BUS.post(event)) {
			return false;
		} else if (event.getOutput().isEmpty()) {
			return true;
		}
		this.handler.getResultHandler().setStackInSlot(0, event.getOutput());
		this.cost = event.getCost();
		this.repairItemCountCost = event.getMaterialCost();
		this.broadcastChanges();
		return false;
	}
	
	@Override
	public boolean quickMoveStack(ItemStack slotStack, int index) {
		if (908 >= index && index >= 0) { // from container
			return this.menu.moveItemStackTo(slotStack, 938, 940); // into input
		} else if (index == 940) { // from result
			return this.movePreferredMenu(slotStack); // into container
		}
		return false;
	}
	
	@Override
	public void close() {
		this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
	}
}
