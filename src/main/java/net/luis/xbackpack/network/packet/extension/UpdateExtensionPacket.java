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

package net.luis.xbackpack.network.packet.extension;

import net.luis.xbackpack.network.NetworkPacket;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 *
 * @author Luis-St
 *
 */

public class UpdateExtensionPacket implements NetworkPacket {
	
	private final BackpackExtension extension;
	
	public UpdateExtensionPacket(@NotNull BackpackExtension extension) {
		this.extension = extension;
	}
	
	public UpdateExtensionPacket(@NotNull FriendlyByteBuf buffer) {
		this.extension = BackpackExtensions.REGISTRY.get().getValue(buffer.readResourceLocation());
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(Objects.requireNonNull(BackpackExtensions.REGISTRY.get().getKey(this.extension)));
	}
	
	@Override
	public void handle(CustomPayloadEvent.@NotNull Context context) {
		ServerPlayer player = Objects.requireNonNull(context.getSender());
		context.enqueueWork(() -> {
			if (player.containerMenu instanceof BackpackMenu menu) {
				menu.setExtension(this.extension);
			}
		});
	}
}
