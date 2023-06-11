package net.luis.xbackpack.event.registry;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.server.commands.BackpackCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-st
 *
 */

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID)
public class RegisterEventHandler {
	
	@SubscribeEvent
	public static void registerCommands(@NotNull RegisterCommandsEvent event) {
		BackpackCommand.register(event.getDispatcher(), event.getBuildContext());
	}
}
