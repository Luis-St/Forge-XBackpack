package net.luis.xbackpack.client.gui.screens.extension;

import java.util.List;

import net.luis.xbackpack.world.extension.BackpackExtension;

/**
 * 
 * @author Luis-st
 *
 */

public interface BackpackExtensionScreenHolder {
	
	List<AbstractBackpackExtensionScreen> getExtensionScreens();
	
	AbstractBackpackExtensionScreen getExtensionScreen(BackpackExtension extension);
	
}
