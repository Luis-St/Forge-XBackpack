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

package net.luis.xbackpack.event.registry;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.server.commands.BackpackCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-St
 *
 */

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID)
public class RegisterEventHandler {
	
	@SubscribeEvent
	public static void registerCommands(@NotNull RegisterCommandsEvent event) {
		BackpackCommand.register(event.getDispatcher(), event.getBuildContext());
	}
}
