package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.extension.AbstractExtensionMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;

/**
 *
 * @author Luis-St
 *
 */

public class ExtensionResultSlot extends ResultSlot implements ExtensionMenuSlot {
	
	private final AbstractExtensionMenu extensionMenu;
	
	public ExtensionResultSlot(AbstractExtensionMenu extensionMenu, Player player, CraftingContainer craftingContainer, Container container, int index, int xPosition, int yPosition) {
		super(player, craftingContainer, container, index, xPosition, yPosition);
		this.extensionMenu = extensionMenu;
	}
	
	@Override
	public AbstractExtensionMenu getMenu() {
		return this.extensionMenu;
	}
	
	@Override
	public BackpackExtension getExtension() {
		return this.extensionMenu.getExtension();
	}
}
