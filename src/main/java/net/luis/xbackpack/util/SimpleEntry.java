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
			if (!this.key.equals(entry.getKey())) {
				return false;
			} else {
				return Objects.equals(this.value, entry.getValue());
			}
		}
		return false;
	}
}
