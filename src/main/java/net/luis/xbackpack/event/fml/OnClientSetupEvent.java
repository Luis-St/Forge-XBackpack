package net.luis.xbackpack.event.fml;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.client.screen.BackpackScreen;
import net.luis.xbackpack.init.XBackpackKeyMappings;
import net.luis.xbackpack.init.XBackpackMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = XBackpack.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class OnClientSetupEvent {
	
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(XBackpackMenuTypes.BACKPACK_MENU.get(), BackpackScreen::new);
		});
		ClientRegistry.registerKeyBinding(XBackpackKeyMappings.BACKPACK_OPEN);
		ClientRegistry.registerKeyBinding(XBackpackKeyMappings.BACKPACK_NEXT);
		ClientRegistry.registerKeyBinding(XBackpackKeyMappings.BACKPACK_SLOT_TOP);
		ClientRegistry.registerKeyBinding(XBackpackKeyMappings.BACKPACK_SLOT_DOWN);
	}

}
