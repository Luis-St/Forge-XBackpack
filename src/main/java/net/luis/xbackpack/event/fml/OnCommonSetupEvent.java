package net.luis.xbackpack.event.fml;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.server.commands.arguments.BackpackExtensionArgument;
import net.luis.xbackpack.server.commands.arguments.ExtensionStateArgument;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 *
 * @author Luis-st
 *
 */

@EventBusSubscriber(modid = XBackpack.MOD_ID, bus = Bus.MOD)
public class OnCommonSetupEvent {
	
	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		ArgumentTypes.register(XBackpack.MOD_ID + ":backpack_extension", BackpackExtensionArgument.class, new EmptyArgumentSerializer<>(BackpackExtensionArgument::extension));
		ArgumentTypes.register(XBackpack.MOD_ID + ":extension_state", ExtensionStateArgument.class, new EmptyArgumentSerializer<>(ExtensionStateArgument::state));
	}
	
}
