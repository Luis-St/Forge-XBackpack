package net.luis.xbackpack.event.fml;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.network.XBackpackNetworkHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * 
 * @author Luis-st
 *
 */

@EventBusSubscriber(modid = XBackpack.MOD_ID, bus = Bus.MOD)
public class OnCommonSetupEvent {
	
	/**
	 * register the common stuff of {@link XBackpack}:<br>
	 * <ul>
	 * 	<li>the {@link SimpleChannel}</li>
	 * 	<li>the network packets</li>
	 * </ul>
	 */
	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		XBackpackNetworkHandler.init();
	}
	
}
