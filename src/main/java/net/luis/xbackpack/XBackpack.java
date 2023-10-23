package net.luis.xbackpack;

import net.luis.xbackpack.commands.XBCommandArgumentTypes;
import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.XBMenuTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Luis-St
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
		BackpackExtensions.BACKPACK_EXTENSIONS.register(modEventBus);
		XBCommandArgumentTypes.COMMAND_ARGUMENT_TYPES.register(modEventBus);
		XBNetworkHandler.INSTANCE.initChannel();
		XBNetworkHandler.INSTANCE.registerPackets();
	}
}
