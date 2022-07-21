package net.luis.xbackpack.client.gui.screens.extension;

import java.util.List;

import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.world.extension.BackpackExtension;

/**
 * 
 * @author Luis-st
 *
 */

public class AnvilExtensionScreen extends AbstractBackpackExtensionScreen {

	public AnvilExtensionScreen(BackpackScreen screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtension.ANVIL.get(), extensions);
	}

}
