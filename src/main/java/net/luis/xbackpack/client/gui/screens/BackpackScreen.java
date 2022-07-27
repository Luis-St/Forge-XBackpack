package net.luis.xbackpack.client.gui.screens;

import static net.luis.xbackpack.world.extension.BackpackExtension.ANVIL;
import static net.luis.xbackpack.world.extension.BackpackExtension.BREWING_STAND;
import static net.luis.xbackpack.world.extension.BackpackExtension.CRAFTING_TABLE;
import static net.luis.xbackpack.world.extension.BackpackExtension.ENCHANTMENT_TABLE;
import static net.luis.xbackpack.world.extension.BackpackExtension.FURNACE;
import static net.luis.xbackpack.world.extension.BackpackExtension.GRINDSTONE;
import static net.luis.xbackpack.world.extension.BackpackExtension.SMITHING_TABLE;
import static net.luis.xbackpack.world.extension.BackpackExtension.STONECUTTER;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.client.gui.screens.extension.AbstractExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.AnvilExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.BrewingStandExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.CraftingExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.EnchantmentTableExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.ExtensionScreenHolder;
import net.luis.xbackpack.client.gui.screens.extension.FurnaceExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.GrindstoneExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.SmithingTableExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.StonecutterExtensionScreen;
import net.luis.xbackpack.network.XBackpackNetworkHandler;
import net.luis.xbackpack.network.packet.extension.UpdateBackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.extension.slot.AnvilExtensionResultSlot;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionResultSlot;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionSlot;
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

public class BackpackScreen extends AbstractScrollableContainerScreen<BackpackMenu> implements ExtensionScreenHolder {

	private static final ResourceLocation BACKPACK = new ResourceLocation(XBackpack.MOD_ID, "textures/gui/container/backpack.png");
	public static final ResourceLocation ICONS = new ResourceLocation(XBackpack.MOD_ID, "textures/gui/container/backpack_icons.png");
	
	private final List<BackpackExtension> extensions = List.of(CRAFTING_TABLE.get(), FURNACE.get(), ANVIL.get(), ENCHANTMENT_TABLE.get(), STONECUTTER.get(), BREWING_STAND.get(), GRINDSTONE.get(), SMITHING_TABLE.get());
	private final List<AbstractExtensionScreen> extensionScreens = Lists.newArrayList();
	private BackpackExtension extension = BackpackExtension.NO.get();
	
	public BackpackScreen(BackpackMenu menu, Inventory inventory, Component titleComponent) {
		super(menu, inventory, titleComponent);
		this.passEvents = false;
		this.imageWidth = 220;
		this.imageHeight = 220;
		this.inventoryLabelX += 22;
		this.inventoryLabelY = 127;
		this.extensionScreens.add(new CraftingExtensionScreen(this, this.extensions));
		this.extensionScreens.add(new FurnaceExtensionScreen(this, this.extensions));
		this.extensionScreens.add(new AnvilExtensionScreen(this, this.extensions));
		this.extensionScreens.add(new EnchantmentTableExtensionScreen(this, this.extensions));
		this.extensionScreens.add(new StonecutterExtensionScreen(this, this.extensions));
		this.extensionScreens.add(new BrewingStandExtensionScreen(this, this.extensions));
		this.extensionScreens.add(new GrindstoneExtensionScreen(this, this.extensions));
		this.extensionScreens.add(new SmithingTableExtensionScreen(this, this.extensions));
	}
	
	@Override
	protected void init() {
		super.init();
		this.extensionScreens.forEach((extensionScreen) -> {
			extensionScreen.init(this.minecraft, this.itemRenderer, this.font, this.imageWidth, this.imageHeight, this.leftPos, this.topPos);
		});
	}
	
	@Override
	protected boolean isSlotActive(Slot slot) {
		if (slot instanceof MoveableSlot moveableSlot) {
			int y = moveableSlot.getY(this.scrollOffset);
			return slot.isActive() && 174 >= slot.x && slot.x >= 30 && 108 >= y && y >= 18;
		} else if (slot instanceof ExtensionSlot extensionSlot) {
			return this.extension == extensionSlot.getExtension();
		} else if (slot instanceof ExtensionResultSlot extensionSlot) {
			return this.extension == extensionSlot.getExtension();
		} else if (slot instanceof AnvilExtensionResultSlot extensionSlot) {
			return this.extension == extensionSlot.getExtension();
		}
		return slot.isActive();
	}
	
	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		super.render(stack, mouseX, mouseY, partialTicks);
		this.renderTooltip(stack, mouseX, mouseY);
	}
	
	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		this.renderBackground(stack);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		this.renderExtensions(stack, partialTicks, mouseX, mouseY);
		RenderSystem.setShaderTexture(0, BACKPACK);	
		this.blit(stack, this.leftPos, this.topPos, 0, 0, 238, 220);
		int scrollPosition = this.topPos + 18 + this.scrollOffset;
		this.blit(stack, this.leftPos + 198, scrollPosition, 244, 0, 12, 15);
	}
	
	private void renderExtensions(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		for (BackpackExtension extension : this.extensions) {
			AbstractExtensionScreen extensionScreen = this.getExtensionScreen(extension);
			if (extensionScreen != null) {
				if (this.extension == extension && this.extension != BackpackExtension.NO.get()) {
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
		AbstractExtensionScreen extensionScreen = this.getExtensionScreen(this.extension);
		if (extensionScreen != null) {
			extensionScreen.renderTooltip(stack, mouseX, mouseY, (itemStack) -> {
				this.renderTooltip(stack, itemStack, mouseX, mouseY);
			});
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
	
	private void updateExtension(BackpackExtension extension) {
		if (this.extension == extension || extension == null) {
			this.extension = BackpackExtension.NO.get();
		} else {
			this.extension = extension;
		}
		XBackpackNetworkHandler.getChannel().sendToServer(new UpdateBackpackExtension(this.extension));
	}

	@Override
	public List<AbstractExtensionScreen> getExtensionScreens() {
		return ImmutableList.copyOf(this.extensionScreens);
	}

	@Override
	public AbstractExtensionScreen getExtensionScreen(BackpackExtension extension) {
		for (AbstractExtensionScreen extensionScreen : this.extensionScreens) {
			if (extensionScreen.getExtension() == extension) {
				return extensionScreen;
			}
		}
		return null;
	}
	
}
