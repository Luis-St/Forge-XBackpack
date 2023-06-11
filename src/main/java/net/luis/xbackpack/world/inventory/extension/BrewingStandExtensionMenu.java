package net.luis.xbackpack.world.inventory.extension;

import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.AbstractExtensionContainerMenu;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionSlot;
import net.luis.xbackpack.world.inventory.handler.CraftingFuelHandler;
import net.luis.xbackpack.world.inventory.progress.ProgressHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 *
 * @author Luis-st
 *
 */

public class BrewingStandExtensionMenu extends AbstractExtensionMenu {
	
	private final CraftingFuelHandler handler;
	private final ProgressHandler progressHandler;
	
	public BrewingStandExtensionMenu(AbstractExtensionContainerMenu menu, Player player) {
		super(menu, player, BackpackExtensions.BREWING_STAND.get());
		IBackpack backpack = BackpackProvider.get(player);
		this.handler = backpack.getBrewingHandler();
		this.progressHandler = backpack.getBrewHandler();
	}
	
	@Override
	public void open() {
		this.progressHandler.broadcastChanges();
	}
	
	@Override
	public void addSlots(@NotNull Consumer<Slot> consumer) {
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 0, 277, 146) {
			@Override
			public boolean mayPlace(@NotNull ItemStack stack) {
				return BrewingRecipeRegistry.isValidIngredient(stack);
			}
		});
		consumer.accept(new ExtensionSlot(this, this.handler.getFuelHandler(), 0, 225, 146) {
			@Override
			public boolean mayPlace(@NotNull ItemStack stack) {
				return stack.is(Items.BLAZE_POWDER);
			}
		});
		for (int i = 0; i < 3; i++) {
			consumer.accept(new ExtensionSlot(this, this.handler.getResultHandler(), i, 254 + i * 23, i == 1 ? 187 : 180) {
				@Override
				public boolean mayPlace(@NotNull ItemStack stack) {
					return BrewingRecipeRegistry.isValidInput(stack);
				}
				
				@Override
				public int getMaxStackSize() {
					return 1;
				}
				
				@Override
				public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
					BrewingStandExtensionMenu.this.onTake(player, stack);
					super.onTake(player, stack);
				}
			});
		}
	}
	
	private void onTake(Player player, ItemStack stack) {
		Potion potion = PotionUtils.getPotion(stack);
		if (player instanceof ServerPlayer serverPlayer) {
			ForgeEventFactory.onPlayerBrewedPotion(player, stack);
			CriteriaTriggers.BREWED_POTION.trigger(serverPlayer, potion);
		}
	}
	
	@Override
	public boolean quickMoveStack(ItemStack slotStack, int index) {
		if (908 >= index && index >= 0) { // from container
			if (slotStack.is(Items.BLAZE_POWDER) && this.canQuickMovePowder()) {
				return this.menu.moveItemStackTo(slotStack, 947, 948); // into fuel
			} else if (BrewingRecipeRegistry.isValidIngredient(slotStack)) {
				return this.menu.moveItemStackTo(slotStack, 946, 947); // into input
			} else if (BrewingRecipeRegistry.isValidInput(slotStack)) {
				return this.menu.moveItemStackTo(slotStack, 948, 451); // into result
			}
		} else if (950 >= index && index >= 946) { // from extension
			return this.movePreferredMenu(slotStack); // into container
		}
		return false;
	}
	
	private boolean canQuickMovePowder() {
		ItemStack stack = this.handler.getFuelHandler().getStackInSlot(0);
		return stack.isEmpty() || (stack.is(Items.BLAZE_POWDER) && stack.getMaxStackSize() > stack.getCount());
	}
}
