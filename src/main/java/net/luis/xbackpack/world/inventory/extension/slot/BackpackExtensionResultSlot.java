package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.extension.BackpackExtension;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackExtensionResultSlot extends ResultSlot {
	
	private final BackpackExtension extension;
	
	public BackpackExtensionResultSlot(Player player, CraftingContainer craftingContainer, Container container, int index, int xPosition, int yPosition, BackpackExtension extension) {
		super(player, craftingContainer, container, index, xPosition, yPosition);
		this.extension = extension;
	}
	
	public BackpackExtension getExtension() {
		return this.extension;
	}

}
