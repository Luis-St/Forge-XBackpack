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

package net.luis.xbackpack.network.packet.modifier;

import net.luis.xbackpack.network.NetworkPacket;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.modifier.ItemModifierType;
import net.luis.xbackpack.world.inventory.modifier.ModifiableMenu.UpdateType;
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

public class ResetItemModifierPacket implements NetworkPacket {
	
	private final ItemModifierType modifierType;
	
	public ResetItemModifierPacket(@NotNull ItemModifierType modifierType) {
		this.modifierType = modifierType;
	}
	
	public ResetItemModifierPacket(@NotNull FriendlyByteBuf buffer) {
		this.modifierType = ItemModifierType.byId(buffer.readInt());
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeInt(this.modifierType.getId());
	}
	
	@Override
	public void handle(CustomPayloadEvent.@NotNull Context context) {
		ServerPlayer player = Objects.requireNonNull(context.getSender());
		context.enqueueWork(() -> {
			if (player.containerMenu instanceof BackpackMenu menu) {
				if (this.modifierType == ItemModifierType.FILTER) {
					menu.updateFilter(null, UpdateType.RESET, null);
				} else if (this.modifierType == ItemModifierType.SORTER) {
					menu.updateSorter(null, UpdateType.RESET, null);
				}
			}
		});
	}
}
