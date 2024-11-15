/*
 * XBackpack
 * Copyright (C) 2024 Luis Staudt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.luis.xbackpack.commands;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.server.commands.arguments.BackpackExtensionArgument;
import net.luis.xbackpack.server.commands.arguments.BackpackExtensionStateArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraftforge.registries.*;

/**
 *
 * @author Luis-St
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
