package net.luis.xbackpack.client.gui.screens.extension;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.world.capability.XBackpackCapabilities;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.extension.AnvilExtensionMenu;
import net.luis.xbackpack.world.inventory.handler.CraftingHandler;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;

/**
 * 
 * @author Luis-st
 *
 */

public class AnvilExtensionScreen extends AbstractExtensionScreen {
	
	private int cost;
	
	public AnvilExtensionScreen(BackpackScreen screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtension.ANVIL.get(), extensions);
	}
	
	@Override
	protected void renderAdditional(PoseStack stack, float partialTicks, int mouseX, int mouseY, boolean open) {
		if (open) {
			if (this.minecraft != null && this.shouldRenderCanceled()) {
				RenderSystem.setShaderTexture(0, this.getTexture());
				this.screen.blit(stack, this.leftPos + this.imageWidth + 59, this.topPos + 71, 111, 0, 22, 21);
			}
			this.renderLabels(stack);
		}	
	}
	
	private boolean shouldRenderCanceled() {
		CraftingHandler handler = this.minecraft.player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new).getAnvilHandler();
		if (!handler.getInputHandler().getStackInSlot(0).isEmpty() || !handler.getInputHandler().getStackInSlot(1).isEmpty()) {
			return handler.getResultHandler().getStackInSlot(0).isEmpty();
		}
		return false;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	private void renderLabels(PoseStack stack) {
		CraftingHandler handler = this.minecraft.player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new).getAnvilHandler();
		RenderSystem.disableBlend();
		if (this.screen.getMenu().getExtensionMenu(this.extension) instanceof AnvilExtensionMenu menu) {
			if (this.cost > 0) {
				int color = 8453920;
				Component component = null;
				if (this.cost >= 40 && !this.minecraft.player.getAbilities().instabuild) {
					component = Component.translatable("xbackpack.backpack_extension.anvil.cost", "X");
					color = 16736352;
				} else if (handler.getResultHandler().getStackInSlot(0).isEmpty()) {
					component = null;
				} else if (this.minecraft != null) {
					component = Component.translatable("xbackpack.backpack_extension.anvil.cost", this.cost);
					if (!this.screen.getMenu().getSlot(940).mayPickup(this.minecraft.player)) {
						color = 16736352;
					}
				}
				if (component != null) {
					int x = this.leftPos + this.imageWidth + 64;
					int y = this.topPos + 9 + this.getExtensionOffset(this.extension);
					if (this.cost >= 40 || 10 > this.cost) {
						GuiComponent.fill(stack, x - 2, y - 2, x + this.font.width(component) + 2, y + 10, 1325400064);
						this.font.drawShadow(stack, component, x, y, color);
					} else {
						GuiComponent.fill(stack, x - 8, y - 2, x + this.font.width(component) - 4, y + 10, 1325400064);
						this.font.drawShadow(stack, component, x - 6, y, color);
					}
				}
			}
		}
	}

}
