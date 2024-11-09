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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *
 * @author Luis-St
 *
 */

public class BrewingStandExtensionScreen extends AbstractExtensionScreen {
	
	private static final int[] BUBBLES = {
		29, 24, 20, 16, 11, 6, 0
	};
	private int fuel;
	private int brewTime;
	
	public BrewingStandExtensionScreen(@NotNull AbstractExtensionContainerScreen<?> screen, @NotNull List<BackpackExtension> extensions) {
		super(screen, BackpackExtensions.BREWING_STAND.get(), extensions);
	}
	
	@Override
	protected void renderAdditional(@NotNull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, boolean open) {
		if (open) {
			this.renderFuel(graphics);
			this.renderBrewing(graphics);
		}
	}
	
	private void renderFuel(@NotNull GuiGraphics graphics) {
		int fuel = Mth.clamp((18 * this.fuel + 20 - 1) / 20, 0, 18);
		if (fuel > 0) {
			int imageWidth = this.extension.getImageWidth();
			int imageHeight = this.extension.getImageHeight();
			graphics.blit(RenderType::guiTextured, this.getTexture(), this.leftPos + this.imageWidth + 38, this.topPos + 173, 106, 29, fuel, 4, imageWidth, imageHeight);
		}
	}
	
	private void renderBrewing(@NotNull GuiGraphics graphics) {
		int imageWidth = this.extension.getImageWidth();
		int imageHeight = this.extension.getImageHeight();
		if (this.brewTime > 0) {
			int progress = (int) (28.0 * (1.0 - this.brewTime / 400.0));
			if (progress > 0) {
				graphics.blit(RenderType::guiTextured, this.getTexture(), this.leftPos + this.imageWidth + 75, this.topPos + 145, 106, 0, 9, progress, imageWidth, imageHeight);
			}
			int bubbles = BUBBLES[this.brewTime / 2 % 7];
			if (bubbles > 0) {
				graphics.blit(RenderType::guiTextured, this.getTexture(), this.leftPos + this.imageWidth + 42, this.topPos + 143 + 29 - bubbles, 115, 29 - bubbles, 12, bubbles, imageWidth, imageHeight);
			}
		}
	}
	
	public void update(int fuel, int brewTime) {
		this.fuel = fuel;
		this.brewTime = brewTime;
	}
}
