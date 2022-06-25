package net.luis.xbackpack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.luis.xbackpack.world.inventory.XBackpackMenuTypes;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

/**
 * 
 * @author Luis-st
 *
 */

@Mod(XBackpack.MOD_ID)
public class XBackpack {
	
	/**
	 * {@link XBackpack} mod id
	 */
	public static final String MOD_ID = "xbackpack";
	
	/**
	 * {@link XBackpack} mod name
	 */
	public static final String MOD_NAME = "XBackpack";
	
	/**
	 * {@link XBackpack} internal {@link Logger}
	 */
	public static final Logger LOGGER = LogManager.getLogger(XBackpack.class);
	
	/**
	 * constructor for the main mod class,
	 * register all {@link DeferredRegister}
	 */
	public XBackpack() {
		XBackpackMenuTypes.MENU_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
}
