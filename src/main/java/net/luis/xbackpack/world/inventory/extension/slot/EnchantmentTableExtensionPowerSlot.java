package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.inventory.extension.EnchantmentTableExtensionMenu;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.IItemHandler;

public class EnchantmentTableExtensionPowerSlot extends ExtensionSlot {
	
	public EnchantmentTableExtensionPowerSlot(EnchantmentTableExtensionMenu extensionMenu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(extensionMenu, itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public EnchantmentTableExtensionMenu getMenu() {
		return (EnchantmentTableExtensionMenu) super.getMenu();
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.is(Tags.Items.BOOKSHELVES) || stack.getItem() instanceof BookItem;
	}
	
	@Override
	public int getMaxStackSize(ItemStack stack) {
		if (stack.is(Tags.Items.BOOKSHELVES)) {
			return 15;
		} else if (stack.getItem() instanceof BookItem) {
			return 45;
		}
		return super.getMaxStackSize(stack);
	}
	
}
