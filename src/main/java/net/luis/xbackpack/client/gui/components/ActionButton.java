package net.luis.xbackpack.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.luis.xbackpack.XBackpack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 *
 * @author Luis-st
 *
 */

public class ActionButton extends AbstractButton {
	
	private static final ResourceLocation BACKPACK = new ResourceLocation(XBackpack.MOD_ID, "textures/gui/container/backpack.png");
	
	private final Consumer<ClickType> action;
	
	public ActionButton(int x, int y, int width, int height, Consumer<ClickType> action) {
		super(x, y, width, height, Component.empty());
		this.action = action;
	}
	
	public boolean isHovered() {
		return this.isHovered;
	}
	
	@Override
	public void onPress() {
		
	}
	
	@Override
	public void renderButton(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKPACK);
		if (this.isHoveredOrFocused()) {
			GuiComponent.blit(stack, this.getX(), this.getY(), this.width, this.height, 244, 27, 12, 12, 256, 256);
		} else {
			GuiComponent.blit(stack, this.getX(), this.getY(), this.width, this.height, 244, 15, 12, 12, 256, 256);
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.active && this.visible && this.clicked(mouseX, mouseY)) {
			if (button == 0) {
				this.playDownSound(Minecraft.getInstance().getSoundManager());
				this.action.accept(ClickType.LEFT);
				return true;
			} else if (button == 1) {
				this.playDownSound(Minecraft.getInstance().getSoundManager());
				this.action.accept(ClickType.RIGHT);
			}
		}
		return false;
	}
	
	@Override
	protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationOutput) {
	
	}
	
	@Override
	protected void defaultButtonNarrationText(@NotNull NarrationElementOutput narrationOutput) {
		
	}
	
	public enum ClickType {
		
		RIGHT(),
		LEFT()
		
	}
	
}
