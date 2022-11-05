package net.luis.xbackpack.world.inventory.modifier.filter;

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import net.luis.xbackpack.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Wearable;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

/**
 *
 * @author Luis-st
 *
 */

public enum ItemFilters implements ItemFilter {
	
	NONE("none", 0) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			return true;
		}
		
		@Override
		public List<MutableComponent> getInfo() {
			List<MutableComponent> infoTooltip = Lists.newArrayList();
			infoTooltip.add(Component.translatable(this.getInfoDescriptorId() + "0"));
			infoTooltip.add(Component.translatable(this.getInfoDescriptorId() + "1"));
			return infoTooltip;
		}
	},
	NAME_SEARCH("name_search", 1, false) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			String itemName = stack.getDisplayName().getString().replace("[", "").replace("]", "").trim().toLowerCase();
			if (searchTerm.isEmpty()) {
				return true;
			} else if (itemName.equals(searchTerm)) {
				return true;
			} else if (itemName.contains(searchTerm)) {
				return true;
			} else if (itemName.startsWith(searchTerm)) {
				return true;
			} else {
				return false;
			}
		}
	},
	NAMESPACE_SEARCH("namespace_search", 2, false) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			String namespace = ForgeRegistries.ITEMS.getKey(stack.getItem()).getNamespace().trim().toLowerCase();
			if (searchTerm.isEmpty()) {
				return true;
			} else if (!searchTerm.startsWith("@")) {
				return false;
			} else {
				String string = searchTerm.substring(1);
				if (string.isEmpty()) {
					return true;
				} else if (namespace.equals(string)) {
					return true;
				} else if (namespace.contains(string)) {
					return true;
				} else if (namespace.startsWith(string)) {
					return true;
				} else {
					return false;
				}
			}
		}
	},
	TAG_SEARCH("tag_search", 3, false) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			if (searchTerm.isEmpty()) {
				return true;
			} else if (!searchTerm.startsWith("#")) {
				return false;
			} else {
				String string = searchTerm.substring(1);
				if (string.isEmpty()) {
					return true;
				} else {
					TagKey<Item> tag = this.getTag(string);
					if (tag == null) {
						return false;
					} else {
						return ForgeRegistries.ITEMS.tags().getTag(tag).contains(stack.getItem());
					}
				}
			}
		}
		
		@Nullable
		private TagKey<Item> getTag(String searchTerm) {
			List<TagKey<Item>> tags = ForgeRegistries.ITEMS.tags().stream().filter(ITag::isBound).filter((tag) -> {
				return !tag.isEmpty();
			}).map(ITag::getKey).collect(Collectors.toList());
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
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			int count = Util.tryParseInteger(searchTerm, -1);
			if (searchTerm.isEmpty()) {
				return true;
			} else if (0 >= count) {
				return false;
			} else if (count > 64) {
				return false;
			} else {
				return stack.getCount() == count;
			}
		}
	},
	STACKABLE("stackable", 5) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			return stack.isStackable();
		}
	},
	NONE_STACKABLE("none_stackable", 6) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			return !stack.isStackable();
		}
	},
	MAX_COUNT("max_count", 7) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			return stack.getItem().getMaxStackSize(stack) == stack.getCount();
		}
	},
	ENCHANTABLE("enchantable", 8) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			return stack.isEnchantable();
		}
	},
	ENCHANTED("enchanted", 9) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			return stack.isEnchanted();
		}
	},
	DAMAGEABLE("damageable", 10) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			return stack.isDamageableItem();
		}
	},
	DAMAGED("damaged", 11) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			return stack.isDamaged();
		}
	},
	EDIBLE("edible", 12) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			return stack.isEdible();
		}
	},
	WEAPON("weapon", 13) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			Item item = stack.getItem();
			return item instanceof SwordItem || item instanceof BowItem || item instanceof CrossbowItem;
		}
	},
	TOOL("tool", 14) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			Item item = stack.getItem();
			return item instanceof DiggerItem || item instanceof FishingRodItem || item instanceof FlintAndSteelItem || item instanceof CompassItem || item == Items.CLOCK;
		}
	},
	ARMOR("armor", 15) {
		@Override
		public boolean canKeepItem(ItemStack stack, String searchTerm) {
			return stack.getItem() instanceof Wearable;
		}
	};
	
	private final String name;
	private final int id;
	private final boolean selectable;
	
	private ItemFilters(String name, int id) {
		this(name, id, true);
	}
	
	private ItemFilters(String name, int id, boolean selectable) {
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
	public abstract boolean canKeepItem(ItemStack stack, String searchTerm);
	
	@Override
	public MutableComponent getDisplayName() {
		return Component.translatable("xbackpack.backpack_action.filter." + this.name);
	}
	
	protected String getInfoDescriptorId() {
		return "xbackpack.backpack_action.filter." + this.name + ".info.";
	}
	
	@Override
	public List<MutableComponent> getInfo() {
		List<MutableComponent> infoTooltip = Lists.newArrayList();
		infoTooltip.add(Component.translatable(this.getInfoDescriptorId() + "0"));
		return infoTooltip;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Nullable
	public static ItemFilter byId(int id) {
		return byId(id, null);
	}
	
	@NotNull
	public static ItemFilter byId(int id, ItemFilter fallback) {
		for (ItemFilter filter : ItemFilters.values()) {
			if (filter.getId() == id) {
				return filter;
			}
		}
		return fallback;
	}
	
}
