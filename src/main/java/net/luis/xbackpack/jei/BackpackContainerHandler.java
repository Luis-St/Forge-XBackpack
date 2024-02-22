package net.luis.xbackpack.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Luis-St
 *
 */

public class BackpackContainerHandler implements IGuiContainerHandler<BackpackScreen> {
	
	@Override
	public @NotNull List<Rect2i> getGuiExtraAreas(@NotNull BackpackScreen screen) {
		LocalPlayer player = screen.getMinecraft().player;
		if (player == null) {
			return Lists.newArrayList();
		}
		if (!BackpackProvider.get(player).getConfig().getExtensionConfig().getWithState(BackpackExtensionState.UNLOCKED).isEmpty()) {
			List<Rect2i> extraAreas = Lists.newArrayList(new Rect2i(screen.getGuiLeft() + screen.getXSize(), screen.getGuiTop(), 30, screen.getYSize()));
			BackpackExtension extension = screen.getExtension();
			if (extension != BackpackExtensions.NO.get()) {
				extraAreas.add(new Rect2i(screen.getGuiLeft() + screen.getXSize(), screen.getGuiTop() + screen.getExtensionOffset(extension), extension.getImageWidth(), extension.getImageHeight()));
			}
			return extraAreas;
		}
		return Lists.newArrayList();
	}
}