package net.luis.xbackpack.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;

import net.luis.xbackpack.world.inventory.slot.MoveableSlot;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * 
 * @author Luis-st
 * 
 */

public abstract class AbstractScrollableContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
	
	private boolean scrolling = false;
	protected int scrollOffset = 0;
	
	protected AbstractScrollableContainerScreen(T menu, Inventory inventory, Component titleComponent) {
		super(menu, inventory, titleComponent);
	}
	
	protected abstract boolean isSlotActive(Slot slot);
	
	@Override
	protected abstract void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY);
	
	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBg(stack, partialTicks, mouseX, mouseY);
		MinecraftForge.EVENT_BUS.post(new ContainerScreenEvent.DrawBackground(this, stack, mouseX, mouseY));
		RenderSystem.disableDepthTest();
		for (Widget widget : this.renderables) {
			widget.render(stack, mouseX, mouseY, partialTicks);
		}
		PoseStack viewStack = RenderSystem.getModelViewStack();
		viewStack.pushPose();
		viewStack.translate(this.leftPos, this.topPos, 0.0D);
		RenderSystem.applyModelViewMatrix();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		this.hoveredSlot = null;
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		for (int i = 0; i < this.menu.slots.size(); ++i) {
			Slot slot = this.menu.slots.get(i);
			if (this.isSlotActive(slot)) {
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				this.renderSlot(stack, slot);
			}
			if (this.isHovering(slot, mouseX, mouseY) && this.isSlotActive(slot)) {
				this.hoveredSlot = slot;
				if (slot instanceof MoveableSlot moveableSlot) {
					renderSlotHighlight(stack, slot.x, moveableSlot.getY(this.scrollOffset), this.getBlitOffset(), this.getSlotColor(i));
				} else {
					renderSlotHighlight(stack, slot.x, slot.y, this.getBlitOffset(), this.getSlotColor(i));
				}
			}
		}
		this.renderLabels(stack, mouseX, mouseY);
		MinecraftForge.EVENT_BUS.post(new ContainerScreenEvent.DrawForeground(this, stack, mouseX, mouseY));
		ItemStack mouseStack = this.draggingItem.isEmpty() ? this.menu.getCarried() : this.draggingItem;
		if (!mouseStack.isEmpty()) {
			int renderOffset = this.draggingItem.isEmpty() ? 8 : 16;
			String count = null;
			if (!this.draggingItem.isEmpty() && this.isSplittingStack) {
				mouseStack = mouseStack.copy();
				mouseStack.setCount(Mth.ceil((float) mouseStack.getCount() / 2.0F));
			} else if (this.isQuickCrafting && this.quickCraftSlots.size() > 1) {
				mouseStack = mouseStack.copy();
				mouseStack.setCount(this.quickCraftingRemainder);
				if (mouseStack.isEmpty()) {
					count = ChatFormatting.YELLOW + "0";
				}
			}
			this.renderFloatingItem(mouseStack, mouseX - this.leftPos - 8, mouseY - this.topPos - renderOffset, count);
		}
		if (!this.snapbackItem.isEmpty()) {
			float time = (float) (Util.getMillis() - this.snapbackTime) / 100.0F;
			if (time >= 1.0F) {
				time = 1.0F;
				this.snapbackItem = ItemStack.EMPTY;
			}
			this.renderFloatingItem(this.snapbackItem, this.snapbackStartX + (int) ((this.snapbackEnd.x - this.snapbackStartX) * time), this.snapbackStartY + (int) ((this.snapbackEnd.y - this.snapbackStartY) * time), (String) null);
		}
		viewStack.popPose();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.enableDepthTest();
		this.renderTooltip(stack, mouseX, mouseY);
	}
	
	@Override
	public void renderSlot(PoseStack stack, Slot slot) {
		int x = slot.x;
		int y;
		if (slot instanceof MoveableSlot moveableSlot) {
			y = moveableSlot.getY(this.scrollOffset);
		} else {
			y = slot.y;
		}
		ItemStack slotStack = slot.getItem();
		boolean quickReplace = false;
		boolean shouldRender = slot == this.clickedSlot && !this.draggingItem.isEmpty() && !this.isSplittingStack;
		ItemStack carriedStack = this.menu.getCarried();
		String stackCount = null;
		if (slot == this.clickedSlot && !this.draggingItem.isEmpty() && this.isSplittingStack && !slotStack.isEmpty()) {
			slotStack = slotStack.copy();
			slotStack.setCount(slotStack.getCount() / 2);
		} else if (this.isQuickCrafting && this.quickCraftSlots.contains(slot) && !carriedStack.isEmpty()) {
			if (this.quickCraftSlots.size() == 1) {
				return;
			}
			if (AbstractContainerMenu.canItemQuickReplace(slot, carriedStack, true) && this.menu.canDragTo(slot)) {
				slotStack = carriedStack.copy();
				quickReplace = true;
				AbstractContainerMenu.getQuickCraftSlotCount(this.quickCraftSlots, this.quickCraftingType, slotStack, slot.getItem().isEmpty() ? 0 : slot.getItem().getCount());
				int count = Math.min(slotStack.getMaxStackSize(), slot.getMaxStackSize(slotStack));
				if (slotStack.getCount() > count) {
					stackCount = ChatFormatting.YELLOW.toString() + count;
					slotStack.setCount(count);
				}
			} else {
				this.quickCraftSlots.remove(slot);
				this.recalculateQuickCraftRemaining();
			}
		}
		this.setBlitOffset(100);
		this.itemRenderer.blitOffset = 100.0F;
		if (slotStack.isEmpty() && this.isSlotActive(slot)) {
			Pair<ResourceLocation, ResourceLocation> pair = slot.getNoItemIcon();
			if (pair != null) {
				TextureAtlasSprite textureatlassprite = this.minecraft.getTextureAtlas(pair.getFirst()).apply(pair.getSecond());
				RenderSystem.setShaderTexture(0, textureatlassprite.atlas().location());
				blit(stack, x, y, this.getBlitOffset(), 16, 16, textureatlassprite);
				shouldRender = true;
			}
		}
		if (!shouldRender) {
			if (quickReplace) {
				fill(stack, x, y, x + 16, y + 16, -2130706433);
			}
			RenderSystem.enableDepthTest();
			int modelOffset;
			if (slot instanceof MoveableSlot moveableSlot) {
				modelOffset = slot.x + moveableSlot.getY(this.scrollOffset) * this.imageWidth;
			} else {
				modelOffset = slot.x + slot.y * this.imageWidth;
			}
			this.itemRenderer.renderAndDecorateItem(this.minecraft.player, slotStack, x, y, modelOffset);
			this.itemRenderer.renderGuiItemDecorations(this.font, slotStack, x, y, stackCount);
		}
		this.itemRenderer.blitOffset = 0.0F;
		this.setBlitOffset(0);
	}
	
	@Override
	public Slot findSlot(double mouseX, double mouseY) {
		for (int i = 0; i < this.menu.slots.size(); ++i) {
			Slot slot = this.menu.slots.get(i);
			if (this.isHovering(slot, mouseX, mouseY) && this.isSlotActive(slot)) {
				return slot;
			}
		}
		return null;
	}
	
	protected abstract int getScrollbarWidth();
	
	protected abstract int getScrollbarHeight();
	
	protected abstract boolean isInScrollbar(double mouseX, double mouseY);
	
	protected abstract int clampMouseMove(double mouseY);
	
	protected abstract int clampMouseScroll(double delta);
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0 && this.isInScrollbar(mouseX, mouseY)) {
			this.scrolling = true;
			this.scrollOffset = this.clampMouseMove(mouseY);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		this.scrolling = false;
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (button == 0 && this.scrolling) {
			this.scrollOffset = this.clampMouseMove(mouseY);
			return true;
		}
		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		this.scrollOffset = this.clampMouseScroll(delta);
		return true;
	}
	
	@Override
	public boolean isHovering(Slot slot, double mouseX, double mouseY) {
		if (slot instanceof MoveableSlot moveableSlot) {
			return this.isHovering(slot.x, moveableSlot.getY(this.scrollOffset), 16, 16, mouseX, mouseY);
		} else {
			return this.isHovering(slot.x, slot.y, 16, 16, mouseX, mouseY);
		}
	}
	
}
