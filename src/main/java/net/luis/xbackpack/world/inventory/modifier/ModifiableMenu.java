package net.luis.xbackpack.world.inventory.modifier;

import net.luis.xbackpack.world.inventory.modifier.filter.ItemFilter;
import net.luis.xbackpack.world.inventory.modifier.sorter.ItemSorter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 *
 * @author Luis-st
 *
 */

public interface ModifiableMenu {
	
	String getSearchTerm();
	
	void setSearchTerm(String searchTerm);
	
	ItemFilter getFilter();
	
	void updateFilter(@Nullable ItemFilter filter, UpdateType type, @Nullable CycleDirection direction);
	
	ItemSorter getSorter();
	
	void updateSorter(@Nullable ItemSorter sorter, UpdateType type, @Nullable CycleDirection direction);
	
	enum UpdateType {
		
		NONE("none", 0, false),
		SET("set", 1, true),
		SET_NO_UPDATE("set_no_update", 2, false),
		RESET("reset", 3, true),
		RESET_NO_UPDATE("reset_no_update", 4, false),
		CYCLE("cycle", 5, true),
		CYCLE_NO_UPDATE("cycle_no_update", 6, false);
		
		private final String name;
		private final int id;
		private final boolean shouldUpdate;
		
		UpdateType(String name, int id, boolean shouldUpdate) {
			this.name = name;
			this.id = id;
			this.shouldUpdate = shouldUpdate;
		}
		
		public String getName() {
			return this.name;
		}
		
		public int getId() {
			return this.id;
		}
		
		public boolean shouldSet() {
			return this == SET || this == SET_NO_UPDATE;
		}
		
		public boolean shouldReset() {
			return this == RESET || this == RESET_NO_UPDATE;
		}
		
		public boolean shouldCycle() {
			return this == CYCLE || this == CYCLE_NO_UPDATE;
		}
		
		public boolean shouldUpdate() {
			return this.shouldUpdate;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
	
	enum CycleDirection {
		
		FORWARDS("forwards", 0) {
			@Override
			public <T extends ItemModifier> T cycle(List<T> itemModifiers, T itemModifier) {
				return itemModifiers.get((itemModifiers.indexOf(itemModifier) + 1) % itemModifiers.size());
			}
		},
		BACKWARDS("backwards", 1) {
			@Override
			public <T extends ItemModifier> T cycle(List<T> itemModifiers, T itemModifier) {
				int index = itemModifiers.indexOf(itemModifier);
				if (index == 0) {
					return itemModifiers.get(itemModifiers.size() - 1);
				} else {
					return itemModifiers.get(index - 1);
				}
			}
		};
		
		private final String name;
		private final int id;
		
		CycleDirection(String name, int id) {
			this.name = name;
			this.id = id;
		}
		
		public String getName() {
			return this.name;
		}
		
		public int getId() {
			return this.id;
		}
		
		public abstract <T extends ItemModifier> T cycle(List<T> itemModifiers, T itemModifier);
		
		@Override
		public String toString() {
			return this.name;
		}
	}
}
