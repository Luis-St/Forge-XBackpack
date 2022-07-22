package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.extension.BackpackExtension;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class ExtensionSlot extends SlotItemHandler {
	
	private final BackpackExtension extension;
	
	public ExtensionSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, BackpackExtension extension) {
		super(itemHandler, index, xPosition, yPosition);
		this.extension = extension;
	}
	
	public BackpackExtension getExtension() {
		return this.extension;
	}
	
}
