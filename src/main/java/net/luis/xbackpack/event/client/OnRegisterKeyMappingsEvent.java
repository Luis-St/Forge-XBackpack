package net.luis.xbackpack.event.client;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.client.XBKeyMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * 
 * @author Luis-st
 *
 */

@EventBusSubscriber(modid = XBackpack.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class OnRegisterKeyMappingsEvent {
	
	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(XBKeyMappings.BACKPACK_OPEN);
		event.register(XBKeyMappings.BACKPACK_NEXT);
		event.register(XBKeyMappings.BACKPACK_SLOT_TOP);
		event.register(XBKeyMappings.BACKPACK_SLOT_MID);
		event.register(XBKeyMappings.BACKPACK_SLOT_DOWN);
	}
	
}
