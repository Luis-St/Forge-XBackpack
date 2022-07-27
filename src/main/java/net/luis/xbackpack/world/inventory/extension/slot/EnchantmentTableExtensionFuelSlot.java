package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.inventory.extension.EnchantmentTableExtensionMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.IItemHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class EnchantmentTableExtensionFuelSlot extends ExtensionSlot {
	
	public EnchantmentTableExtensionFuelSlot(EnchantmentTableExtensionMenu extensionMenu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(extensionMenu, itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public EnchantmentTableExtensionMenu getMenu() {
		return (EnchantmentTableExtensionMenu) super.getMenu();
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.is(Tags.Items.ENCHANTING_FUELS);
	}
	
}
