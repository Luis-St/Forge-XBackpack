package net.luis.xbackpack.client.gui.screens.extension;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.world.capability.XBackpackCapabilities;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 
 * @author Luis-st
 *
 */

public class EnchantmentTableExtensionScreen extends AbstractExtensionScreen {
	
	private final Enchantment[] enchantments = new Enchantment[3];
	private final int[] enchantmentLevels = new int[3];
	private final int[] enchantingCosts = new int[3];
	private int enchantmentSeed = 0;
	
	public EnchantmentTableExtensionScreen(BackpackScreen screen, List<BackpackExtension> extensions) {
		super(screen, BackpackExtension.ENCHANTMENT_TABLE.get(), extensions);
	}
	
	@Override
	protected void renderAdditional(PoseStack stack, float partialTicks, int mouseX, int mouseY, boolean open) {
		if (open) {
			if (this.minecraft != null) {
				LocalPlayer player = this.minecraft.player;
				for (int row = 0; row < 3; row++) {
					this.renderRow(stack, mouseX, mouseY, row, player, this.enchantments[row], this.enchantmentLevels[row], this.enchantingCosts[row]);
				}
				for (int row = 0; row < 3; row++) {
					this.renderTooltip(stack, mouseX, mouseY, row, player, this.enchantments[row], this.enchantmentLevels[row], this.enchantingCosts[row]);
				}
			}

		}
	}
	
	private void renderRow(PoseStack stack, int mouseX, int mouseY, int row, LocalPlayer player, Enchantment enchantment, int enchantmentLevel, int enchantingCost) {
		if (enchantment != null) {
			int costColor = 0;
			int enchantmentColor = 0;
			RenderSystem.setShaderTexture(0, this.getTexture());
			if ((player.experienceLevel >= enchantingCost || player.getAbilities().instabuild) && this.hasFuel(row)) {
				if (this.isHoveringRow(row, mouseX, mouseY)) {
					this.screen.blit(stack, this.leftPos + this.imageWidth + 47, this.topPos + 97 + row * 19, 136, 38, 78, 19);
				} else {
					this.screen.blit(stack, this.leftPos + this.imageWidth + 47, this.topPos + 97 + row * 19, 136, 0, 78, 19);
				}
				this.renderLevel(stack, row, true);
				costColor = 8453920;
				enchantmentColor = 6839882;
			} else {
				this.screen.blit(stack, this.leftPos + this.imageWidth + 47, this.topPos + 97 + row * 19, 136, 19, 78, 19);
				this.renderLevel(stack, row, false);
				costColor = 4226832;
				enchantmentColor = 3419941;
			}
			this.renderLabels(stack, row, costColor, enchantmentColor);
		} else {
			this.screen.blit(stack, this.leftPos + this.imageWidth + 47, this.topPos + 97 + row * 19, 136, 19, 78, 19);
		}
	}
	
	private void renderLevel(PoseStack stack, int row, boolean active) {
		if (active) {
			this.screen.blit(stack, this.leftPos + this.imageWidth + 47, this.topPos + 97 + row * 19, 136 + row * 19, 57, 19, 19);
		} else {
			this.screen.blit(stack, this.leftPos + this.imageWidth + 47, this.topPos + 97 + row * 19, 136 + row * 19, 76, 19, 19);
		}
	}
	
	private void renderLabels(PoseStack stack, int row, int costColor, int enchantmentColor) {
		String cost = Integer.toString(this.enchantingCosts[row]);
		this.font.drawShadow(stack, cost, this.leftPos + this.imageWidth + 123 - this.font.width(cost), this.topPos + 106 + 19 * row, costColor);
		int length = 50 - this.font.width(cost);
		EnchantmentNames.getInstance().initSeed(this.enchantmentSeed + row);
		FormattedText enchantmentName = EnchantmentNames.getInstance().getRandomName(this.font, length);
		this.font.drawWordWrap(enchantmentName, this.leftPos + this.imageWidth + 67, this.topPos + 99 + 19 * row, length, enchantmentColor);
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
					MutableComponent mutablecomponent;
					if (rowIndex == 1) {
						mutablecomponent = Component.translatable("container.enchant.lapis.one");
					} else {
						mutablecomponent = Component.translatable("container.enchant.lapis.many", rowIndex);
					}
					components.add(mutablecomponent.withStyle(fuel >= rowIndex ? ChatFormatting.GRAY : ChatFormatting.RED));
					MutableComponent mutablecomponent1;
					if (rowIndex == 1) {
						mutablecomponent1 = Component.translatable("container.enchant.level.one");
					} else {
						mutablecomponent1 = Component.translatable("container.enchant.level.many", rowIndex);
					}
					components.add(mutablecomponent1.withStyle(ChatFormatting.GRAY));
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
					this.minecraft.gameMode.handleInventoryButtonClick(this.screen.getMenu().containerId, row);
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
		return this.minecraft.player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new).getEnchantingHandler().getStackInSlot(2).getCount();
	}
	
	private boolean hasFuel(int row) {
		return this.getFuel() >= row + 1;
	}
	
	public void update(ResourceLocation[] enchantments, int[] enchantmentLevels, int[] enchantingCosts, int enchantmentSeed) {
		for (int row = 0; row < this.enchantments.length; row++) {
			this.enchantments[row] = ForgeRegistries.ENCHANTMENTS.getValue(enchantments[row]);
		}
		for (int row = 0; row < this.enchantmentLevels.length; row++) {
			this.enchantmentLevels[row] = enchantmentLevels[row];
		}
		for (int row = 0; row < this.enchantingCosts.length; row++) {
			this.enchantingCosts[row] = enchantingCosts[row];
		}
		this.enchantmentSeed = enchantmentSeed;
	}

}
