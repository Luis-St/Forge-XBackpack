package net.luis.xbackpack.event.fml;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.client.XBKeyMappings;
import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.world.inventory.XBMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * 
 * @author Luis-st
 *
 */

@EventBusSubscriber(modid = XBackpack.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class OnClientSetupEvent {
	
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(XBMenuTypes.BACKPACK_MENU.get(), BackpackScreen::new);
		});
		ClientRegistry.registerKeyBinding(null);
		ClientRegistry.registerKeyBinding(XBKeyMappings.BACKPACK_OPEN);
		ClientRegistry.registerKeyBinding(XBKeyMappings.BACKPACK_NEXT);
		ClientRegistry.registerKeyBinding(XBKeyMappings.BACKPACK_SLOT_TOP);
		ClientRegistry.registerKeyBinding(XBKeyMappings.BACKPACK_SLOT_MID);
		ClientRegistry.registerKeyBinding(XBKeyMappings.BACKPACK_SLOT_DOWN);
	}
	
}
