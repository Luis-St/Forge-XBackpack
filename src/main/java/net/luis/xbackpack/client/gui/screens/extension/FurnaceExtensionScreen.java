package net.luis.xbackpack.client.gui.screens.extension;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.luis.xbackpack.client.gui.screens.AbstractExtensionContainerScreen;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;

import java.util.List;

/**
 *
 * @author Luis-st
 *
 */

public class FurnaceExtensionScreen extends AbstractExtensionScreen {
	
	private int cookingProgress;
	private int fuelProgress;
	
	public FurnaceExtensionScreen(AbstractExtensionContainerScreen<?> screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtensions.FURNACE.get(), extensions);
	}
	
	@Override
	protected void renderAdditional(PoseStack stack, float partialTicks, int mouseX, int mouseY, boolean open) {
		if (open) {
			RenderSystem.setShaderTexture(0, this.getTexture());
			this.screen.blit(stack, this.leftPos + this.imageWidth + 24, this.topPos + 89, 86, 14, this.cookingProgress, 17);
			this.screen.blit(stack, this.leftPos + this.imageWidth + 5, this.topPos + 90 + 13 - this.fuelProgress, 86, 13 - this.fuelProgress, 14, this.fuelProgress);
		}
	}
	
	public void update(int cookingProgress, int fuelProgress) {
		this.cookingProgress = cookingProgress;
		this.fuelProgress = fuelProgress;
	}
	
}
