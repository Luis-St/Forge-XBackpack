package net.luis.xbackpack.client.gui.screens.extension;

import java.util.List;

import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.world.extension.BackpackExtension;

/**
 * 
 * @author Luis-st
 *
 */

public class SmithingTableExtensionScreen extends AbstractBackpackExtensionScreen {

	public SmithingTableExtensionScreen(BackpackScreen screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtension.SMITHING_TABLE.get(), extensions);
	}

}
