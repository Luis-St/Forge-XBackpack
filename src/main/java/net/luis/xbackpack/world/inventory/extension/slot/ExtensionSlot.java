package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.extension.AbstractExtensionMenu;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class ExtensionSlot extends SlotItemHandler {
	
	private final AbstractExtensionMenu extensionMenu;
	
	public ExtensionSlot(AbstractExtensionMenu extensionMenu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.extensionMenu = extensionMenu;
	}
	
	public AbstractExtensionMenu getMenu() {
		return this.extensionMenu;
	}
	
	public BackpackExtension getExtension() {
		return this.extensionMenu.getExtension();
	}
	
}
