/*
 * XBackpack
 * Copyright (C) 2024 Luis Staudt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.luis.xbackpack.client.gui.screens;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.luis.xbackpack.client.gui.screens.extension.AbstractExtensionScreen;
import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.network.packet.extension.UpdateExtensionPacket;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensionState;
import net.luis.xbackpack.world.inventory.AbstractExtensionContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static net.luis.xbackpack.world.extension.BackpackExtensions.*;

/**
 *
 * @author Luis-St
 *
 */

public abstract class AbstractExtensionContainerScreen<T extends AbstractExtensionContainerMenu> extends AbstractScrollableContainerScreen<T> {
	
	private final List<BackpackExtension> extensions = Lists.newArrayList(REGISTRY.get()).stream().filter(((Predicate<BackpackExtension>) BackpackExtension::isDisabled).negate()).toList();
	private final List<AbstractExtensionScreen> extensionScreens = Lists.newArrayList();
	private BackpackExtension extension = NO.get();
	
	protected AbstractExtensionContainerScreen(T menu, Inventory inventory, Component titleComponent) {
		super(menu, inventory, titleComponent);
	}
	
	@NotNull
	public BackpackExtension getExtension() {
		return this.extension == null ? NO.get() : this.extension;
	}
	
	@Override
	protected void init() {
		super.init();
		this.extensionScreens.forEach((extensionScreen) -> extensionScreen.init(this.minecraft, this.font, this.imageWidth, this.imageHeight, this.leftPos, this.topPos));
	}
	
	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		super.render(graphics, mouseX, mouseY, partialTicks);
		this.renderScreen(graphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(graphics, mouseX, mouseY);
	}
	
	protected abstract void renderScreen(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks);
	
	@Override
	protected void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		this.renderExtensions(graphics, partialTicks, mouseX, mouseY);
	}
	
	private void renderExtensions(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
		for (BackpackExtension extension : this.extensions) {
			AbstractExtensionScreen extensionScreen = this.getExtensionScreen(extension);
			if (extensionScreen != null) {
				if (this.extension == extension && this.extension != NO.get()) {
					extensionScreen.renderOpened(graphics, partialTicks, mouseX, mouseY);
				} else if (this.isExtensionRenderable(extension)) {
					extensionScreen.render(graphics, partialTicks, mouseX, mouseY);
				}
			}
		}
	}
	
	@Override
	protected void renderTooltip(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
		super.renderTooltip(graphics, mouseX, mouseY);
		for (BackpackExtension extension : this.extensions) {
			AbstractExtensionScreen extensionScreen = this.getExtensionScreen(extension);
			if (extensionScreen != null && this.canUseExtension(extension)) {
				extensionScreen.renderTooltip(graphics, mouseX, mouseY, this.extension == extension && this.extension != NO.get(), this.isExtensionRenderable(extension), (itemStack) -> graphics.renderTooltip(this.font, itemStack, mouseX, mouseY));
			}
		}
	}
	
	protected boolean canUseExtension(BackpackExtension extension) {
		return BackpackProvider.get(Objects.requireNonNull(Objects.requireNonNull(this.minecraft).player)).getConfig().getExtensionConfig().getWithState(BackpackExtensionState.UNLOCKED).contains(extension);
	}
	
	protected boolean isExtensionRenderable(BackpackExtension extension) {
		if (!this.canUseExtension(extension)) {
			return false;
		} else if (this.extension == NO.get()) {
			return true;
		} else if (this.extensions.indexOf(this.extension) > this.extensions.indexOf(extension)) {
			return true;
		} else {
			return this.getExtensionOffset(extension) > this.getExtensionOffset(this.extension) + this.extension.getImageHeight();
		}
	}
	
	public int getExtensionOffset(BackpackExtension extension) {
		int offset = 3;
		for (BackpackExtension backpackExtension : this.extensions) {
			if (backpackExtension == extension) {
				break;
			}
			offset += extension.getIconHeight() + 2;
		}
		return offset;
	}
	
	protected boolean isInExtension(BackpackExtension extension, double mouseX, double mouseY) {
		if (this.extension == extension || this.isExtensionRenderable(extension)) {
			double topX = this.leftPos + this.imageWidth;
			double topY = this.topPos + this.getExtensionOffset(extension);
			return topX + extension.getIconWidth() >= mouseX && mouseX >= topX && topY + extension.getIconHeight() >= mouseY && mouseY >= topY;
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
		AbstractExtensionScreen extensionScreen = this.getExtensionScreen(this.extension);
		if (extensionScreen != null && extensionScreen.mouseClicked(mouseX, mouseY, button)) {
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		AbstractExtensionScreen extensionScreen = this.getExtensionScreen(this.extension);
		if (extensionScreen != null && extensionScreen.mouseReleased(mouseX, mouseY, button)) {
			return true;
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		AbstractExtensionScreen extensionScreen = this.getExtensionScreen(this.extension);
		if (extensionScreen != null && extensionScreen.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
			return true;
		}
		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
		AbstractExtensionScreen extensionScreen = this.getExtensionScreen(this.extension);
		if (extensionScreen != null && extensionScreen.mouseScrolled(mouseX, mouseY, deltaY)) {
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
	}
	
	@Override
	protected boolean hasClickedOutside(double mouseX, double mouseY, int leftPos, int topPos, int button) {
		int buttonOffset = 21;
		if (this.extensions.stream().noneMatch(this::canUseExtension)) {
			return super.hasClickedOutside(mouseX, mouseY, leftPos, topPos, button);
		} else if (this.extension == NO.get()) {
			this.imageWidth += buttonOffset;
			boolean flag = super.hasClickedOutside(mouseX, mouseY, leftPos, topPos, button);
			this.imageWidth -= buttonOffset;
			return flag;
		} else if (leftPos > mouseX) {
			return true;
		} else if (topPos > mouseY) {
			return true;
		} else if (mouseY > topPos + this.imageHeight && leftPos + this.imageWidth > mouseX) {
			return true;
		} else if (mouseX > leftPos + this.imageWidth) {
			int extensionOffset = this.getExtensionOffset(this.extension);
			if (topPos + extensionOffset > mouseY) {
				return true;
			} else if (mouseX > leftPos + this.imageWidth + this.extension.getImageWidth()) {
				return true;
			} else {
				return mouseY > topPos + extensionOffset + this.extension.getImageHeight();
			}
		}
		return false;
	}
	
	protected void addExtensionScreen(@NotNull BiFunction<AbstractExtensionContainerScreen<T>, List<BackpackExtension>, AbstractExtensionScreen> screenFactory) {
		AbstractExtensionScreen extensionScreen = screenFactory.apply(this, this.extensions);
		if (!extensionScreen.getExtension().isDisabled()) {
			this.extensionScreens.add(extensionScreen);
		}
	}
	
	public AbstractExtensionScreen getExtensionScreen(BackpackExtension extension) {
		return this.extensionScreens.stream().filter((extensionScreen) -> extensionScreen.getExtension() == extension).findAny().orElse(null);
	}
	
	private void updateExtension(BackpackExtension extension) {
		Objects.requireNonNull(this.minecraft).getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		if (this.extension == extension || extension == null || extension.isDisabled()) {
			this.extension = NO.get();
		} else {
			this.extension = extension;
		}
		XBNetworkHandler.INSTANCE.sendToServer(new UpdateExtensionPacket(this.extension));
	}
}
