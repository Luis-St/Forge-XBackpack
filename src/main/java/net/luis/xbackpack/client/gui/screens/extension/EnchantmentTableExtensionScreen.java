package net.luis.xbackpack.client.gui.screens.extension;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.luis.xbackpack.client.gui.screens.AbstractExtensionContainerScreen;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.handler.EnchantingHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 * @author Luis-st
 *
 */

public class EnchantmentTableExtensionScreen extends AbstractExtensionScreen {
	
	private final Enchantment[] enchantments = new Enchantment[3];
	private final int[] enchantmentLevels = new int[3];
	private final int[] enchantingCosts = new int[3];
	private EnchantingHandler handler;
	private int enchantmentSeed = 0;
	
	public EnchantmentTableExtensionScreen(AbstractExtensionContainerScreen<?> screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtensions.ENCHANTMENT_TABLE.get(), extensions);
	}
	
	@Override
	protected void init() {
		this.handler = BackpackProvider.get(Objects.requireNonNull(this.minecraft.player)).getEnchantingHandler();
	}
	
	@Override
	protected void renderAdditional(PoseStack stack, float partialTicks, int mouseX, int mouseY, boolean open) {
		for (int row = 0; row < 3 && open; row++) {
			this.renderRow(stack, mouseX, mouseY, row, this.minecraft.player, this.enchantments[row], this.enchantingCosts[row]);
		}
	}
	
	private void renderRow(PoseStack stack, int mouseX, int mouseY, int row, LocalPlayer player, Enchantment enchantment, int enchantingCost) {
		if (enchantment != null) {
			int costColor;
			int enchantmentColor;
			RenderSystem.setShaderTexture(0, this.getTexture());
			if ((player.experienceLevel >= enchantingCost && this.hasFuel(row)) || player.getAbilities().instabuild) {
				GuiComponent.blit(stack, this.leftPos + this.imageWidth + 47, this.topPos + 97 + row * 19, 136, this.isHoveringRow(row, mouseX, mouseY) ? 38 : 0, 78, 19);
				this.renderLevel(stack, row, true);
				costColor = 8453920;
				enchantmentColor = 6839882;
			} else {
				GuiComponent.blit(stack, this.leftPos + this.imageWidth + 47, this.topPos + 97 + row * 19, 136, 19, 78, 19);
				this.renderLevel(stack, row, false);
				costColor = 4226832;
				enchantmentColor = 3419941;
			}
			this.renderLabels(stack, row, costColor, enchantmentColor);
		} else {
			GuiComponent.blit(stack, this.leftPos + this.imageWidth + 47, this.topPos + 97 + row * 19, 136, 19, 78, 19);
		}
	}
	
	private void renderLevel(PoseStack stack, int row, boolean active) {
		GuiComponent.blit(stack, this.leftPos + this.imageWidth + 47, this.topPos + 97 + row * 19, 136 + row * 19, active ? 57 : 76, 19, 19);
	}
	
	private void renderLabels(PoseStack stack, int row, int costColor, int enchantmentColor) {
		String cost = Integer.toString(this.enchantingCosts[row]);
		this.font.drawShadow(stack, cost, this.leftPos + this.imageWidth + 123 - this.font.width(cost), this.topPos + 106 + 19 * row, costColor);
		int length = 50 - this.font.width(cost);
		EnchantmentNames.getInstance().initSeed(this.enchantmentSeed + row);
		FormattedText enchantmentName = EnchantmentNames.getInstance().getRandomName(this.font, length);
		this.font.drawWordWrap(stack, enchantmentName, this.leftPos + this.imageWidth + 67, this.topPos + 99 + 19 * row, length, enchantmentColor);
	}
	
	@Override
	public void renderTooltip(PoseStack stack, int mouseX, int mouseY, boolean open, boolean renderable, Consumer<ItemStack> tooltipRenderer) {
		super.renderTooltip(stack, mouseX, mouseY, open, renderable, tooltipRenderer);
		for (int row = 0; row < 3 && open; row++) {
			this.renderTooltip(stack, mouseX, mouseY, row, this.minecraft.player, this.enchantments[row], this.enchantmentLevels[row], this.enchantingCosts[row]);
		}
	}
	
	private void renderTooltip(PoseStack stack, int mouseX, int mouseY, int row, LocalPlayer player, Enchantment enchantment, int enchantmentLevel, int enchantingCost) {
		int fuel = this.getFuel();
		int rowIndex = row + 1;
		if (this.isHoveringRow(row, mouseX, mouseY) && enchantingCost > 0) {
			List<Component> components = Lists.newArrayList();
			components.add((Component.translatable("container.enchant.clue", enchantment == null ? "" : enchantment.getFullname(enchantmentLevel))).withStyle(ChatFormatting.WHITE));
			if (enchantment == null) {
				components.add(CommonComponents.EMPTY);
				components.add(Component.translatable("forge.container.enchant.limitedEnchantability").withStyle(ChatFormatting.RED));
			} else if (!player.getAbilities().instabuild) {
				components.add(CommonComponents.EMPTY);
				if (enchantmentLevel >= player.experienceLevel) {
					components.add(Component.translatable("container.enchant.level.requirement", enchantmentLevel).withStyle(ChatFormatting.RED));
				} else {
					MutableComponent lapisComponent = rowIndex == 1 ? Component.translatable("container.enchant.lapis.one") : Component.translatable("container.enchant.lapis.many", rowIndex);
					components.add(lapisComponent.withStyle(fuel >= rowIndex ? ChatFormatting.GRAY : ChatFormatting.RED));
					MutableComponent levelComponent = rowIndex == 1 ? Component.translatable("container.enchant.level.one") : Component.translatable("container.enchant.level.many", rowIndex);
					components.add(levelComponent.withStyle(ChatFormatting.GRAY));
				}
			}
			this.screen.renderComponentTooltip(stack, components, mouseX, mouseY);
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.minecraft != null) {
			for (int row = 0; row < 3; row++) {
				if (this.isHoveringRow(row, mouseX, mouseY)) {
					Objects.requireNonNull(this.minecraft.gameMode).handleInventoryButtonClick(this.screen.getMenu().containerId, row);
				}
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	private boolean isHoveringRow(int row, double mouseX, double mouseY) {
		int x = this.leftPos + this.imageWidth + 47;
		int y = this.topPos + 97 + row * 19;
		return x + 77 >= mouseX && mouseX >= x && y + 18 >= mouseY && mouseY >= y;
	}
	
	private int getFuel() {
		return this.handler.getFuelHandler().getStackInSlot(0).getCount();
	}
	
	private boolean hasFuel(int row) {
		return this.getFuel() >= row + 1;
	}
	
	public void update(ResourceLocation[] enchantments, int[] enchantmentLevels, int[] enchantingCosts, int enchantmentSeed) {
		for (int row = 0; row < this.enchantments.length; row++) {
			this.enchantments[row] = ForgeRegistries.ENCHANTMENTS.getValue(enchantments[row]);
		}
		System.arraycopy(enchantmentLevels, 0, this.enchantmentLevels, 0, this.enchantmentLevels.length);
		System.arraycopy(enchantingCosts, 0, this.enchantingCosts, 0, this.enchantingCosts.length);
		this.enchantmentSeed = enchantmentSeed;
	}
	
}
