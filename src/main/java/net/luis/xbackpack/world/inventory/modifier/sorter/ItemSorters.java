package net.luis.xbackpack.world.inventory.modifier.sorter;

import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

/**
 *
 * @author Luis-st
 *
 */

public enum ItemSorters implements ItemSorter {
	
	NONE("none", 0) {
		@Override
		public List<ItemStack> sort(List<ItemStack> stacks, String searchTerm) {
			return stacks;
		}
	},
	NAME_SEARCH("name_search", 1, false) {
		@Override
		public List<ItemStack> sort(List<ItemStack> stacks, String searchTerm) {
			if (!searchTerm.isEmpty()) {
				List<ItemStack> returnList = Lists.newArrayList();
				List<ItemStack> equalsList = Lists.newArrayList();
				List<ItemStack> startsList = Lists.newArrayList();
				List<ItemStack> containsList = Lists.newArrayList();
				Iterator<ItemStack> iterator = stacks.iterator();
				while (iterator.hasNext()) {
					ItemStack stack = iterator.next();
					String name = stack.getDisplayName().getString().replace("[", "").replace("]", "").trim().toLowerCase();
					if (name.equals(searchTerm)) {
						equalsList.add(stack);
						iterator.remove();
					} else if (name.startsWith(searchTerm)) {
						startsList.add(stack);
						iterator.remove();
					} else if (name.contains(searchTerm)) {
						containsList.add(stack);
						iterator.remove();
					} else {
						XBackpack.LOGGER.error("The filtered item list contains a item with name {} which does not match in any form with the search term '{}'", name, searchTerm);
					}
				}
				returnList.addAll(ItemSorters.ALPHABETICALLY.sort(equalsList, searchTerm));
				returnList.addAll(ItemSorters.ALPHABETICALLY.sort(startsList, searchTerm));
				returnList.addAll(ItemSorters.ALPHABETICALLY.sort(containsList, searchTerm));
				return returnList;
			}
			return stacks;
		}
	},
	COUNT_SEARCH("count_search", 2, false) {
		@Override
		public List<ItemStack> sort(List<ItemStack> stacks, String searchTerm) {
			if (!searchTerm.isEmpty()) {
				int count = Util.tryParseInteger(searchTerm, -1);
				if (count == -1) {
					XBackpack.LOGGER.error("There was a problem sorting the items by the stack size, the search term is invalid");
				}
				return ItemSorters.ALPHABETICALLY.sort(stacks, searchTerm);
			}
			return stacks;
		}
		
		@Override
		public List<MutableComponent> getInfo() {
			List<MutableComponent> infoTooltip = Lists.newArrayList();
			infoTooltip.add(Component.translatable(this.getInfoDescriptorId() + "0"));
			return infoTooltip;
		}
	},
	ALPHABETICALLY("alphabetically", 3) {
		@Override
		public List<ItemStack> sort(List<ItemStack> stacks, String searchTerm) {
			stacks.sort((firstStack, secondStack) -> {
				return firstStack.getDisplayName().getString().toLowerCase().compareTo(secondStack.getDisplayName().getString().toLowerCase());
			});
			return stacks;
		}
		
		@Override
		public List<MutableComponent> getInfo() {
			List<MutableComponent> infoTooltip = Lists.newArrayList();
			infoTooltip.add(Component.translatable(this.getInfoDescriptorId() + "0"));
			return infoTooltip;
		}
	},
	COUNT_DOWNWARDS("count_downwards", 4) {
		@Override
		public List<ItemStack> sort(List<ItemStack> stacks, String searchTerm) {
			stacks.sort((firstStack, secondStack) -> {
				if (secondStack.getCount() > firstStack.getCount()) {
					return 1;
				} else if (firstStack.getCount() == secondStack.getCount()) {
					return 0;
				} else {
					return -1;
				}
			});
			return stacks;
		}
	},
	COUNT_UPWARDS("count_upwards", 5) {
		@Override
		public List<ItemStack> sort(List<ItemStack> stacks, String searchTerm) {
			stacks.sort((firstStack, secondStack) -> {
				if (secondStack.getCount() > firstStack.getCount()) {
					return -1;
				} else if (firstStack.getCount() == secondStack.getCount()) {
					return 0;
				} else {
					return 1;
				}
			});
			return stacks;
		}
	};
	
	private final String name;
	private final int id;
	private final boolean selectable;
	
	private ItemSorters(String name, int id) {
		this(name, id, true);
	}

	private ItemSorters(String name, int id, boolean selectable) {
		this.name = name;
		this.id = id;
		this.selectable = selectable;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public boolean isSelectable() {
		return this.selectable;
	}
	
	@Override
	public abstract List<ItemStack> sort(List<ItemStack> stacks, String searchTerm);
	
	@Override
	public MutableComponent getDisplayName() {
		return Component.translatable("xbackpack.backpack_action.sorter." + this.name);
	}
	
	protected String getInfoDescriptorId() {
		return "xbackpack.backpack_action.sorter." + this.name + ".info.";
	}
	
	@Override
	public List<MutableComponent> getInfo() {
		List<MutableComponent> infoTooltip = Lists.newArrayList();
		infoTooltip.add(Component.translatable(this.getInfoDescriptorId() + "0"));
		infoTooltip.add(Component.translatable(this.getInfoDescriptorId() + "1"));
		return infoTooltip;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Nullable
	public static ItemSorter byId(int id) {
		return byId(id, null);
	}
	
	@NotNull
	public static ItemSorter byId(int id, ItemSorter fallback) {
		for (ItemSorter sorter : ItemSorters.values()) {
			if (sorter.getId() == id) {
				return sorter;
			}
		}
		return fallback;
	}
	
}
