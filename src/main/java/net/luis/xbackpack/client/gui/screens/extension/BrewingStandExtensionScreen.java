package net.luis.xbackpack.client.gui.screens.extension;

import net.luis.xbackpack.client.gui.screens.AbstractExtensionContainerScreen;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

import java.util.List;

/**
 *
 * @author Luis-st
 *
 */

public class BrewingStandExtensionScreen extends AbstractExtensionScreen {
	
	private static final int[] BUBBLES = new int[]{
			29, 24, 20, 16, 11, 6, 0
	};
	private int fuel;
	private int brewTime;
	
	public BrewingStandExtensionScreen(AbstractExtensionContainerScreen<?> screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtensions.BREWING_STAND.get(), extensions);
	}
	
	@Override
	protected void renderAdditional(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, boolean open) {
		if (open) {
			this.renderFuel(graphics);
			this.renderBrewing(graphics);
		}
	}
	
	private void renderFuel(GuiGraphics graphics) {
		int fuel = Mth.clamp((18 * this.fuel + 20 - 1) / 20, 0, 18);
		if (fuel > 0) {
			graphics.blit(this.getTexture(), this.leftPos + this.imageWidth + 38, this.topPos + 173, 106, 29, fuel, 4);
		}
	}
	
	private void renderBrewing(GuiGraphics graphics) {
		if (this.brewTime > 0) {
			int progress = (int) (28.0 * (1.0 - (double) this.brewTime / 400.0));
			if (progress > 0) {
				graphics.blit(this.getTexture(), this.leftPos + this.imageWidth + 75, this.topPos + 145, 106, 0, 9, progress);
			}
			int bubbles = BUBBLES[this.brewTime / 2 % 7];
			if (bubbles > 0) {
				graphics.blit(this.getTexture(), this.leftPos + this.imageWidth + 42, this.topPos + 143 + 29 - bubbles, 115, 29 - bubbles, 12, bubbles);
			}
		}
	}
	
	public void update(int fuel, int brewTime) {
		this.fuel = fuel;
		this.brewTime = brewTime;
	}
}
