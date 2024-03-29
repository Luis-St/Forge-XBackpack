package net.luis.xbackpack.world.inventory.slot;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;

/**
 *
 * @author Luis-St
 *
 */

public class BackpackOffhandSlot extends Slot {
	
	public BackpackOffhandSlot(Container container, int index, int xPosition, int yPosition) {
		super(container, index, xPosition, yPosition);
	}
	
	@Override
	public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
		return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
	}
}
