package net.luis.xbackpack.event.fml;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.world.inventory.XBMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-st
 *
 */

@EventBusSubscriber(modid = XBackpack.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientSetupEventHandler {
	
	@SubscribeEvent
	public static void clientSetup(@NotNull FMLClientSetupEvent event) {
		event.enqueueWork(() -> MenuScreens.register(XBMenuTypes.BACKPACK_MENU.get(), BackpackScreen::new));
	}
	
}
