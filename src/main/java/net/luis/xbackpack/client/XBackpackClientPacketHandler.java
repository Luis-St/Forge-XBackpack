package net.luis.xbackpack.client;

import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.client.gui.screens.extension.AnvilExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.EnchantmentTableExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.FurnaceExtensionScreen;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class XBackpackClientPacketHandler {
	
	public static void handleUpdateFurnaceExtension(int cookingProgress, int fuelProgress) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.screen instanceof BackpackScreen screen) {
			if (screen.getExtensionScreen(BackpackExtension.FURNACE.get()) instanceof FurnaceExtensionScreen furnaceExtension) {
				furnaceExtension.setCookingProgress(cookingProgress);
				furnaceExtension.setFuelProgress(fuelProgress);
			}
		}
	}
	
	public static void handleUpdateAnvilExtension(int cost) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.screen instanceof BackpackScreen screen) {
			if (screen.getExtensionScreen(BackpackExtension.ANVIL.get()) instanceof AnvilExtensionScreen anvilExtension) {
				anvilExtension.setCost(cost);
			}
		}
	}
	
	
	
	public static void handleUpdateEnchantmentTableExtension(ResourceLocation[] enchantments, int[] enchantmentLevels, int[] enchantingCosts, int enchantmentSeed) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.screen instanceof BackpackScreen screen) {
			if (screen.getExtensionScreen(BackpackExtension.ENCHANTMENT_TABLE.get()) instanceof EnchantmentTableExtensionScreen enchantmentTableExtension) {
				enchantmentTableExtension.update(enchantments, enchantmentLevels, enchantingCosts, enchantmentSeed);
			}
		}
	}
	
}
