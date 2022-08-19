package net.luis.xbackpack.world.inventory.extension;

import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.world.entity.player.Player;

/**
 *
 * @author Luis-st
 *
 */

@FunctionalInterface
public interface ExtensionMenuFactory {
	
	AbstractExtensionMenu create(BackpackMenu menu, Player player);
	
}
