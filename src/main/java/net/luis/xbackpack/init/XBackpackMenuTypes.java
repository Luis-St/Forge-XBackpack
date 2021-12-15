package net.luis.xbackpack.init;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.common.inventory.menu.BackpackMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Luis-st
 *
 */

public class XBackpackMenuTypes {
	
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, XBackpack.MOD_ID);
	
	public static final RegistryObject<MenuType<BackpackMenu>> BACKPACK_MENU = MENU_TYPES.register("backpack_menu", () -> {
		return IForgeMenuType.create(BackpackMenu::new);
	});
	
}
