package net.luis.xbackpack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.XBMenuTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * 
 * @author Luis-st
 *
 */

@Mod(XBackpack.MOD_ID)
public class XBackpack {
	
	public static final String MOD_ID = "xbackpack";
	public static final String MOD_NAME = "XBackpack";
	public static final Logger LOGGER = LogManager.getLogger(XBackpack.class);
	
	public XBackpack() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		XBMenuTypes.MENU_TYPES.register(modEventBus);
		BackpackExtension.BACKPACK_EXTENSIONS.register(modEventBus);
		XBNetworkHandler.register();
	}
	
}
