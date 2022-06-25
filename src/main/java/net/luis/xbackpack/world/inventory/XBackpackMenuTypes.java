package net.luis.xbackpack.world.inventory;

import net.luis.xbackpack.XBackpack;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * registration class, for the {@link XBackpack} {@link MenuType}s
 * 
 * @author Luis-st
 */

public class XBackpackMenuTypes {
	
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, XBackpack.MOD_ID);
	
	/**
	 * {@link RegistryObject} for {@link BackpackMenu}
	 */
	public static final RegistryObject<MenuType<BackpackMenu>> BACKPACK_MENU = MENU_TYPES.register("backpack_menu", () -> {
		return IForgeMenuType.create(BackpackMenu::new);
	});
	
}