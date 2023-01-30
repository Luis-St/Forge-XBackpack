package net.luis.xbackpack.client.gui.screens.extension;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.client.gui.screens.AbstractExtensionContainerScreen;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensionState;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 * @author Luis-st
 *
 */

public abstract class AbstractExtensionScreen {
	
	protected static final ResourceLocation ICONS = new ResourceLocation(XBackpack.MOD_ID, "textures/gui/container/backpack_icons.png");
	
	protected final AbstractExtensionContainerScreen<?> screen;
	protected final BackpackExtension extension;
	protected final List<BackpackExtension> extensions;
	protected Minecraft minecraft;
	protected ItemRenderer itemRenderer;
	protected Font font;
	protected int imageWidth;
	protected int imageHeight;
	protected int leftPos;
	protected int topPos;
	
	protected AbstractExtensionScreen(AbstractExtensionContainerScreen<?> screen, BackpackExtension extension, List<BackpackExtension> extensions) {
		this.screen = screen;
		this.extension = extension;
		this.extensions = extensions;
	}
	
	public final void init(Minecraft minecraft, ItemRenderer itemRenderer, Font font, int imageWidth, int imageHeight, int leftPos, int topPos) {
		this.minecraft = minecraft;
		this.itemRenderer = itemRenderer;
		this.font = font;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.leftPos = leftPos;
		this.topPos = topPos;
		if (this.minecraft != null) {
			this.init();
		}
	}
	
	protected void init() {
		
	}
	
	protected boolean canUseExtension(BackpackExtension extension) {
		return BackpackProvider.get(Objects.requireNonNull(this.minecraft.player)).getConfig().getExtensionConfig().getWithState(BackpackExtensionState.UNLOCKED).contains(extension);
	}
	
	protected int getExtensionOffset(BackpackExtension extension) {
		int offset = 3;
		for (BackpackExtension backpackExtension : this.extensions) {
			if (backpackExtension == extension) {
				break;
			}
			offset += extension.getIconHeight() + 2;
		}
		return offset;
	}
	
	protected boolean isExtensionRenderable(BackpackExtension extension) {
		if (this.extension == BackpackExtensions.NO.get()) {
			return true;
		} else if (this.extensions.indexOf(this.extension) > this.extensions.indexOf(extension)) {
			return true;
		} else {
			return this.getExtensionOffset(extension) > this.getExtensionOffset(this.extension) + this.extension.getImageHeight();
		}
	}
	
	protected boolean isInExtension(BackpackExtension extension, double mouseX, double mouseY) {
		if (this.extension == extension || this.isExtensionRenderable(extension)) {
			double topX = this.leftPos + this.imageWidth;
			double topY = this.topPos + this.getExtensionOffset(extension);
			return topX + extension.getIconWidth() >= mouseX && mouseX >= topX && topY + extension.getIconHeight() >= mouseY && mouseY >= topY;
		}
		return false;
	}
	
	protected ResourceLocation getTexture() {
		ResourceLocation location = Objects.requireNonNull(BackpackExtensions.REGISTRY.get().getKey(this.extension));
		return new ResourceLocation(location.getNamespace(), "textures/gui/container/" + location.getPath() + "_extension.png");
	}
	
	public void render(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		int offset = this.getExtensionOffset(this.extension);
		RenderSystem.setShaderTexture(0, ICONS);
		GuiComponent.blit(stack, this.leftPos + this.imageWidth, this.topPos + offset, this.extension.getIconWidth(), this.extension.getIconHeight(), 0, 0, 32, 32, 256, 256);
		this.itemRenderer.renderAndDecorateItem(this.extension.getIcon(), this.leftPos + this.imageWidth + 1, this.topPos + 3 + offset);
	}
	
	public void renderOpened(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		int offset = this.getExtensionOffset(this.extension);
		RenderSystem.setShaderTexture(0, this.getTexture());
		this.screen.blit(stack, this.leftPos + this.imageWidth - 3, this.topPos + offset, 0, 0, this.extension.getImageWidth(), this.extension.getImageHeight());
		this.itemRenderer.renderAndDecorateItem(this.extension.getIcon(), this.leftPos + this.imageWidth + 1, this.topPos + 4 + offset);
		this.font.draw(stack, this.extension.getTitle(), this.leftPos + this.imageWidth + 19, this.topPos + 9 + offset, 4210752);
		if (this.minecraft != null) {
			this.renderAdditional(stack, partialTicks, mouseX, mouseY, true);
		}
	}
	
	protected void renderAdditional(PoseStack stack, float partialTicks, int mouseX, int mouseY, boolean open) {
		
	}
	
	public void renderTooltip(PoseStack stack, int mouseX, int mouseY, boolean open, boolean renderable, Consumer<ItemStack> tooltipRenderer) {
		if (this.isInExtension(this.extension, mouseX, mouseY) && !open && renderable) {
			this.screen.renderTooltip(stack, this.extension.getTooltip(), mouseX, mouseY);
		}
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return false;
	}
	
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return false;
	}
	
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		return false;
	}
	
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		return false;
	}
	
	public BackpackExtension getExtension() {
		return this.extension;
	}
	
}
