package net.luis.xbackpack.client.gui.screens;

import static net.luis.xbackpack.world.extension.BackpackExtension.*;

import java.util.List;
import java.util.stream.Collectors;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.network.XBackpackNetworkHandler;
import net.luis.xbackpack.network.packet.UpdateBackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtension;
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

	private static final ResourceLocation BACKPACK = new ResourceLocation(XBackpack.MOD_ID, "textures/gui/container/backpack.png");
	private static final ResourceLocation ICONS = new ResourceLocation(XBackpack.MOD_ID, "textures/gui/container/backpack_icons.png");
	
	private final List<BackpackExtension> extensions = List.of(CRAFTING_TABLE.get(), FURNACE.get(), ANVIL.get(), ENCHANTING_TABLE.get(), STONECUTTER.get(), BREWING_STAND.get(), GRINDSTONE.get(), SMITHING_TABLE.get());
	private BackpackExtension extension = BackpackExtension.NO.get();
	
	public BackpackScreen(BackpackMenu menu, Inventory inventory, Component titleComponent) {
		super(menu, inventory, titleComponent);
		this.passEvents = false;
		this.imageWidth = 220;
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
		this.renderOpenedExtension(stack, mouseX, mouseY);
		RenderSystem.setShaderTexture(0, BACKPACK);	
		this.blit(stack, this.leftPos, this.topPos, 0, 0, 238, 220);
		int scrollPosition = this.topPos + 18 + this.scrollOffset;
		this.blit(stack, this.leftPos + 198, scrollPosition, 244, 0, 12, 15);
		this.renderExtensions(stack, mouseX, mouseY);
	}
	
	private int getExtensionOffset(BackpackExtension extension) {
		int offset = 3;
		for (BackpackExtension backpackExtension : this.extensions) {
			if (backpackExtension == extension) {
				break;
			}
			offset += extension.iconHeight() + 2;
		}
		return offset;
	}
	
	private void renderOpenedExtension(PoseStack stack, int mouseX, int mouseY) {
		if (this.extension != BackpackExtension.NO.get()) {
			int offset = this.getExtensionOffset(extension);
			ResourceLocation location = BackpackExtension.REGISTRY.get().getKey(this.extension);
			RenderSystem.setShaderTexture(0, new ResourceLocation(location.getNamespace(), "textures/gui/container/" + location.getPath() + "_extension.png"));
			this.blit(stack, this.leftPos + this.imageWidth - 3, this.topPos + offset, 0, 0, this.extension.imageWidth(), this.extension.imageHeight());
			this.itemRenderer.renderAndDecorateItem(extension.icon(), this.leftPos + this.imageWidth + 1, this.topPos + 4 + offset);
			this.font.draw(stack, this.extension.title(), this.leftPos + this.imageWidth + 19, this.topPos + 9 + offset, 4210752);
		}
	}
	
	private boolean isExtensionRenderable(BackpackExtension extension) {
		if (this.extension == BackpackExtension.NO.get()) {
			return true;
		} else if (this.extensions.indexOf(this.extension) > this.extensions.indexOf(extension)) {
			return true;
		} else if (this.getExtensionOffset(extension) > this.getExtensionOffset(this.extension) + this.extension.imageHeight()) {
			return true;
		}
		return false;
	}
	
	private void renderExtensions(PoseStack stack, int mouseX, int mouseY) {
		for (BackpackExtension extension : this.extensions.stream().filter(this::isExtensionRenderable).collect(Collectors.toList())) {
			int offset = this.getExtensionOffset(extension);
			RenderSystem.setShaderTexture(0, ICONS);
			blit(stack, this.leftPos + this.imageWidth, this.topPos + offset, extension.iconWidth(), extension.iconHeight(), 0, 0, 32, 32, 256, 256);
			this.itemRenderer.renderAndDecorateItem(extension.icon(), this.leftPos + this.imageWidth + 1, this.topPos + 3 + offset);
			if (this.isInExtension(extension, mouseX, mouseY)) {
				this.renderTooltip(stack, extension.tooltip(), mouseX, mouseY);
			}
		}
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
	
	private boolean isInExtension(BackpackExtension extension, double mouseX, double mouseY) {
		if (this.extension == extension || this.isExtensionRenderable(extension)) {
			double topX = this.leftPos + this.imageWidth;
			double topY = this.topPos + this.getExtensionOffset(extension);
			if (topX + extension.iconWidth() >= mouseX && mouseX >= topX && topY + extension.iconHeight() >= mouseY && mouseY >= topY) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (BackpackExtension extension : this.extensions) {
			if (this.isInExtension(extension, mouseX, mouseY)) {
				this.updateExtension(extension);
				break;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	private void updateExtension(BackpackExtension extension) {
		if (this.extension == extension || extension == null) {
			this.extension = BackpackExtension.NO.get();
		} else {
			this.extension = extension;
		}
		XBackpackNetworkHandler.getChannel().sendToServer(new UpdateBackpackExtension(this.extension));
	}
	
}
