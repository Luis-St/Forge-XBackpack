package net.luis.xbackpack.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.client.gui.components.RenderData;
import net.luis.xbackpack.client.gui.screens.extension.*;
import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.network.packet.modifier.ResetItemModifierPacket;
import net.luis.xbackpack.network.packet.modifier.UpdateSearchTermPacket;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionMenuSlot;
import net.luis.xbackpack.world.inventory.modifier.ItemModifierType;
import net.luis.xbackpack.world.inventory.modifier.filter.ItemFilters;
import net.luis.xbackpack.world.inventory.modifier.sorter.ItemSorters;
import net.luis.xbackpack.world.inventory.slot.BackpackSlot;
import net.luis.xbackpack.world.inventory.slot.MoveableSlot;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 *
 * @author Luis-st
 *
 */

public class BackpackScreen extends AbstractModifiableContainerScreen<BackpackMenu> {
	
	private static final ResourceLocation BACKPACK = new ResourceLocation(XBackpack.MOD_ID, "textures/gui/container/backpack.png");
	private static final ResourceLocation ICONS = new ResourceLocation(XBackpack.MOD_ID, "textures/gui/container/backpack_icons.png");
	
	private RenderData searchData;
	private RenderData filterData;
	private RenderData sorterData;
	private RenderData mergerData;
	
	public BackpackScreen(BackpackMenu menu, Inventory inventory, Component titleComponent) {
		super(menu, inventory, titleComponent);
		this.passEvents = false;
		this.imageWidth = 220;
		this.imageHeight = 220;
		this.inventoryLabelX += 22;
		this.inventoryLabelY = 127;
		this.addExtensionScreen(CraftingExtensionScreen::new);
		this.addExtensionScreen(FurnaceExtensionScreen::new);
		this.addExtensionScreen(AnvilExtensionScreen::new);
		this.addExtensionScreen(EnchantmentTableExtensionScreen::new);
		this.addExtensionScreen(StonecutterExtensionScreen::new);
		this.addExtensionScreen(BrewingStandExtensionScreen::new);
		this.addExtensionScreen(GrindstoneExtensionScreen::new);
		this.addExtensionScreen(SmithingTableExtensionScreen::new);
	}
	
	@Override
	protected @NotNull SlotRenderType getSlotRenderType(Slot slot) {
		if (slot instanceof ExtensionMenuSlot extensionSlot) {
			return this.getExtension() == extensionSlot.getExtension() ? SlotRenderType.DEFAULT : SlotRenderType.SKIP;
		} else if (slot instanceof BackpackSlot) {
			int y = slot instanceof MoveableSlot moveableSlot ? moveableSlot.getY(this.scrollOffset) : slot.y;
			if (174 >= slot.x && slot.x >= 30 && 108 >= y && y >= 18) {
				return SlotRenderType.DEFAULT;
			} else {
				return SlotRenderType.SKIP;
			}
		}
		return super.getSlotRenderType(slot);
	}
	
	@Override
	protected void renderScreen(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		super.renderScreen(stack, mouseX, mouseY, partialTicks);
		RenderSystem.setShaderTexture(0, ICONS);
		blit(stack, this.leftPos + 75, this.topPos + 6, 32, 0, 8, 8);
		GuiComponent.blit(stack, this.leftPos + 89, this.topPos + 6, 8, 8, 46, 0, 14, 14, 256, 256);
		if (this.menu.getFilter() == ItemFilters.NONE && this.menu.getSorter() == ItemSorters.NONE) {
			GuiComponent.blit(stack, this.leftPos + 200, this.topPos + 6, 8, 8, 40, 0, 6, 6, 256, 256);
		}
	}
	
	@Override
	protected void renderBg(@NotNull PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		super.renderBg(stack, partialTicks, mouseX, mouseY);
		RenderSystem.setShaderTexture(0, BACKPACK);
		blit(stack, this.leftPos, this.topPos, 0, 0, 220, 220);
		int scrollPosition = this.topPos + 18 + this.scrollOffset;
		blit(stack, this.leftPos + 198, scrollPosition, 244, 0, 12, 15);
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
		return topX + this.getScrollbarWidth() >= mouseX && mouseX >= topX && topY + this.getScrollbarHeight() >= mouseY && mouseY >= topY;
	}
	
	@Override
	protected int clampMouseMove(double mouseY) {
		return Mth.clamp((int) (mouseY - this.topPos - 25.5), 0, this.getScrollbarHeight() - 17);
	}
	
	@Override
	protected int clampMouseScroll(double delta) {
		return Mth.clamp((int) (this.scrollOffset - delta), 0, this.getScrollbarHeight() - 17);
	}
	
	@Override
	public Slot findSlot(double mouseX, double mouseY) {
		return super.findSlot(mouseX, mouseY);
	}
	
	@Override
	protected RenderData getSearchData() {
		if (this.searchData == null) {
			this.searchData = new RenderData(true, this.leftPos + 103, this.topPos + 6, 86, 9);
		}
		return new RenderData(true, this.leftPos + 103, this.topPos + 6, 86, 9);
	}
	
	@Override
	protected int getMaxSearchLength() {
		return 50;
	}
	
	@Override
	protected RenderData getFilterData() {
		if (this.filterData == null) {
			this.filterData = new RenderData(true, this.leftPos + 73, this.topPos + 4, 12, 12);
		}
		return new RenderData(true, this.leftPos + 73, this.topPos + 4, 12, 12);
	}
	
	@Override
	protected RenderData getSorterData() {
		if (this.sorterData == null) {
			this.sorterData = new RenderData(true, this.leftPos + 87, this.topPos + 4, 12, 12);
		}
		return new RenderData(true, this.leftPos + 87, this.topPos + 4, 12, 12);
	}
	
	@Override
	protected RenderData getMergerData() {
		if (this.mergerData == null) {
			this.mergerData = new RenderData(true, this.leftPos + 198, this.topPos + 4, 12, 12);
		}
		return new RenderData(true, this.leftPos + 198, this.topPos + 4, 12, 12);
	}
	
	@Override
	protected TooltipFlag getTooltipFlag() {
		return BackpackProvider.get(Objects.requireNonNull(Objects.requireNonNull(this.minecraft).player)).getConfig().getClientConfig().shouldShowModifierInfo() ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;
	}
	
	@Override
	protected void updateSearchTerm(String searchBoxValue) {
		XBNetworkHandler.INSTANCE.sendToServer(new UpdateSearchTermPacket(searchBoxValue));
	}
	
	@Override
	protected void updateItemModifier(ItemModifierType modifierType) {
		XBNetworkHandler.INSTANCE.sendToServer(new ResetItemModifierPacket(modifierType));
	}
	
}
