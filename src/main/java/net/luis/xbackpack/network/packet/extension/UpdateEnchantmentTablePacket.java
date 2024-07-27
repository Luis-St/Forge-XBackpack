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
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-St
 *
 */

public class UpdateEnchantmentTablePacket implements NetworkPacket {
	
	private final ResourceLocation[] enchantments;
	private final int[] enchantmentLevels;
	private final int[] enchantingCosts;
	private final int enchantmentSeed;
	
	public UpdateEnchantmentTablePacket(ResourceLocation @NotNull [] enchantments, int @NotNull [] enchantmentLevels, int @NotNull [] enchantingCosts, int enchantmentSeed) {
		this.enchantments = enchantments;
		this.enchantmentLevels = enchantmentLevels;
		this.enchantingCosts = enchantingCosts;
		this.enchantmentSeed = enchantmentSeed;
	}
	
	public UpdateEnchantmentTablePacket(@NotNull FriendlyByteBuf buffer) {
		this.enchantments = new ResourceLocation[buffer.readInt()];
		for (int i = 0; i < this.enchantments.length; i++) {
			this.enchantments[i] = buffer.readResourceLocation();
		}
		this.enchantmentLevels = buffer.readVarIntArray();
		this.enchantingCosts = buffer.readVarIntArray();
		this.enchantmentSeed = buffer.readInt();
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeInt(this.enchantments.length);
		for (ResourceLocation enchantment : this.enchantments) {
			buffer.writeResourceLocation(enchantment);
		}
		buffer.writeVarIntArray(this.enchantmentLevels);
		buffer.writeVarIntArray(this.enchantingCosts);
		buffer.writeInt(this.enchantmentSeed);
	}
	
	@Override
	public void handle(CustomPayloadEvent.@NotNull Context context) {
		context.enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateEnchantmentTableExtension(this.enchantments, this.enchantmentLevels, this.enchantingCosts, this.enchantmentSeed);
			});
		});
	}
}
