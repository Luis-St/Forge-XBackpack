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

package net.luis.xbackpack.world.inventory.handler;

import net.luis.xbackpack.world.item.DynamicItemStackHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-St
 *
 */

public class CraftingHandler {
	
	private final ItemStackHandler inputHandler;
	private final ItemStackHandler resultHandler;
	
	public CraftingHandler(@NotNull DynamicItemStackHandler inputHandler) {
		this(inputHandler, new DynamicItemStackHandler(1));
	}
	
	public CraftingHandler(int input, int result) {
		this(new DynamicItemStackHandler(input), new DynamicItemStackHandler(result));
	}
	
	public CraftingHandler(@NotNull DynamicItemStackHandler inputHandler, @NotNull DynamicItemStackHandler resultHandler) {
		this.inputHandler = inputHandler;
		this.resultHandler = resultHandler;
	}
	
	public @NotNull ItemStackHandler getInputHandler() {
		return this.inputHandler;
	}
	
	public @NotNull ItemStackHandler getResultHandler() {
		return this.resultHandler;
	}
	
	public @NotNull CompoundTag serialize(HolderLookup.@NotNull Provider provider) {
		CompoundTag tag = new CompoundTag();
		tag.put("input_handler", this.inputHandler.serializeNBT(provider));
		tag.put("result_handler", this.resultHandler.serializeNBT(provider));
		return tag;
	}
	
	public void deserialize(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag tag) {
		this.inputHandler.deserializeNBT(provider, tag.getCompound("input_handler"));
		this.resultHandler.deserializeNBT(provider, tag.getCompound("result_handler"));
	}
}
