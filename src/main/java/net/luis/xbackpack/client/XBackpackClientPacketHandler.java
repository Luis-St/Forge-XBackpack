package net.luis.xbackpack.client;

import org.jetbrains.annotations.Nullable;

import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.client.gui.screens.extension.AbstractExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.AnvilExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.BrewingStandExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.EnchantmentTableExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.FurnaceExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.StonecutterExtensionScreen;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Luis-st
 *
 */

public class XBackpackClientPacketHandler {
	
	@Nullable
	private static AbstractExtensionScreen getExtensionScreen(BackpackExtension extension) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.screen instanceof BackpackScreen screen) {
			AbstractExtensionScreen extensionScreen = screen.getExtensionScreen(extension);
			if (extensionScreen != null) {
				return extensionScreen;
			}
		}
		return null;
	}
	
	public static void updateFurnaceExtension(int cookingProgress, int fuelProgress) {
		if (getExtensionScreen(BackpackExtension.FURNACE.get()) instanceof FurnaceExtensionScreen furnaceExtension) {
			furnaceExtension.update(cookingProgress, fuelProgress);
		}
	}
	
	public static void updateAnvilExtension(int cost) {
		if (getExtensionScreen(BackpackExtension.ANVIL.get()) instanceof AnvilExtensionScreen anvilExtension) {
			anvilExtension.update(cost);
		}
	}
	
	public static void updateEnchantmentTableExtension(ResourceLocation[] enchantments, int[] enchantmentLevels, int[] enchantingCosts, int enchantmentSeed) {
		if (getExtensionScreen(BackpackExtension.ENCHANTMENT_TABLE.get()) instanceof EnchantmentTableExtensionScreen enchantmentTableExtension) {
			enchantmentTableExtension.update(enchantments, enchantmentLevels, enchantingCosts, enchantmentSeed);
		}
	}
	
	public static void updateStonecutterExtension(boolean resetSelected) {
		if (getExtensionScreen(BackpackExtension.STONECUTTER.get()) instanceof StonecutterExtensionScreen stonecutterScreen) {
			stonecutterScreen.updateRecipes(resetSelected);
		}
	}
	
	public static void updateBrewingStandExtension(int fuel, int brewTime) {
		if (getExtensionScreen(BackpackExtension.BREWING_STAND.get()) instanceof BrewingStandExtensionScreen brewingStandScreen) {
			brewingStandScreen.update(fuel, brewTime);
		}
	}
	
}
