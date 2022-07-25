package net.luis.xbackpack.world.inventory.extension;

import java.util.Map;
import java.util.function.Consumer;

import net.luis.xbackpack.network.XBackpackNetworkHandler;
import net.luis.xbackpack.network.packet.extension.UpdateAnvilExtension;
import net.luis.xbackpack.world.capability.XBackpackCapabilities;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.extension.slot.AnvilExtensionResultSlot;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionSlot;
import net.luis.xbackpack.world.inventory.handler.CraftingHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.network.PacketDistributor;

/**
 * 
 * @author Luis-st
 *
 */

public class AnvilExtensionMenu extends AbstractExtensionMenu {
	
	private final CraftingHandler handler;
	private int repairItemCountCost;
	private int cost;
	
	public AnvilExtensionMenu(BackpackMenu menu, Player player) {
		super(menu, player, BackpackExtension.ANVIL.get());
		this.handler = this.player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new).getAnvilHandler();
	}
	
	@Override
	public void open() {
		if (!this.handler.getInputHandler().getStackInSlot(0).isEmpty() && !this.handler.getInputHandler().getStackInSlot(1).isEmpty()) {
			this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
			this.createResult();
		}
	}
	
	@Override
	public void addSlots(Consumer<Slot> consumer) {
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 0, 225, 73));
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 1, 260, 73));
		consumer.accept(new AnvilExtensionResultSlot(this, this.handler.getResultHandler(), 0, 304, 73));
	}
	
	public boolean mayPickup(Player player, boolean hasResult) {
		return (player.getAbilities().instabuild || player.experienceLevel >= this.cost) && this.cost > 0;
	}
	
	public void onTake(Player player, ItemStack stack) {
		if (!player.getAbilities().instabuild) {
			player.giveExperienceLevels(-this.cost);
		}
		this.handler.getInputHandler().setStackInSlot(0, ItemStack.EMPTY);
		if (this.repairItemCountCost > 0) {
			ItemStack rigthStack = this.handler.getInputHandler().getStackInSlot(1);
			if (!rigthStack.isEmpty() && rigthStack.getCount() > this.repairItemCountCost) {
				rigthStack.shrink(this.repairItemCountCost);
				this.handler.getInputHandler().setStackInSlot(1, rigthStack);
			} else {
				this.handler.getInputHandler().setStackInSlot(1, ItemStack.EMPTY);
			}
		} else {
			this.handler.getInputHandler().setStackInSlot(1, ItemStack.EMPTY);
		}
		this.cost = 0;
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
		int renameCost = 0;
		if (leftStack.isEmpty()) {
			this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
			this.cost = 0;
			this.broadcastCost();
		} else {
			ItemStack resultStack = leftStack.copy();
			ItemStack rightStack = this.handler.getInputHandler().getStackInSlot(1);
			Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(resultStack);
			repairCost += leftStack.getBaseRepairCost() + (rightStack.isEmpty() ? 0 : rightStack.getBaseRepairCost());
			this.repairItemCountCost = 0;
			boolean enchantedBook = false;
			if (!rightStack.isEmpty()) {
				if (!this.onAnvilUpdate(leftStack, rightStack, repairCost)) {
					return;
				}
				enchantedBook = rightStack.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(rightStack).isEmpty();
				if (resultStack.isDamageableItem() && resultStack.getItem().isValidRepairItem(leftStack, rightStack)) {
					int damage = Math.min(resultStack.getDamageValue(), resultStack.getMaxDamage() / 4);
					if (damage <= 0) {
						this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
						this.cost = 0;
						this.broadcastCost();
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
						this.broadcastCost();
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
								int rarityCost = 0;
								switch (rightEnchantment.getRarity()) {
									case COMMON:
										rarityCost = 1;
										break;
									case UNCOMMON:
										rarityCost = 2;
										break;
									case RARE:
										rarityCost = 4;
										break;
									case VERY_RARE:
										rarityCost = 8;
								}
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
						this.broadcastCost();
						return;
					}
				}
			}
			if (enchantedBook && !resultStack.isBookEnchantable(rightStack))
				resultStack = ItemStack.EMPTY;
			this.cost = 0;
			this.cost = repairCost + enchantCost;
			if (enchantCost <= 0) {
				resultStack = ItemStack.EMPTY;
			}
			if (renameCost == enchantCost && renameCost > 0 && this.cost >= 40) {
				this.cost = 39;
			}
			if (this.cost >= 40 && !this.player.getAbilities().instabuild) {
				resultStack = ItemStack.EMPTY;
			}
			if (!resultStack.isEmpty()) {
				int baseRepairCost = resultStack.getBaseRepairCost();
				if (!rightStack.isEmpty() && baseRepairCost < rightStack.getBaseRepairCost()) {
					baseRepairCost = rightStack.getBaseRepairCost();
				}
				if (renameCost != enchantCost || renameCost == 0) {
					baseRepairCost = calculateIncreasedRepairCost(baseRepairCost);
				}
				resultStack.setRepairCost(baseRepairCost);
				EnchantmentHelper.setEnchantments(map, resultStack);
			}
			this.handler.getResultHandler().setStackInSlot(0, resultStack);
			this.menu.broadcastChanges();
			this.broadcastCost();
		}
	}
	
	private void broadcastCost() {
		if (this.player instanceof ServerPlayer player) {
			XBackpackNetworkHandler.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new UpdateAnvilExtension(this.cost));
		}
	}
	
	private static int calculateIncreasedRepairCost(int cost) {
		return cost * 2 + 1;
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
		return true;
	}
	
	@Override
	public void close() {
		this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
	}

}
