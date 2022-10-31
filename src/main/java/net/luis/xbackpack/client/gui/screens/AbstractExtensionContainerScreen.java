package net.luis.xbackpack.client.gui.screens;

import static net.luis.xbackpack.world.extension.BackpackExtensions.ANVIL;
import static net.luis.xbackpack.world.extension.BackpackExtensions.BREWING_STAND;
import static net.luis.xbackpack.world.extension.BackpackExtensions.CRAFTING_TABLE;
import static net.luis.xbackpack.world.extension.BackpackExtensions.ENCHANTMENT_TABLE;
import static net.luis.xbackpack.world.extension.BackpackExtensions.FURNACE;
import static net.luis.xbackpack.world.extension.BackpackExtensions.GRINDSTONE;
import static net.luis.xbackpack.world.extension.BackpackExtensions.NO;
import static net.luis.xbackpack.world.extension.BackpackExtensions.SMITHING_TABLE;
import static net.luis.xbackpack.world.extension.BackpackExtensions.STONECUTTER;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.luis.xbackpack.client.gui.screens.extension.AbstractExtensionScreen;
import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.network.packet.extension.UpdateBackpackExtension;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensionState;
import net.luis.xbackpack.world.inventory.AbstractExtensionContainerMenu;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;

/**
 *
 * @author Luis-st
 *
 */

public abstract class AbstractExtensionContainerScreen<T extends AbstractExtensionContainerMenu> extends AbstractScrollableContainerScreen<T> {
	
	private final List<BackpackExtension> extensions = List.of(CRAFTING_TABLE.get(), FURNACE.get(), ANVIL.get(), ENCHANTMENT_TABLE.get(), STONECUTTER.get(), BREWING_STAND.get(), GRINDSTONE.get(), SMITHING_TABLE.get());
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
		this.extensionScreens.forEach((extensionScreen) -> {
			extensionScreen.init(this.minecraft, this.itemRenderer, this.font, this.imageWidth, this.imageHeight, this.leftPos, this.topPos);
		});
	}
	
	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		super.render(stack, mouseX, mouseY, partialTicks);
		this.renderScreen(stack, mouseX, mouseY, partialTicks);
		this.renderTooltip(stack, mouseX, mouseY);
	}
	
	protected abstract void renderScreen(PoseStack stack, int mouseX, int mouseY, float partialTicks);
	
	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		this.renderBackground(stack);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		this.renderExtensions(stack, partialTicks, mouseX, mouseY);
	}
	
	private void renderExtensions(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		for (BackpackExtension extension : this.extensions) {
			AbstractExtensionScreen extensionScreen = this.getExtensionScreen(extension);
			if (extensionScreen != null) {
				if (this.extension == extension && this.extension != NO.get()) {
					extensionScreen.renderOpened(stack, partialTicks, mouseX, mouseY);
				} else if (this.isExtensionRenderable(extension)) {
					extensionScreen.render(stack, partialTicks, mouseX, mouseY);
				}
			}
		}
	}
	
	@Override
	protected void renderTooltip(PoseStack stack, int mouseX, int mouseY) {
		super.renderTooltip(stack, mouseX, mouseY);
		for (BackpackExtension extension : this.extensions) {
			AbstractExtensionScreen extensionScreen = this.getExtensionScreen(extension);
			if (extensionScreen != null && this.canUseExtension(extension)) {
				extensionScreen.renderTooltip(stack, mouseX, mouseY, this.extension == extension && this.extension != NO.get(), this.isExtensionRenderable(extension), (itemStack) -> {
					this.renderTooltip(stack, itemStack, mouseX, mouseY);
				});
			}
		}
	}
	
	protected boolean canUseExtension(BackpackExtension extension) {
		return BackpackProvider.get(this.minecraft.player).getConfig().getExtensionConfig().getWithState(BackpackExtensionState.UNLOCKED).contains(extension);
	}
	
	protected boolean isExtensionRenderable(BackpackExtension extension) {
		if (!this.canUseExtension(extension)) {
			return false;
		} else if (this.extension == NO.get()) {
			return true;
		} else if (this.extensions.indexOf(this.extension) > this.extensions.indexOf(extension)) {
			return true;
		} else if (this.getExtensionOffset(extension) > this.getExtensionOffset(this.extension) + this.extension.getImageHeight()) {
			return true;
		}
		return false;
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
			if (topX + extension.getIconWidth() >= mouseX && mouseX >= topX && topY + extension.getIconHeight() >= mouseY && mouseY >= topY) {
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
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		AbstractExtensionScreen extensionScreen = this.getExtensionScreen(this.extension);
		if (extensionScreen != null && extensionScreen.mouseScrolled(mouseX, mouseY, delta)) {
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, delta);
	}
	
	@Override
	protected boolean hasClickedOutside(double mouseX, double mouseY, int leftPos, int topPos, int button) {
		int buttonOffset = 21;
		if (this.extensions.stream().filter(this::canUseExtension).collect(Collectors.toList()).isEmpty()) {
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
			} else if (mouseY > topPos + extensionOffset + this.extension.getImageHeight()) {
				return true;
			}
		}
		return false;
	}
	
	@NotNull
	public List<AbstractExtensionScreen> getExtensionScreens() {
		return ImmutableList.copyOf(this.extensionScreens);
	}
	
	protected void addExtensionScreen(BiFunction<AbstractExtensionContainerScreen<T>, List<BackpackExtension>, AbstractExtensionScreen> screenFactory) {
		this.extensionScreens.add(screenFactory.apply(this, this.extensions));
	}
	
	@Nullable
	public AbstractExtensionScreen getExtensionScreen(BackpackExtension extension) {
		return this.extensionScreens.stream().filter((extensionScreen) -> {
			return extensionScreen.getExtension() == extension;
		}).findAny().orElse(null);
	}
	
	@NotNull
	public Optional<AbstractExtensionScreen> getExtensionMenu() {
		return Optional.ofNullable(this.getExtensionScreen(this.getExtension()));
	}
	
	private void updateExtension(BackpackExtension extension) {
		this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		if (this.extension == extension || extension == null) {
			this.extension = NO.get();
		} else {
			this.extension = extension;
		}
		XBNetworkHandler.sendToServer(new UpdateBackpackExtension(this.extension));
	}
	
}