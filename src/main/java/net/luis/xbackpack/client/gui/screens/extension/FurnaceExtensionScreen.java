/*
 * XBackpack
 * Copyright (C) 2024 Luis Staudt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.luis.xbackpack.client.gui.screens.extension;

import net.luis.xbackpack.client.gui.screens.AbstractExtensionContainerScreen;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

/**
 *
 * @author Luis-St
 *
 */

public class FurnaceExtensionScreen extends AbstractExtensionScreen {
	
	private int cookingProgress;
	private int fuelProgress;
	
	public FurnaceExtensionScreen(AbstractExtensionContainerScreen<?> screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtensions.FURNACE.get(), extensions);
	}
	
	@Override
	protected void renderAdditional(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, boolean open) {
		if (open) {
			graphics.blit(this.getTexture(), this.leftPos + this.imageWidth + 24, this.topPos + 89, 86, 14, this.cookingProgress, 17);
			graphics.blit(this.getTexture(), this.leftPos + this.imageWidth + 5, this.topPos + 90 + 13 - this.fuelProgress, 86, 13 - this.fuelProgress, 14, this.fuelProgress);
		}
	}
	
	public void update(int cookingProgress, int fuelProgress) {
		this.cookingProgress = cookingProgress;
		this.fuelProgress = fuelProgress;
	}
}
