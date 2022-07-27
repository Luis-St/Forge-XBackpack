package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.inventory.extension.EnchantmentTableExtensionMenu;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class EnchantmentTableExtensionInputSlot extends ExtensionSlot {

	public EnchantmentTableExtensionInputSlot(EnchantmentTableExtensionMenu extensionMenu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(extensionMenu, itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public EnchantmentTableExtensionMenu getMenu() {
		return (EnchantmentTableExtensionMenu) super.getMenu();
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.isEnchantable() || stack.getItem() instanceof BookItem;
	}
	
	@Override
	public int getMaxStackSize() {
		return 1;
	}
	
}
