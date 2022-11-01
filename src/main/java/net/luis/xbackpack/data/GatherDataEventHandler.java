package net.luis.xbackpack.data;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.data.provider.language.XBLanguageProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 *
 * @author Luis-st
 *
 */

@EventBusSubscriber(modid = XBackpack.MOD_ID, bus = Bus.MOD)
public class GatherDataEventHandler {
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		if (event.includeDev()) {
			generator.addProvider(event.includeClient(), new XBLanguageProvider(generator));
		}
	}
	
}
