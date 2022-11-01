package net.luis.xbackpack.client.gui.screens;

import org.jetbrains.annotations.NotNull;

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
	
	@Override
	protected abstract void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY);
	
	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBg(stack, partialTicks, mouseX, mouseY);
		MinecraftForge.EVENT_BUS.post(new ContainerScreenEvent.Render.Background(this, stack, mouseX, mouseY));
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
		this.renderSlots(stack, mouseX, mouseY);
		this.renderLabels(stack, mouseX, mouseY);
		MinecraftForge.EVENT_BUS.post(new ContainerScreenEvent.Render.Foreground(this, stack, mouseX, mouseY));
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
	
	@NotNull
	protected SlotRenderType getSlotRenderType(Slot slot) {
		return SlotRenderType.DEFAULT;
	}
	
	protected void renderSlots(PoseStack stack, int mouseX, int mouseY) {
		for (int i = 0; i < this.menu.slots.size(); ++i) {
			Slot slot = this.menu.slots.get(i);
			if (this.getSlotRenderType(slot) == SlotRenderType.SKIP) {
				continue;
			} else {
				int y = slot instanceof MoveableSlot moveableSlot ? moveableSlot.getY(this.scrollOffset) : slot.y;
				this.renderAndDecorateSlot(stack, mouseX, mouseY, slot, slot.x, y, this.getSlotColor(i));
			}
		}
	}
	
	protected void renderAndDecorateSlot(PoseStack stack, int mouseX, int mouseY, Slot slot, int slotX, int slotY, int slotColor) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		if (slot.x == slotX && slot.y == slotY) {
			super.renderSlot(stack, slot);
		} else {
			this.renderSlot(stack, slot, slotX, slotY);
		}
		if (this.isHovering(slot, mouseX, mouseY)) {
			this.hoveredSlot = slot;
			renderSlotHighlight(stack, slotX, slotY, this.getBlitOffset(), slotColor);
		}
	}
	
	protected void renderSlot(PoseStack stack, Slot slot, int slotX, int slotY) {
		ItemStack slotStack = slot.getItem();
		boolean quickReplace = false;
		boolean clickedSlot = slot == this.clickedSlot && !this.draggingItem.isEmpty() && !this.isSplittingStack;
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
				AbstractContainerMenu.getQuickCraftSlotCount(this.quickCraftSlots, this.quickCraftingType, slotStack, slotStack.isEmpty() ? 0 : slotStack.getCount());
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
		if (slotStack.isEmpty()) {
			Pair<ResourceLocation, ResourceLocation> pair = slot.getNoItemIcon();
			if (pair != null) {
				TextureAtlasSprite atlasSprite = this.minecraft.getTextureAtlas(pair.getFirst()).apply(pair.getSecond());
				RenderSystem.setShaderTexture(0, atlasSprite.atlas().location());
				blit(stack, slotX, slotY, this.getBlitOffset(), 16, 16, atlasSprite);
				clickedSlot = true;
			}
		}
		if (!clickedSlot) {
			if (quickReplace) {
				fill(stack, slotX, slotY, slotX + 16, slotY + 16, -2130706433);
			}
			RenderSystem.enableDepthTest();
			int modelOffset = slotX + slotY * this.imageWidth;
			this.itemRenderer.renderAndDecorateItem(this.minecraft.player, slotStack, slotX, slotY, modelOffset);
			this.itemRenderer.renderGuiItemDecorations(this.font, slotStack, slotX, slotY, stackCount);
		}
		this.itemRenderer.blitOffset = 0.0F;
		this.setBlitOffset(0);
	}
	
	@Override
	protected void renderTooltip(PoseStack stack, int mouseX, int mouseY) {
		if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem() && this.getSlotRenderType(this.hoveredSlot) == SlotRenderType.DEFAULT) {
			this.renderTooltip(stack, this.hoveredSlot.getItem(), mouseX, mouseY);
		}
	}
	
	@Override
	public Slot findSlot(double mouseX, double mouseY) {
		for (int i = 0; i < this.menu.slots.size(); ++i) {
			Slot slot = this.menu.slots.get(i);
			if (this.isHovering(slot, mouseX, mouseY) && this.getSlotRenderType(slot) == SlotRenderType.DEFAULT) {
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
		return this.isHovering(slot.x, slot instanceof MoveableSlot moveableSlot ? moveableSlot.getY(this.scrollOffset) : slot.y, 16, 16, mouseX, mouseY);
	}
	
}
