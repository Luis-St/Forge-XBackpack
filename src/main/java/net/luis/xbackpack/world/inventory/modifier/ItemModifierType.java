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

package net.luis.xbackpack.world.inventory.modifier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Luis-St
 *
 */

public enum ItemModifierType {
	
	FILTER("filter", 0),
	SORTER("sorter", 1);
	
	private final String name;
	private final int id;
	
	ItemModifierType(@NotNull String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public static @Nullable ItemModifierType byId(int id) {
		if (id == FILTER.getId()) {
			return FILTER;
		}
		return id == SORTER.getId() ? SORTER : null;
	}
	
	public @NotNull String getName() {
		return this.name;
	}
	
	public int getId() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
