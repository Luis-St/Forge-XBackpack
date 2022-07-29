package net.luis.xbackpack.client.gui.screens.extension;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.world.capability.XBackpackCapabilities;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.handler.CraftingHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class GrindstoneExtensionScreen extends AbstractExtensionScreen {
	
	private CraftingHandler handler;
	
	public GrindstoneExtensionScreen(BackpackScreen screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtension.GRINDSTONE.get(), extensions);
	}
	
	@Override
	protected void init() {
		this.handler = this.minecraft.player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new).getGrindstoneHandler();
	}
	
	@Override
	protected void renderAdditional(PoseStack stack, float partialTicks, int mouseX, int mouseY, boolean open) {
		if (open) {
			RenderSystem.setShaderTexture(0, this.getTexture());
			ItemStackHandler handler = this.handler.getInputHandler();
			if ((!handler.getStackInSlot(0).isEmpty() || !handler.getStackInSlot(1).isEmpty()) && this.handler.getResultHandler().getStackInSlot(0).isEmpty()) {
				this.screen.blit(stack, this.leftPos + this.imageWidth + 60, this.topPos + 184, 112, 0, 22, 21);
			}
		}
	}

}
