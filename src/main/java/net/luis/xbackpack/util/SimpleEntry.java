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

package net.luis.xbackpack.util;

import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 *
 * @author Luis-St
 *
 */

public class SimpleEntry<K, V> implements Map.Entry<K, V> {
	
	private final K key;
	protected V value;
	
	public SimpleEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey() {
		return this.key;
	}
	
	@Nullable
	@Override
	public V getValue() {
		return this.value;
	}
	
	@Nullable
	@Override
	public V setValue(V value) {
		throw new ConcurrentModificationException("Unable to set value of entry to " + value + ", since the entry is muted");
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof SimpleEntry<?, ?> entry) {
			if (this.key.equals(entry.getKey())) {
				return Objects.equals(this.value, entry.getValue());
			} else {
				return false;
			}
		}
		return false;
	}
}
