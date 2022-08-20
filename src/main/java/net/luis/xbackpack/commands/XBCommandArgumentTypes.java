package net.luis.xbackpack.commands;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.server.commands.arguments.BackpackExtensionArgument;
import net.luis.xbackpack.server.commands.arguments.ExtensionStateArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 *
 * @author Luis-st
 *
 */

public class XBCommandArgumentTypes {
	
	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, XBackpack.MOD_ID);
	
	public static final RegistryObject<BackpackExtensionArgument.Info> BACKPACK_EXTENSION_TYPE = COMMAND_ARGUMENT_TYPES.register("backpack_extension_type", () -> {
		return ArgumentTypeInfos.registerByClass(BackpackExtensionArgument.class, new BackpackExtensionArgument.Info());
	});
	public static final RegistryObject<ExtensionStateArgument.Info> EXTENSION_STATE_TYPE = COMMAND_ARGUMENT_TYPES.register("extension_state_type", () -> {
		return ArgumentTypeInfos.registerByClass(ExtensionStateArgument.class, new ExtensionStateArgument.Info());
	});
	
}