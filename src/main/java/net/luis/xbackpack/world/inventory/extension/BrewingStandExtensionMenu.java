package net.luis.xbackpack.world.inventory.extension;

import java.util.function.Consumer;

import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionSlot;
import net.luis.xbackpack.world.inventory.handler.BrewingHandler;
import net.luis.xbackpack.world.inventory.handler.progress.ProgressHandler;
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

/**
 * 
 * @author Luis-st
 *
 */

public class BrewingStandExtensionMenu extends AbstractExtensionMenu {
	
	private final BrewingHandler handler;
	private final ProgressHandler progressHandler;
	
	public BrewingStandExtensionMenu(BackpackMenu menu, Player player) {
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
	public void addSlots(Consumer<Slot> consumer) {
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 0, 277, 146) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return BrewingRecipeRegistry.isValidIngredient(stack);
			}
		});
		consumer.accept(new ExtensionSlot(this, this.handler.getFuelHandler(), 0, 225, 146) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.is(Items.BLAZE_POWDER);
			}
		});
		for (int i = 0; i < 3; i++) {
			consumer.accept(new ExtensionSlot(this, this.handler.getResultHandler(), i, 254 + i * 23, i == 1 ? 187 : 180) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return BrewingRecipeRegistry.isValidInput(stack);
				}
				
				@Override
				public int getMaxStackSize() {
					return 1;
				}
				
				@Override
				public void onTake(Player player, ItemStack stack) {
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
	
}
