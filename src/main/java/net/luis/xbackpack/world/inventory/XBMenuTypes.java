package net.luis.xbackpack.world.inventory;

import net.luis.xbackpack.XBackpack;
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

public class XBMenuTypes {
	
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, XBackpack.MOD_ID);
	
	public static final RegistryObject<MenuType<BackpackMenu>> BACKPACK_MENU = MENU_TYPES.register("backpack_menu", () -> IForgeMenuType.create(BackpackMenu::new));
	
}
