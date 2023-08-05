package net.luis.xbackpack.world.inventory.extension;

import net.luis.xbackpack.world.inventory.AbstractExtensionContainerMenu;
import net.minecraft.world.entity.player.Player;

/**
 *
 * @author Luis-St
 *
 */

@FunctionalInterface
public interface ExtensionMenuFactory {
	
	AbstractExtensionMenu create(AbstractExtensionContainerMenu menu, Player player);
}
