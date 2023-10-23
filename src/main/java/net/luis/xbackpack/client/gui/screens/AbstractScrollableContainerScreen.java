package net.luis.xbackpack.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.luis.xbackpack.world.inventory.slot.MoveableSlot;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
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
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 *
 * @author Luis-St
 *
 */

public abstract class AbstractScrollableContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
	
	private boolean scrolling = false;
	protected int scrollOffset = 0;
	
	protected AbstractScrollableContainerScreen(T menu, Inventory inventory, Component titleComponent) {
		super(menu, inventory, titleComponent);
	}
	
	@Override
	protected abstract void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY);
	
	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(graphics, mouseX, mouseY, partialTicks);
		MinecraftForge.EVENT_BUS.post(new ContainerScreenEvent.Render.Background(this, graphics, mouseX, mouseY));
		RenderSystem.disableDepthTest();
		for (Renderable widget : this.renderables) {
			widget.render(graphics, mouseX, mouseY, partialTicks);
		}
		PoseStack viewStack = RenderSystem.getModelViewStack();
		viewStack.pushPose();
		viewStack.translate(this.leftPos, this.topPos, 0.0D);
		RenderSystem.applyModelViewMatrix();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		this.hoveredSlot = null;
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		this.renderSlots(graphics, mouseX, mouseY);
		this.renderLabels(graphics, mouseX, mouseY);
		MinecraftForge.EVENT_BUS.post(new ContainerScreenEvent.Render.Foreground(this, graphics, mouseX, mouseY));
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
			this.renderFloatingItem(graphics, mouseStack, mouseX - this.leftPos - 8, mouseY - this.topPos - renderOffset, count);
		}
		if (!this.snapbackItem.isEmpty()) {
			float time = (float) (Util.getMillis() - this.snapbackTime) / 100.0F;
			if (time >= 1.0F) {
				time = 1.0F;
				this.snapbackItem = ItemStack.EMPTY;
			}
			this.renderFloatingItem(graphics, this.snapbackItem, this.snapbackStartX + (int) ((Objects.requireNonNull(this.snapbackEnd).x - this.snapbackStartX) * time),
				this.snapbackStartY + (int) ((this.snapbackEnd.y - this.snapbackStartY) * time), null);
		}
		viewStack.popPose();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.enableDepthTest();
		this.renderTooltip(graphics, mouseX, mouseY);
	}
	
	@NotNull
	protected SlotRenderType getSlotRenderType(Slot slot) {
		return SlotRenderType.DEFAULT;
	}
	
	protected void renderSlots(GuiGraphics graphics, int mouseX, int mouseY) {
		for (int i = 0; i < this.menu.slots.size(); ++i) {
			Slot slot = this.menu.slots.get(i);
			if (this.getSlotRenderType(slot) != SlotRenderType.SKIP) {
				int y = slot instanceof MoveableSlot moveableSlot ? moveableSlot.getY(this.scrollOffset) : slot.y;
				this.renderAndDecorateSlot(graphics, mouseX, mouseY, slot, slot.x, y, this.getSlotColor(i));
			}
		}
	}
	
	protected void renderAndDecorateSlot(GuiGraphics graphics, int mouseX, int mouseY, Slot slot, int slotX, int slotY, int slotColor) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		this.renderSlot(graphics, slot, slotX, slotY);
		if (this.isHovering(slot, mouseX, mouseY)) {
			this.hoveredSlot = slot;
			renderSlotHighlight(graphics, slotX, slotY, 0, slotColor);
		}
	}
	
	protected void renderSlot(GuiGraphics graphics, @NotNull Slot slot, int slotX, int slotY) {
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
		graphics.pose().translate(0.0F, 0.0F, 100.0F);
		if (slotStack.isEmpty()) {
			Pair<ResourceLocation, ResourceLocation> pair = slot.getNoItemIcon();
			if (pair != null) {
				TextureAtlasSprite atlasSprite = Objects.requireNonNull(this.minecraft).getTextureAtlas(pair.getFirst()).apply(pair.getSecond());
				RenderSystem.setShaderTexture(0, atlasSprite.atlasLocation());
				graphics.blit(slotX, slotY, 0, 16, 16, atlasSprite);
				clickedSlot = true;
			}
		}
		if (!clickedSlot) {
			if (quickReplace) {
				graphics.fill(slotX, slotY, slotX + 16, slotY + 16, -2130706433);
			}
			RenderSystem.enableDepthTest();
			int modelOffset = slotX + slotY * this.imageWidth;
			graphics.renderItem(Objects.requireNonNull(Objects.requireNonNull(this.minecraft).player), slotStack, slotX, slotY, modelOffset);
			graphics.renderItemDecorations(this.font, slotStack, slotX, slotY, stackCount);
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
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
		this.scrollOffset = this.clampMouseScroll(deltaY);
		return true;
	}
	
	@Override
	public boolean isHovering(Slot slot, double mouseX, double mouseY) {
		return this.isHovering(slot.x, slot instanceof MoveableSlot moveableSlot ? moveableSlot.getY(this.scrollOffset) : slot.y, 16, 16, mouseX, mouseY);
	}
}
