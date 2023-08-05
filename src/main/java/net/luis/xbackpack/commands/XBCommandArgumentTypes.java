package net.luis.xbackpack.commands;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.server.commands.arguments.BackpackExtensionArgument;
import net.luis.xbackpack.server.commands.arguments.BackpackExtensionStateArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraftforge.registries.*;

/**
 *
 * @author Luis-st
 *
 */

public class XBCommandArgumentTypes {
	
	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, XBackpack.MOD_ID);
	
	public static final RegistryObject<BackpackExtensionArgument.Info> BACKPACK_EXTENSION_TYPE = COMMAND_ARGUMENT_TYPES.register("backpack_extension_type", () -> {
		return ArgumentTypeInfos.registerByClass(BackpackExtensionArgument.class, new BackpackExtensionArgument.Info());
	});
	public static final RegistryObject<BackpackExtensionStateArgument.Info> BACKPACK_EXTENSION_STATE_TYPE = COMMAND_ARGUMENT_TYPES.register("backpack_extension_state_type", () -> {
		return ArgumentTypeInfos.registerByClass(BackpackExtensionStateArgument.class, new BackpackExtensionStateArgument.Info());
	});
}
