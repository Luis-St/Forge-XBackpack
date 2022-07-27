package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.inventory.extension.BrewingStandExtensionMenu;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.IItemHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class BrewingStandExtensionResultSlot extends ExtensionSlot {

	public BrewingStandExtensionResultSlot(BrewingStandExtensionMenu extensionMenu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(extensionMenu, itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public BrewingStandExtensionMenu getMenu() {
		return (BrewingStandExtensionMenu) super.getMenu();
	}
	
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
		Potion potion = PotionUtils.getPotion(stack);
		if (player instanceof ServerPlayer serverPlayer) {
			ForgeEventFactory.onPlayerBrewedPotion(player, stack);
			CriteriaTriggers.BREWED_POTION.trigger(serverPlayer, potion);
		}
		super.onTake(player, stack);
	}
	
}
