package net.luis.xbackpack.event.registry;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.server.commands.BackpackCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 *
 * @author Luis-st
 *
 */

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID)
public class OnRegisterCommandsEvent {
	
	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		BackpackCommand.register(event.getDispatcher());
	}

}
