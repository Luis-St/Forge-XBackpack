package net.luis.xbackpack.client.gui.screens.extension;

import com.google.common.collect.Lists;
import net.luis.xbackpack.client.gui.screens.AbstractExtensionContainerScreen;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.handler.CraftingHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 * @author Luis-St
 *
 */

public class StonecutterExtensionScreen extends AbstractExtensionScreen {
	
	private final List<RecipeHolder<StonecutterRecipe>> recipes = Lists.newArrayList();
	private Player player;
	private CraftingHandler handler;
	private double scrollOffset = 0.0F;
	private boolean scrolling = false;
	private int startIndex = 0;
	private int selectedRecipe = -1;
	
	public StonecutterExtensionScreen(AbstractExtensionContainerScreen<?> screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtensions.STONECUTTER.get(), extensions);
	}
	
	@Override
	protected void init() {
		this.player = Objects.requireNonNull(this.minecraft.player);
		this.handler = BackpackProvider.get(this.player).getStonecutterHandler();
	}
	
	@Override
	protected void renderAdditional(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, boolean open) {
		if (open) {
			graphics.blit(this.getTexture(), this.leftPos + this.imageWidth + 72, this.topPos + 143 + (int) (39.0 * this.scrollOffset), 95 + (this.isScrollBarActive() ? 0 : 12), 0, 12, 15);
			this.renderButtons(graphics, mouseX, mouseY);
			this.renderRecipes(graphics);
		}
	}
	
	private void renderButtons(GuiGraphics graphics, int mouseX, int mouseY) {
		for (int index = this.startIndex; index < this.startIndex + 12 && index < this.recipes.size(); ++index) {
			int i = index - this.startIndex;
			int x = this.leftPos + 225 + index % 4 * 16;
			int y = this.topPos + 142 + (i / 4) * 18 + 2;
			int offset = 15;
			if (index == this.selectedRecipe) {
				offset += 18;
			} else if (mouseX >= x && x + 16 > mouseX && mouseY + 1 >= y && y + 18 > mouseY + 1) {
				offset += 36;
			}
			graphics.blit(this.getTexture(), x, y - 1, 95, offset, 16, 18);
		}
	}
	
	private void renderRecipes(GuiGraphics graphics) {
		for (int index = this.startIndex; index < this.startIndex + 12 && index < this.recipes.size(); ++index) {
			int i = index - this.startIndex;
			int x = this.leftPos + 225 + index % 4 * 16;
			int y = this.topPos + 142 + (i / 4) * 18 + 2;
			graphics.renderItem(this.recipes.get(index).value().getResultItem(this.player.level().registryAccess()), x, y);
		}
	}
	
	@Override
	public void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY, boolean open, boolean renderable, Consumer<ItemStack> tooltipRenderer) {
		super.renderTooltip(graphics, mouseX, mouseY, open, renderable, tooltipRenderer);
		if (open) {
			if (this.shouldDisplayRecipes()) {
				for (int index = this.startIndex; index < this.startIndex + 12 && index < this.recipes.size(); ++index) {
					int i = index - this.startIndex;
					double x = mouseX - (double) (this.leftPos + 225 + i % 4 * 16);
					double y = mouseY - (double) (this.topPos + 142 + i / 4 * 18);
					if (x >= 0.0 && y >= 0.0 && x < 16.0 && y < 18.0) {
						tooltipRenderer.accept(this.recipes.get(index).value().getResultItem(this.player.level().registryAccess()));
					}
				}
			}
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.shouldDisplayRecipes()) {
			for (int index = this.startIndex; index < this.startIndex + 12; ++index) {
				int i = index - this.startIndex;
				double x = mouseX - (double) (this.leftPos + 225 + i % 4 * 16);
				double y = mouseY - (double) (this.topPos + 142 + i / 4 * 18);
				if (x >= 0.0 && y >= 0.0 && x < 16.0 && y < 18.0) {
					Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
					Objects.requireNonNull(this.minecraft.gameMode).handleInventoryButtonClick(this.screen.getMenu().containerId, index);
					this.selectedRecipe = index;
					return true;
				}
			}
			double x = this.leftPos + this.imageWidth + 72;
			double y = this.topPos + 143;
			if (mouseX >= x && x + 12 >= mouseX && mouseY >= y && y + 54 >= mouseY) {
				this.scrolling = true;
				return true;
			}
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
		if (this.scrolling && this.isScrollBarActive()) {
			int top = this.topPos + 141;
			int bottom = top + 54;
			this.scrollOffset = (mouseY - top - 7.5) / ((bottom - top) - 15.0);
			this.scrollOffset = Mth.clamp(this.scrollOffset, 0.0, 1.0);
			this.startIndex = (int) (this.scrollOffset * (double) this.getOffScreenRows() + 0.5) * 4;
			return true;
		} else {
			return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
		}
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (this.isScrollBarActive()) {
			double offset = delta / (double) this.getOffScreenRows();
			this.scrollOffset = Mth.clamp(this.scrollOffset - offset, 0.0F, 1.0F);
			this.startIndex = (int) (this.scrollOffset * (double) this.getOffScreenRows() + 0.5) * 4;
		}
		return true;
	}
	
	private ItemStack getInputStack() {
		return this.handler.getInputHandler().getStackInSlot(0);
	}
	
	private boolean shouldDisplayRecipes() {
		return !this.getInputStack().isEmpty();
	}
	
	private boolean isScrollBarActive() {
		return this.shouldDisplayRecipes() && this.recipes.size() > 12;
	}
	
	private int getOffScreenRows() {
		return (this.recipes.size() + 4 - 1) / 4 - 3;
	}
	
	public void updateRecipes(boolean resetSelected) {
		this.recipes.clear();
		this.recipes.addAll(Objects.requireNonNull(this.minecraft.getConnection()).getRecipeManager().getRecipesFor(RecipeType.STONECUTTING, new SimpleContainer(this.getInputStack()), Objects.requireNonNull(this.minecraft.level)));
		this.scrollOffset = 0.0F;
		this.startIndex = 0;
		if (resetSelected) {
			this.selectedRecipe = -1;
		}
	}
}
