package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.inventory.extension.FurnaceExtensionMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class FurnaceExtensionResultStorageSlot extends ExtensionSlot {

	public FurnaceExtensionResultStorageSlot(FurnaceExtensionMenu extensionMenu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(extensionMenu, itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
	
}
