package net.luis.xbackpack.jei;

import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.extension.ExtensionState;
import net.minecraft.client.renderer.Rect2i;

/**
 *
 * @author Luis-st
 *
 */

public class BackpackContainerHandler implements IGuiContainerHandler<BackpackScreen> {
	
	@Override
	@SuppressWarnings("resource")
	public List<Rect2i> getGuiExtraAreas(BackpackScreen screen) {
		if (BackpackProvider.get(screen.getMinecraft().player).getConfig().getWithState(ExtensionState.UNLOCKED).size() > 0) {
			List<Rect2i> extraAreas = Lists.newArrayList(new Rect2i(screen.getGuiLeft() + screen.getXSize(), screen.getGuiTop(), 30, screen.getYSize()));
			BackpackExtension extension = screen.getExtension();
			if (extension != BackpackExtensions.NO.get()) {
				extraAreas.add(new Rect2i(screen.getGuiLeft() + screen.getXSize(), screen.getGuiTop() +  screen.getExtensionOffset(extension), extension.getImageWidth(), extension.getImageHeight()));
			}
			return extraAreas;
		}
		return Lists.newArrayList();
	}
	
}
