package net.luis.xbackpack.client.gui.screens.extension;

import com.mojang.blaze3d.systems.RenderSystem;
import net.luis.xbackpack.client.gui.screens.AbstractExtensionContainerScreen;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.extension.AnvilExtensionMenu;
import net.luis.xbackpack.world.inventory.handler.CraftingHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Luis-st
 *
 */

public class AnvilExtensionScreen extends AbstractExtensionScreen {
	
	private CraftingHandler handler;
	private int cost;
	
	public AnvilExtensionScreen(AbstractExtensionContainerScreen<?> screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtensions.ANVIL.get(), extensions);
	}
	
	@Override
	protected void init() {
		this.handler = BackpackProvider.get(Objects.requireNonNull(this.minecraft.player)).getAnvilHandler();
	}
	
	@Override
	protected void renderAdditional(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, boolean open) {
		if (open) {
			if (this.shouldRenderCanceled()) {
				graphics.blit(this.getTexture(), this.leftPos + this.imageWidth + 59, this.topPos + 71, 111, 0, 22, 21);
			}
			this.renderLabels(graphics);
		}
	}
	
	private boolean shouldRenderCanceled() {
		if (!this.handler.getInputHandler().getStackInSlot(0).isEmpty() || !this.handler.getInputHandler().getStackInSlot(1).isEmpty()) {
			return this.handler.getResultHandler().getStackInSlot(0).isEmpty();
		}
		return false;
	}
	
	private void renderLabels(GuiGraphics graphics) {
		RenderSystem.disableBlend();
		if (this.screen.getMenu().getExtensionMenu(this.extension) instanceof AnvilExtensionMenu menu) {
			if (this.cost > 0) {
				int color = 8453920;
				Component component = null;
				if (!this.handler.getResultHandler().getStackInSlot(0).isEmpty() && this.minecraft != null) {
					component = Component.translatable("xbackpack.backpack_extension.anvil.cost", this.cost);
					if (!menu.mayPickup(Objects.requireNonNull(this.minecraft.player))) {
						color = 16736352;
					}
				}
				if (component != null) {
					int x = this.leftPos + this.imageWidth + 64;
					int y = this.topPos + 9 + this.getExtensionOffset(this.extension);
					if (10 > this.cost) {
						graphics.fill(x - 1, y - 2, x + this.font.width(component) + 3, y + 10, 1325400064);
						graphics.drawString(this.font, component, x + 1, y, color);
					} else if (100 > this.cost) {
						graphics.fill(x - 7, y - 2, x + this.font.width(component) - 3, y + 10, 1325400064);
						graphics.drawString(this.font, component, x - 5, y, color);
					} else {
						graphics.fill(x - 13, y - 2, x + this.font.width(component) - 9, y + 10, 1325400064);
						graphics.drawString(this.font, component, x - 11, y, color);
					}
				}
			}
		}
	}
	
	public void update(int cost) {
		this.cost = cost;
	}
}
