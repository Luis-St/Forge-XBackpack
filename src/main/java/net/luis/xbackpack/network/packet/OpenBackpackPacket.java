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

package net.luis.xbackpack.network.packet;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.network.NetworkPacket;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-St
 *
 */

public class OpenBackpackPacket implements NetworkPacket {
	
	private static final Component CONTAINER_NAME = Component.translatable(XBackpack.MOD_ID + ".container.backpack");
	
	public OpenBackpackPacket() {}
	
	public OpenBackpackPacket(@NotNull FriendlyByteBuf buffer) {}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {}
	
	@Override
	public void handle(CustomPayloadEvent.@NotNull Context context) {
		ServerPlayer player = context.getSender();
		context.enqueueWork(() -> {
			assert player != null;
			if (player.containerMenu == player.inventoryMenu) {
				player.openMenu(new SimpleMenuProvider((id, inventory, playerIn) -> new BackpackMenu(id, inventory), CONTAINER_NAME));
			}
		});
	}
}
