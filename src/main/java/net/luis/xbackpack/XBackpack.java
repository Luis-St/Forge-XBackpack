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

package net.luis.xbackpack;

import net.luis.xbackpack.commands.XBCommandArgumentTypes;
import net.luis.xbackpack.core.components.XBDataComponents;
import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.XBMenuTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Luis-St
 *
 */

@Mod(XBackpack.MOD_ID)
public class XBackpack {
	
	public static final String MOD_ID = "xbackpack";
	public static final String MOD_NAME = "XBackpack";
	public static final Logger LOGGER = LogManager.getLogger(XBackpack.class);
	
	public XBackpack() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		XBMenuTypes.MENU_TYPES.register(modEventBus);
		BackpackExtensions.BACKPACK_EXTENSIONS.register(modEventBus);
		XBCommandArgumentTypes.COMMAND_ARGUMENT_TYPES.register(modEventBus);
		XBDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
		XBNetworkHandler.INSTANCE.initChannel();
		XBNetworkHandler.INSTANCE.registerPackets();
	}
}
