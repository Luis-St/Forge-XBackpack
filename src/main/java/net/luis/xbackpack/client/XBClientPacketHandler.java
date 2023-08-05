package net.luis.xbackpack.client;

import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.client.gui.screens.extension.*;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.modifier.ModifiableMenu.UpdateType;
import net.luis.xbackpack.world.inventory.modifier.filter.ItemFilter;
import net.luis.xbackpack.world.inventory.modifier.sorter.ItemSorter;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 *
 * @author Luis-St
 *
 */

public class XBClientPacketHandler {
	
	public static void updateBackpack(CompoundTag tag) {
		BackpackProvider.get(Objects.requireNonNull(Minecraft.getInstance().player)).deserialize(tag);
	}
	
	@Nullable
	private static AbstractExtensionScreen getExtensionScreen(BackpackExtension extension) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.screen instanceof BackpackScreen screen) {
			return screen.getExtensionScreen(extension);
		}
		return null;
	}
	
	public static void updateFurnaceExtension(int cookingProgress, int fuelProgress) {
		if (getExtensionScreen(BackpackExtensions.FURNACE.get()) instanceof FurnaceExtensionScreen furnaceExtension) {
			furnaceExtension.update(cookingProgress, fuelProgress);
		}
	}
	
	public static void updateAnvilExtension(int cost) {
		if (getExtensionScreen(BackpackExtensions.ANVIL.get()) instanceof AnvilExtensionScreen anvilExtension) {
			anvilExtension.update(cost);
		}
	}
	
	public static void updateEnchantmentTableExtension(ResourceLocation[] enchantments, int[] enchantmentLevels, int[] enchantingCosts, int enchantmentSeed) {
		if (getExtensionScreen(BackpackExtensions.ENCHANTMENT_TABLE.get()) instanceof EnchantmentTableExtensionScreen enchantmentTableExtension) {
			enchantmentTableExtension.update(enchantments, enchantmentLevels, enchantingCosts, enchantmentSeed);
		}
	}
	
	public static void updateStonecutterExtension(boolean resetSelected) {
		if (getExtensionScreen(BackpackExtensions.STONECUTTER.get()) instanceof StonecutterExtensionScreen stonecutterScreen) {
			stonecutterScreen.updateRecipes(resetSelected);
		}
	}
	
	public static void updateBrewingStandExtension(int fuel, int brewTime) {
		if (getExtensionScreen(BackpackExtensions.BREWING_STAND.get()) instanceof BrewingStandExtensionScreen brewingStandScreen) {
			brewingStandScreen.update(fuel, brewTime);
		}
	}
	
	public static void updateBackpackItemModifiers(ItemFilter filter, ItemSorter sorter) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.screen instanceof BackpackScreen screen) {
			screen.getMenu().updateFilter(filter, UpdateType.SET_NO_UPDATE, null);
			screen.getMenu().updateSorter(sorter, UpdateType.SET_NO_UPDATE, null);
		}
	}
	
}
