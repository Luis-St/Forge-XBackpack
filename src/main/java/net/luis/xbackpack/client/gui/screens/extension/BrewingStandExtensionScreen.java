package net.luis.xbackpack.client.gui.screens.extension;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.minecraft.util.Mth;

/**
 * 
 * @author Luis-st
 *
 */

public class BrewingStandExtensionScreen extends AbstractExtensionScreen {
	
	private static final int[] BUBBLES = new int[] {
			29, 24, 20, 16, 11, 6, 0
	};
	private int fuel;
	private int brewTime;
	
	public BrewingStandExtensionScreen(BackpackScreen screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtensions.BREWING_STAND.get(), extensions);
	}
	
	@Override
	protected void renderAdditional(PoseStack stack, float partialTicks, int mouseX, int mouseY, boolean open) {
		if (open) {
			RenderSystem.setShaderTexture(0, this.getTexture());
			this.renderFuel(stack);
			this.renderBrewing(stack);
		}
	}
	
	private void renderFuel(PoseStack stack) {
		int fuel = Mth.clamp((18 * this.fuel + 20 - 1) / 20, 0, 18);
		if (fuel > 0) {
			this.screen.blit(stack, this.leftPos + this.imageWidth + 38, this.topPos + 173, 106, 29, fuel, 4);
		}
	}
	
	private void renderBrewing(PoseStack stack) {
		if (this.brewTime > 0) {
			int progress = (int) (28.0 * (1.0 - (double) this.brewTime / 400.0));
			if (progress > 0) {
				this.screen.blit(stack, this.leftPos + this.imageWidth + 75, this.topPos + 145, 106, 0, 9, progress);
			}
			int bubbles = BUBBLES[this.brewTime / 2 % 7];
			if (bubbles > 0) {
				this.screen.blit(stack, this.leftPos + this.imageWidth + 42, this.topPos + 143 + 29 - bubbles, 115, 29 - bubbles, 12, bubbles);
			}
		}
	}
	
	public void update(int fuel, int brewTime) {
		this.fuel = fuel;
		this.brewTime = brewTime;
	}

}
