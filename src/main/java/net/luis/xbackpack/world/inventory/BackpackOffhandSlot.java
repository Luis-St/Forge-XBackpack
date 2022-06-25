package net.luis.xbackpack.world.inventory;

import com.mojang.datafixers.util.Pair;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackOffhandSlot extends Slot {

	/**
	 * constructor for a {@link BackpackOffhandSlot}
	 */
	public BackpackOffhandSlot(Container container, int index, int xPosition, int yPosition) {
		super(container, index, xPosition, yPosition);
	}
	
	/**
	 * @return correct background for the Slot
	 */
	@Override
	public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
		return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
	}
	
}
