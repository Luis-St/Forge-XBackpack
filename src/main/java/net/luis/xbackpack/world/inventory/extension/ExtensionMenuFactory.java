package net.luis.xbackpack.world.inventory.extension;

import net.luis.xbackpack.world.inventory.AbstractExtensionContainerMenu;
import net.minecraft.world.entity.player.Player;

/**
 *
 * @author Luis-st
 *
 */

@FunctionalInterface
public interface ExtensionMenuFactory {
	
	AbstractExtensionMenu create(AbstractExtensionContainerMenu menu, Player player);
}
