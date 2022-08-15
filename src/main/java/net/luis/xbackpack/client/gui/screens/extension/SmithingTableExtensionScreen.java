package net.luis.xbackpack.client.gui.screens.extension;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.world.capability.XBCapabilities;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.handler.CraftingHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class SmithingTableExtensionScreen extends AbstractExtensionScreen {
	
	private CraftingHandler handler;
	
	public SmithingTableExtensionScreen(BackpackScreen screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtension.SMITHING_TABLE.get(), extensions);
	}
	
	@Override
	protected void init() {
		this.handler = this.minecraft.player.getCapability(XBCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new).getSmithingHandler();
	}
	
	@Override
	protected void renderAdditional(PoseStack stack, float partialTicks, int mouseX, int mouseY, boolean open) {
		if (open) {
			RenderSystem.setShaderTexture(0, this.getTexture());
			ItemStackHandler handler = this.handler.getInputHandler();
			if ((!handler.getStackInSlot(0).isEmpty() || !handler.getStackInSlot(1).isEmpty()) && this.handler.getResultHandler().getStackInSlot(0).isEmpty()) {
				this.screen.blit(stack, this.leftPos + this.imageWidth + 59, this.topPos + 191, 111, 0, 22, 21);
			}
		}
	}
	
}
