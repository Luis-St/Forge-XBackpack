package net.luis.xbackpack.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.slot.MoveableSlot;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackScreen extends AbstractScrollableContainerScreen<BackpackMenu> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(XBackpack.MOD_ID, "textures/gui/container/backpack.png");
	
	public BackpackScreen(BackpackMenu menu, Inventory inventory, Component titleComponent) {
		super(menu, inventory, titleComponent);
		this.passEvents = false;
		this.imageWidth = 216;
		this.imageHeight = 220;
		this.inventoryLabelX += 22;
		this.inventoryLabelY = 127;
	}

	@Override
	protected boolean isSlotActive(Slot slot) {
		if (slot instanceof MoveableSlot moveableSlot) {
			int y = moveableSlot.getY(this.scrollOffset);
			return slot.isActive() && 174 >= slot.x && slot.x >= 30 && 108 >= y && y >= 18;
		}
		return slot.isActive();
	}

	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		this.renderBackground(stack);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);	
		this.blit(stack, this.leftPos, this.topPos, 0, 0, 238, 220);
		int scrollPosition = this.topPos + 18 + this.scrollOffset;
		this.blit(stack, this.leftPos + 198, scrollPosition, 244, 0, 12, 15);
	}

	@Override
	protected int getScrollbarWidth() {
		return 14;
	}

	@Override
	protected int getScrollbarHeight() {
		return 108;
	}

	@Override
	protected boolean isInScrollbar(double mouseX, double mouseY) {
		double topX = this.leftPos + 198.0;
		double topY = this.topPos + 16.0;
		if (topX + this.getScrollbarWidth() >= mouseX && mouseX >= topX && topY + this.getScrollbarHeight() >= mouseY && mouseY >= topY) {
			return true;
		}
		return false;
	}
	
	@Override
	protected int clampMouseMove(double mouseY) {
		return Mth.clamp((int) (mouseY - this.topPos - 25.5), 0, this.getScrollbarHeight() - 17);
	}
	
	@Override
	protected int clampMouseScroll(double delta) {
		return Mth.clamp((int) (this.scrollOffset - delta), 0, this.getScrollbarHeight() - 17);
	}
	
}
