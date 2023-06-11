package net.luis.xbackpack.world.inventory.extension;

import com.google.common.collect.Maps;
import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.AbstractExtensionContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Map;

/**
 *
 * @author Luis-st
 *
 */

public class ExtensionMenuRegistry {
	
	private static final Map<BackpackExtension, ExtensionMenuFactory> EXTENSION_FACTORIES = Maps.newHashMap();
	
	public static AbstractExtensionMenu getExtensionMenu(BackpackExtension extension, AbstractExtensionContainerMenu menu, Player player, ExtensionMenuFactory fallbackFactory) {
		return EXTENSION_FACTORIES.getOrDefault(extension, fallbackFactory).create(menu, player);
	}
	
	/**
	 * Use this method to register a {@link ExtensionMenuFactory} for the {@link BackpackExtension}.
	 * Call this method in {@link FMLCommonSetupEvent}
	 */
	public static void registerOverride(BackpackExtension extension, String modid, ExtensionMenuFactory factory) {
		if (EXTENSION_FACTORIES.containsKey(extension)) {
			XBackpack.LOGGER.error("Fail to register Extension Menu override for Mod {} of type {}, since there is already a Extension Menu present", modid, BackpackExtensions.REGISTRY.get().getKey(extension));
		} else {
			EXTENSION_FACTORIES.put(extension, factory);
		}
	}
}
