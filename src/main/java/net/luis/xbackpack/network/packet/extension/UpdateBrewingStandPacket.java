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

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.luis.xbackpack.network.NetworkPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

public class UpdateBrewingStandPacket implements NetworkPacket {
	
	private final int fuel;
	private final int brewTime;
	
	public UpdateBrewingStandPacket(int fuel, int brewTime) {
		this.fuel = fuel;
		this.brewTime = brewTime;
	}
	
	public UpdateBrewingStandPacket(@NotNull FriendlyByteBuf buffer) {
		this.fuel = buffer.readInt();
		this.brewTime = buffer.readInt();
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeInt(this.fuel);
		buffer.writeInt(this.brewTime);
	}
	
	@Override
	public void handle(CustomPayloadEvent.@NotNull Context context) {
		context.enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateBrewingStandExtension(this.fuel, this.brewTime);
			});
		});
	}
}
