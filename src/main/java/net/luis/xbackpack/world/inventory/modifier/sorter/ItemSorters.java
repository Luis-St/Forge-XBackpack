package net.luis.xbackpack.world.inventory.modifier.sorter;

import com.google.common.collect.Lists;
import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
						XBackpack.LOGGER.error("[Filter Failed] The filtered item list contains an item with the name '{}' that does not match the search term '{}' in any form", name, searchTerm);
					}
				}
				returnList.addAll(ItemSorters.ALPHABETICALLY.sort(equalsList, searchTerm));
				returnList.addAll(ItemSorters.ALPHABETICALLY.sort(startsList, searchTerm));
				returnList.addAll(ItemSorters.ALPHABETICALLY.sort(containsList, searchTerm));
				return returnList;
			}
			XBackpack.LOGGER.info("An attempt is made to apply a search term sorter to an empty search term");
			return stacks;
		}
	},
	NAMESPACE_SEARCH("namespace_search", 2, false) {
		@Override
		public List<ItemStack> sort(List<ItemStack> stacks, String searchTerm) {
			if (searchTerm.startsWith("@")) {
				String string = searchTerm.substring(1);
				if (!string.isEmpty()) {
					List<ItemStack> equalsList = Lists.newArrayList();
					List<ItemStack> startsList = Lists.newArrayList();
					List<ItemStack> containsList = Lists.newArrayList();
					Iterator<ItemStack> iterator = stacks.iterator();
					while (iterator.hasNext()) {
						ItemStack stack = iterator.next();
						String namespace = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).getNamespace();
						if (namespace.equals(string)) {
							equalsList.add(stack);
							iterator.remove();
						} else if (namespace.startsWith(string)) {
							startsList.add(stack);
							iterator.remove();
						} else if (namespace.contains(string)) {
							containsList.add(stack);
							iterator.remove();
						} else {
							XBackpack.LOGGER.error("[Filter Failed] The filtered item list contains an item with the namespace '{}' that does not match the search term '{}' in any form", namespace, string);
						}
					}
					equalsList.sort(Comparator.comparing(stack -> Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).getNamespace()));
					startsList.sort(Comparator.comparing(stack -> Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).getNamespace()));
					containsList.sort(Comparator.comparing(stack -> Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).getNamespace()));
					return Stream.of(equalsList, startsList, containsList).flatMap(List::stream).collect(Collectors.toList());
				}
				return stacks;
			}
			XBackpack.LOGGER.info("An attempt is made to apply a search term sorter to an empty search term");
			return stacks;
		}
	},
	TAG_SEARCH("tag_search", 3, false) {
		@Override
		public List<ItemStack> sort(List<ItemStack> stacks, String searchTerm) {
			if (searchTerm.startsWith("#")) {
				TagKey<Item> tag = this.getTag(searchTerm);
				if (tag != null) {
					List<ItemStack> returnList = Lists.newArrayList();
					for (ItemStack stack : stacks) {
						if (Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(tag).contains(stack.getItem())) {
							returnList.add(stack);
						} else {
							XBackpack.LOGGER.error("[Filter Failed] The filtered item list contains an item '{}' which is not subordinated to the tag '{}'", stack.getItem(), tag.location());
						}
					}
					return ItemSorters.ALPHABETICALLY.sort(returnList, searchTerm);
				}
				return stacks;
			}
			XBackpack.LOGGER.info("An attempt is made to apply a search term sorter to an empty search term");
			return stacks;
		}
		
		@Nullable
		private TagKey<Item> getTag(String searchTerm) {
			List<TagKey<Item>> tags = Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).stream().filter(ITag::isBound).filter((tag) -> !tag.isEmpty()).map(ITag::getKey).toList();
			for (TagKey<Item> tag : tags) {
				if (searchTerm.replace(" ", "_").equals(tag.location().getPath())) {
					return tag;
				}
			}
			return null;
		}
	},
	COUNT_SEARCH("count_search", 4, false) {
		@Override
		public List<ItemStack> sort(List<ItemStack> stacks, String searchTerm) {
			if (!searchTerm.isEmpty()) {
				int count = Util.tryParseInteger(searchTerm, -1);
				if (count == -1) {
					XBackpack.LOGGER.error("There was a problem sorting the items by the stack size, the search term is invalid");
				}
				return ItemSorters.ALPHABETICALLY.sort(stacks, searchTerm);
			}
			XBackpack.LOGGER.info("An attempt is made to apply a search term sorter to an empty search term");
			return stacks;
		}
		
		@Override
		public List<MutableComponent> getInfo() {
			List<MutableComponent> infoTooltip = Lists.newArrayList();
			infoTooltip.add(Component.translatable(this.getInfoDescriptorId() + "0"));
			return infoTooltip;
		}
	},
	ALPHABETICALLY("alphabetically", 5) {
		@Override
		public List<ItemStack> sort(List<ItemStack> stacks, String searchTerm) {
			stacks.sort(Comparator.comparing(stack -> stack.getDisplayName().getString().toLowerCase()));
			return stacks;
		}
		
		@Override
		public List<MutableComponent> getInfo() {
			List<MutableComponent> infoTooltip = Lists.newArrayList();
			infoTooltip.add(Component.translatable(this.getInfoDescriptorId() + "0"));
			return infoTooltip;
		}
	},
	COUNT_DOWNWARDS("count_downwards", 6) {
		@Override
		public List<ItemStack> sort(List<ItemStack> stacks, String searchTerm) {
			stacks.sort((firstStack, secondStack) -> Integer.compare(secondStack.getCount(), firstStack.getCount()));
			return stacks;
		}
	},
	COUNT_UPWARDS("count_upwards", 7) {
		@Override
		public List<ItemStack> sort(List<ItemStack> stacks, String searchTerm) {
			stacks.sort(Comparator.comparingInt(ItemStack::getCount));
			return stacks;
		}
	};
	
	private final String name;
	private final int id;
	private final boolean selectable;
	
	ItemSorters(String name, int id) {
		this(name, id, true);
	}
	
	ItemSorters(String name, int id, boolean selectable) {
		this.name = name;
		this.id = id;
		this.selectable = selectable;
	}
	
	public static @NotNull ItemSorter byId(int id) {
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
	
}
