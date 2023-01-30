package net.luis.xbackpack.world.inventory.slot;

import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

/**
 *
 * @author Luis-st
 *
 */

public class BackpackSlot extends SlotItemHandler implements MoveableSlot {
	
	public BackpackSlot(IItemHandlerModifiable itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public int getY(int scrollOffset) {
		return this.y - scrollOffset * 18;
	}
	
}
