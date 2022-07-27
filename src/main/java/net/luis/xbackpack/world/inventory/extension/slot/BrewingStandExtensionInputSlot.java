package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.inventory.extension.BrewingStandExtensionMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.items.IItemHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class BrewingStandExtensionInputSlot extends ExtensionSlot {

	public BrewingStandExtensionInputSlot(BrewingStandExtensionMenu extensionMenu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(extensionMenu, itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public BrewingStandExtensionMenu getMenu() {
		return (BrewingStandExtensionMenu) super.getMenu();
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return BrewingRecipeRegistry.isValidIngredient(stack);
	}
	
}