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

package net.luis.xbackpack.world.capability;

import net.luis.xbackpack.world.backpack.BackpackHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-St
 *
 */

public class BackpackProvider implements ICapabilitySerializable<CompoundTag> {
	
	public static final Capability<IBackpack> BACKPACK = CapabilityManager.get(new CapabilityToken<>() {});
	
	private final BackpackHandler handler;
	private final LazyOptional<IBackpack> optional;
	
	public BackpackProvider(Player player) {
		this.handler = new BackpackHandler(player);
		this.optional = LazyOptional.of(() -> this.handler);
	}
	
	public static IBackpack get(@NotNull Player player) {
		return player.getCapability(BACKPACK, null).orElseThrow(NullPointerException::new);
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, Direction side) {
		return BACKPACK.orEmpty(capability, this.optional);
	}
	
	@Override
	public CompoundTag serializeNBT() {
		return this.handler.serialize();
	}
	
	@Override
	public void deserializeNBT(CompoundTag tag) {
		this.handler.deserialize(tag);
	}
}
