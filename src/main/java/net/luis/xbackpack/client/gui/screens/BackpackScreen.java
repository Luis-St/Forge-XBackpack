package net.luis.xbackpack.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackScreen extends AbstractContainerScreen<BackpackMenu> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(XBackpack.MOD_ID, "textures/gui/container/backpack.png");
	
	private double scrollOffset = 0;
	
	public BackpackScreen(BackpackMenu backpackMenu, Inventory inventory, Component component) {
		super(backpackMenu, inventory, component);
		this.passEvents = false;
		this.imageWidth = 238;
		this.imageHeight = 202;
		this.inventoryLabelX += 22;
		this.inventoryLabelY = 109;
	}
	
	@Override
	public void render(PoseStack stack, int x, int y, float partialTicks) {
		super.render(stack, x, y, partialTicks);
		this.renderTooltip(stack, x, y);
	}
	
	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int x, int y) {
		this.renderBackground(stack);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);	
		this.blit(stack, this.leftPos, this.topPos, 0, 0, 238, 184);
		this.blit(stack, this.leftPos, this.topPos + 89, 0, 89, 198, 184);
		int scrollPosition = (int) (this.topPos + 18 + this.scrollOffset);
		this.blit(stack, this.leftPos + 196, scrollPosition, 244, 0, 12, 15);
	}
	
	private boolean isInScrollbar(double mouseX, double mouseY) {
		double topX = this.leftPos + 196.0;
		double topY = this.topPos + 16.0;
		if (topX + 14 >= mouseX && mouseX >= topX && topY + 90 >= mouseY && mouseY >= topY) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0 && this.isInScrollbar(mouseX, mouseY)) {
			this.scrollOffset = Mth.clamp(mouseY - this.topPos - 25.5, 0.0, 73.0);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (button == 0 && this.isInScrollbar(mouseX, mouseY)) {
			this.scrollOffset = Mth.clamp(mouseY - this.topPos - 25.5, 0.0, 73.0);
			return true;
		}
		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		this.scrollOffset = (int) Mth.clamp(this.scrollOffset - delta * 2.0, 0.0, 73.0);
		return true;
	}

}
