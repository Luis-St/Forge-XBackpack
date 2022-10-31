package net.luis.xbackpack.world.inventory.modifier;

import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Luis-st
 *
 */

public enum ItemModifierType {
	
	FILTER("filter", 0),
	SORTER("sorter", 1);
	
	private final String name;
	private final int id;
	
	private ItemModifierType(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}

	public int getId() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Nullable
	public static ItemModifierType byId(int id) {
		if (id == FILTER.getId()) {
			return FILTER;
		}
		return id == SORTER.getId() ? SORTER : null;
	}
	
}
