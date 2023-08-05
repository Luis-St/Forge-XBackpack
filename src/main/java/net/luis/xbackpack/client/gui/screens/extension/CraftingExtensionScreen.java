package net.luis.xbackpack.client.gui.screens.extension;

import net.luis.xbackpack.client.gui.screens.AbstractExtensionContainerScreen;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;

import java.util.List;

/**
 *
 * @author Luis-St
 *
 */

public class CraftingExtensionScreen extends AbstractExtensionScreen {
	
	public CraftingExtensionScreen(AbstractExtensionContainerScreen<?> screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtensions.CRAFTING_TABLE.get(), extensions);
	}
}
