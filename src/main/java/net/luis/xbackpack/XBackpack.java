package net.luis.xbackpack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.luis.xbackpack.init.XBackpackMenuTypes;
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
	public static final Logger LOGGER = LogManager.getLogger(XBackpack.class); // internal Logger create your own Logger
	
	public XBackpack() {
		XBackpackMenuTypes.MENU_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
}
