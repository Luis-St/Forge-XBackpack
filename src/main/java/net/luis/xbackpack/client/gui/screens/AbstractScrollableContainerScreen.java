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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.luis.xbackpack.world.inventory.slot.MoveableSlot;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ForgeEventFactoryClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 *
 * @author Luis-St
 *
 */

public abstract class AbstractScrollableContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
	
	private boolean scrolling = false;
	protected int scrollOffset = 0;
	
	protected AbstractScrollableContainerScreen(@NotNull T menu, @NotNull Inventory inventory, @NotNull Component titleComponent) {
		super(menu, inventory, titleComponent);
	}
	
	@Override
	protected abstract void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY);
	
	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		//region Avoid super call
		this.renderBackground(graphics, mouseX, mouseY, partialTicks);
		for (Renderable widget : this.renderables) {
			widget.render(graphics, mouseX, mouseY, partialTicks);
		}
		ForgeEventFactoryClient.onContainerRenderBackground(this, graphics, mouseX, mouseY);
		//endregion
		RenderSystem.disableDepthTest();
		graphics.pose().pushPose();
		graphics.pose().translate(this.leftPos, this.topPos, 0.0F);
		this.hoveredSlot = null;
		
		this.renderSlots(graphics, mouseX, mouseY);
		this.renderLabels(graphics, mouseX, mouseY);
		ForgeEventFactoryClient.onContainerRenderForeground(this, graphics, mouseX, mouseY);
		ItemStack mouseStack = this.draggingItem.isEmpty() ? this.menu.getCarried() : this.draggingItem;
		if (!mouseStack.isEmpty()) {
			int renderOffset = this.draggingItem.isEmpty() ? 8 : 16;
			String count = null;
			if (!this.draggingItem.isEmpty() && this.isSplittingStack) {
				mouseStack = mouseStack.copyWithCount(Mth.ceil(mouseStack.getCount() / 2.0F));
			} else if (this.isQuickCrafting && this.quickCraftSlots.size() > 1) {
				mouseStack = mouseStack.copyWithCount(this.quickCraftingRemainder);
				if (mouseStack.isEmpty()) {
					count = ChatFormatting.YELLOW + "0";
				}
			}
			this.renderFloatingItem(graphics, mouseStack, mouseX - this.leftPos - 8, mouseY - this.topPos - renderOffset, count);
		}
		if (!this.snapbackItem.isEmpty()) {
			float f = (float) (Util.getMillis() - this.snapbackTime) / 100.0F;
			if (f >= 1.0F) {
				f = 1.0F;
				this.snapbackItem = ItemStack.EMPTY;
			}
			int x = this.snapbackStartX + (int) ((float) this.snapbackEnd.x - this.snapbackStartX * f);
			int y = this.snapbackStartY + (int) ((float) this.snapbackEnd.y - this.snapbackStartY * f);
			this.renderFloatingItem(graphics, this.snapbackItem, x, y, null);
		}
		
		graphics.pose().popPose();
		RenderSystem.enableDepthTest();
		this.renderTooltip(graphics, mouseX, mouseY);
	}
	
	protected @NotNull SlotRenderType getSlotRenderType(@NotNull Slot slot) {
		return SlotRenderType.DEFAULT;
	}
	
	protected void renderSlots(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
		/* Vanilla code
		for (int k = 0; k < this.menu.slots.size(); ++k) {
			Slot slot = this.menu.slots.get(k);
			if (slot.isActive()) {
				this.renderSlot(graphics, slot);
			}
			if (this.isHovering(slot, mouseX, mouseY) && slot.isActive()) {
				this.hoveredSlot = slot;
				if (this.hoveredSlot.isHighlightable()) {
					renderSlotHighlight(graphics, slot.x, slot.y, 0, this.getSlotColor(k));
				}
			}
		}
		*/
		for (int i = 0; i < this.menu.slots.size(); ++i) {
			Slot slot = this.menu.slots.get(i);
			if (this.getSlotRenderType(slot) != SlotRenderType.SKIP) {
				this.renderSlot(graphics, slot);
				if (this.isHovering(slot, mouseX, mouseY)) {
					this.hoveredSlot = slot;
					int y = slot instanceof MoveableSlot moveableSlot ? moveableSlot.getY(this.scrollOffset) : slot.y;
					renderSlotHighlight(graphics, slot.x, y, 0, this.getSlotColor(i));
				}
			}
		}
	}
	
	@Override
	protected void renderSlot(@NotNull GuiGraphics graphics, @NotNull Slot slot) {
		int y = slot instanceof MoveableSlot moveableSlot ? moveableSlot.getY(this.scrollOffset) : slot.y;
		ItemStack slotStack = slot.getItem();
		ItemStack carriedStack = this.menu.getCarried();
		boolean quickReplace = false;
		boolean clickedSlot = slot == this.clickedSlot && !this.draggingItem.isEmpty() && !this.isSplittingStack;
		String stackCount = null;
		if (slot == this.clickedSlot && !this.draggingItem.isEmpty() && this.isSplittingStack && !slotStack.isEmpty()) {
			slotStack = slotStack.copyWithCount(slotStack.getCount() / 2);
		} else if (this.isQuickCrafting && this.quickCraftSlots.contains(slot) && !carriedStack.isEmpty()) {
			if (this.quickCraftSlots.size() == 1) {
				return;
			}
			if (AbstractContainerMenu.canItemQuickReplace(slot, carriedStack, true) && this.menu.canDragTo(slot)) {
				quickReplace = true;
				int count = Math.min(carriedStack.getMaxStackSize(), slot.getMaxStackSize(carriedStack));
				int craftPlaceCount = AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, this.quickCraftingType, carriedStack) + (slot.getItem().isEmpty() ? 0 : slot.getItem().getCount());
				if (craftPlaceCount > count) {
					craftPlaceCount = count;
					stackCount = ChatFormatting.YELLOW.toString() + count;
				}
				slotStack = carriedStack.copyWithCount(craftPlaceCount);
			} else {
				this.quickCraftSlots.remove(slot);
				this.recalculateQuickCraftRemaining();
			}
		}
		graphics.pose().pushPose();
		graphics.pose().translate(0.0, 0.0, 100.0);
		if (slotStack.isEmpty() && slot.isActive()) {
			Pair<ResourceLocation, ResourceLocation> pair = slot.getNoItemIcon();
			if (pair != null) {
				TextureAtlasSprite atlasSprite = Objects.requireNonNull(this.minecraft).getTextureAtlas(pair.getFirst()).apply(pair.getSecond());
				graphics.blit(slot.x, y, 0, 16, 16, atlasSprite);
				clickedSlot = true;
			}
		}
		if (!clickedSlot) {
			if (quickReplace) {
				graphics.fill(slot.x, y, slot.x + 16, y + 16, -2130706433);
			}
			int modelOffset = slot.x + slot.y * this.imageWidth;
			if (slot.isFake()) {
				graphics.renderFakeItem(slotStack, slot.x, y, modelOffset);
			} else {
				graphics.renderItem(slotStack, slot.x, y, modelOffset);
			}
			graphics.renderItemDecorations(this.font, slotStack, slot.x, y, stackCount);
		}
		graphics.pose().popPose();
	}
	
	@Override
	protected void renderTooltip(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
		if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem() && this.getSlotRenderType(this.hoveredSlot) == SlotRenderType.DEFAULT) {
			graphics.renderTooltip(this.font, this.hoveredSlot.getItem(), mouseX, mouseY);
		}
	}
	
	@Override
	public @Nullable Slot findSlot(double mouseX, double mouseY) {
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
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
		this.scrollOffset = this.clampMouseScroll(deltaY);
		return true;
	}
	
	@Override
	public boolean isHovering(@NotNull Slot slot, double mouseX, double mouseY) {
		return this.isHovering(slot.x, slot instanceof MoveableSlot moveableSlot ? moveableSlot.getY(this.scrollOffset) : slot.y, 16, 16, mouseX, mouseY);
	}
}
