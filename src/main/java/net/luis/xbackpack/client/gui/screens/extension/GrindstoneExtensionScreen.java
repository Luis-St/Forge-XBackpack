package net.luis.xbackpack.client.gui.screens.extension;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.luis.xbackpack.client.gui.screens.AbstractExtensionContainerScreen;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.handler.CraftingHandler;
import net.minecraft.client.gui.GuiComponent;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Luis-st
 *
 */

public class GrindstoneExtensionScreen extends AbstractExtensionScreen {
	
	private CraftingHandler handler;
	
	public GrindstoneExtensionScreen(AbstractExtensionContainerScreen<?> screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtensions.GRINDSTONE.get(), extensions);
	}
	
	@Override
	protected void init() {
		this.handler = BackpackProvider.get(Objects.requireNonNull(this.minecraft.player)).getGrindstoneHandler();
	}
	
	@Override
	protected void renderAdditional(PoseStack stack, float partialTicks, int mouseX, int mouseY, boolean open) {
		if (open) {
			RenderSystem.setShaderTexture(0, this.getTexture());
			ItemStackHandler handler = this.handler.getInputHandler();
			if ((!handler.getStackInSlot(0).isEmpty() || !handler.getStackInSlot(1).isEmpty()) && this.handler.getResultHandler().getStackInSlot(0).isEmpty()) {
				GuiComponent.blit(stack, this.leftPos + this.imageWidth + 60, this.topPos + 184, 112, 0, 22, 21);
			}
		}
	}
	
}
