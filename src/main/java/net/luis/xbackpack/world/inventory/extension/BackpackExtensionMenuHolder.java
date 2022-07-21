package net.luis.xbackpack.world.inventory.extension;

import java.util.List;

import net.luis.xbackpack.world.extension.BackpackExtension;

/**
 * 
 * @author Luis-st
 *
 */

public interface BackpackExtensionMenuHolder {
	
	List<AbstractBackpackExtensionMenu> getExtensionScreens();
	
	AbstractBackpackExtensionMenu getExtensionScreen(BackpackExtension extension);
	
}
