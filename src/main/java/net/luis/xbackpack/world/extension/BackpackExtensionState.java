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

package net.luis.xbackpack.world.extension;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 *
 * @author Luis-St
 *
 */

public enum BackpackExtensionState {
	
	BLOCKED("blocked"),
	UNLOCKED("unlocked"),
	LOCKED("locked");
	
	private final String name;
	
	BackpackExtensionState(String name) {
		this.name = name;
	}
	
	@Nullable
	public static BackpackExtensionState fromString(String string) {
		for (BackpackExtensionState state : values()) {
			if (state.getName().equals(string)) {
				return state;
			}
		}
		return null;
	}
	
	@NotNull
	public static BackpackExtensionState fromString(String string, BackpackExtensionState fallbackState) {
		BackpackExtensionState state = fromString(string);
		return state != null ? state : fallbackState;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
