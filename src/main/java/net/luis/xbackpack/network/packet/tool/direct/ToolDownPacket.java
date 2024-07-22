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

package net.luis.xbackpack.network.packet.tool.direct;

import net.luis.xbackpack.BackpackConstants;
import net.luis.xbackpack.network.NetworkPacket;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.capability.IBackpack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 *
 * @author Luis-St
 *
 */

public class ToolDownPacket implements NetworkPacket {
	
	public ToolDownPacket() {}
	
	public ToolDownPacket(@NotNull FriendlyByteBuf buffer) {}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {}
	
	@Override
	public void handle(CustomPayloadEvent.@NotNull Context context) {
		ServerPlayer player = context.getSender();
		context.enqueueWork(() -> {
			IBackpack backpack = BackpackProvider.get(Objects.requireNonNull(player));
			ItemStack main = player.getMainHandItem().copy();
			ItemStack down = backpack.getToolHandler().getStackInSlot(2).copy();
			if (BackpackConstants.VALID_TOOL_SLOT_ITEMS.contains(main.getItem())) {
				player.setItemInHand(InteractionHand.MAIN_HAND, down);
				backpack.getToolHandler().setStackInSlot(2, main);
			}
		});
	}
}
