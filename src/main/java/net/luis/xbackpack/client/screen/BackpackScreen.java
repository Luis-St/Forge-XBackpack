package net.luis.xbackpack.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.common.inventory.menu.BackpackMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BackpackScreen extends AbstractContainerScreen<BackpackMenu> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(XBackpack.MOD_ID, "textures/gui/container/backpack.png");
	
	public BackpackScreen(BackpackMenu backpackMenu, Inventory inventory, Component component) {
		super(backpackMenu, inventory, component);
		this.passEvents = false;
		this.titleLabelX -= 22;
		this.imageHeight = 144 + 4 * 18;
		this.inventoryLabelY = this.imageHeight - 125;
	}

	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int x, int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int width = (this.width - (this.imageWidth + 44)) / 2;
		int height = (this.height - this.imageHeight) / 2;
		this.blit(stack, width, height, 0, 0, 220, 184);
		this.blit(stack, width, height + 89, 0, 89, 198, 184);
	}

}