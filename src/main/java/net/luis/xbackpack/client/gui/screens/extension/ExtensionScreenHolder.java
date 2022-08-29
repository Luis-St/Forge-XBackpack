package net.luis.xbackpack.client.gui.screens.extension;

import java.util.List;

import net.luis.xbackpack.world.extension.BackpackExtension;

/**
 * 
 * @author Luis-st
 *
 */

public interface ExtensionScreenHolder {
	
	BackpackExtension getExtension();
	
	List<AbstractExtensionScreen> getExtensionScreens();
	
	AbstractExtensionScreen getExtensionScreen(BackpackExtension extension);
	
}
