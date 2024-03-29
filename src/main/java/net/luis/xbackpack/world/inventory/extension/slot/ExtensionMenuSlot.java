package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.extension.AbstractExtensionMenu;

/**
 *
 * @author Luis-St
 *
 */

public interface ExtensionMenuSlot {
	
	AbstractExtensionMenu getMenu();
	
	BackpackExtension getExtension();
}
