package net.luis.xbackpack.world.inventory.modifier.filter;

import com.google.common.collect.Lists;
import net.luis.xbackpack.util.Util;
import net.luis.xbackpack.world.item.CustomBackpackFilterItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 *
 * @author Luis-st
 *
 */

public enum ItemFilters implements ItemFilter {
	
	NONE("none", 0) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
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
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			String itemName = stack.getDisplayName().getString().replace("[", "").replace("]", "").trim().toLowerCase();
			if (searchTerm.isEmpty()) {
				return true;
			} else if (itemName.equals(searchTerm)) {
				return true;
			} else if (itemName.contains(searchTerm)) {
				return true;
			}
			return itemName.startsWith(searchTerm);
		}
	},
	NAMESPACE_SEARCH("namespace_search", 2, false) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			String namespace = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).getNamespace().trim().toLowerCase();
			if (searchTerm.isEmpty()) {
				return true;
			} else if (!searchTerm.startsWith("@")) {
				return false;
			}
			String string = searchTerm.substring(1);
			if (string.isEmpty()) {
				return true;
			} else if (namespace.equals(string)) {
				return true;
			} else if (namespace.contains(string)) {
				return true;
			}
			return namespace.startsWith(string);
		}
	},
	TAG_SEARCH("tag_search", 3, false) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			if (searchTerm.isEmpty()) {
				return true;
			} else if (!searchTerm.startsWith("#")) {
				return false;
			}
			String string = searchTerm.substring(1);
			if (string.isEmpty()) {
				return true;
			} else {
				TagKey<Item> tag = this.getTag(string);
				if (tag == null) {
					return false;
				} else {
					return Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(tag).contains(stack.getItem());
				}
			}
		}
		
		private @Nullable TagKey<Item> getTag(String searchTerm) {
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
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			if (searchTerm.isEmpty()) {
				return true;
			}
			int count = Util.tryParseInteger(searchTerm, -1);
			if (count == -1) {
				return false;
			}
			char firstChar = searchTerm.charAt(0);
			if (firstChar == '=') {
				return stack.getCount() == count;
			} else if (firstChar == '<') {
				return stack.getCount() <= count;
			} else if (firstChar == '>') {
				return stack.getCount() >= count;
			}
			char lastChar = searchTerm.charAt(searchTerm.length() - 1);
			if (lastChar == '<') {
				return stack.getCount() >= count;
			} else if (lastChar == '>') {
				return stack.getCount() <= count;
			}
			return stack.getCount() == count;
		}
	},
	STACKABLE("stackable", 5) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			return this.checkCustom(stack, CustomBackpackFilterItem::isStackable) || stack.isStackable();
		}
	},
	NONE_STACKABLE("none_stackable", 6) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			return this.checkCustom(stack, (custom, itemStack) -> !custom.isStackable(itemStack)) || !stack.isStackable();
		}
	},
	MAX_COUNT("max_count", 7) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			return this.checkCustom(stack, CustomBackpackFilterItem::isMaxCount) || stack.getItem().getMaxStackSize(stack) == stack.getCount();
		}
	},
	ENCHANTABLE("enchantable", 8) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			return stack.getItem() == Items.BOOK || this.checkCustom(stack, CustomBackpackFilterItem::isEnchantable) || stack.isEnchantable();
		}
	},
	ENCHANTED("enchanted", 9) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			return stack.getItem() instanceof EnchantedBookItem || this.checkCustom(stack, CustomBackpackFilterItem::isEnchanted) || stack.isEnchanted();
		}
	},
	DAMAGEABLE("damageable", 10) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			return this.checkCustom(stack, CustomBackpackFilterItem::isDamageable) || stack.isDamageableItem();
		}
	},
	DAMAGED("damaged", 11) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			return this.checkCustom(stack, CustomBackpackFilterItem::isDamaged) || stack.isDamaged();
		}
	},
	EDIBLE("edible", 12) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			return this.checkCustom(stack, CustomBackpackFilterItem::isEdible) || stack.isEdible();
		}
	},
	REPAIRABLE("repairable", 13) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			return this.checkCustom(stack, CustomBackpackFilterItem::isRepairable) || stack.isRepairable();
		}
	},
	FIRE_RESISTANT("fire_resistant", 14) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			return this.checkCustom(stack, CustomBackpackFilterItem::isFireResistant) || stack.getItem().isFireResistant();
		}
	},
	WEAPON("weapon", 15) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			Item item = stack.getItem();
			return this.checkCustom(stack, CustomBackpackFilterItem::isWeapon) || item instanceof SwordItem || item instanceof BowItem || item instanceof CrossbowItem;
		}
	},
	TOOL("tool", 16) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			Item item = stack.getItem();
			return this.checkCustom(stack, CustomBackpackFilterItem::isTool) || item instanceof DiggerItem || item instanceof FishingRodItem || item instanceof FlintAndSteelItem || item instanceof CompassItem || item == Items.CLOCK;
		}
	},
	ARMOR("armor", 17) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			return this.checkCustom(stack, CustomBackpackFilterItem::isArmor) || stack.getItem() instanceof Equipable;
		}
	},
	FOOD("food", 18) {
		@Override
		protected boolean canKeepItem(ItemStack stack, String searchTerm) {
			return this.checkCustom(stack, CustomBackpackFilterItem::isFood) || stack.getFoodProperties(null) != null || stack.getItem().getFoodProperties() != null;
		}
	};
	
	private final String name;
	private final int id;
	private final boolean selectable;
	
	ItemFilters(String name, int id) {
		this(name, id, true);
	}
	
	ItemFilters(String name, int id, boolean selectable) {
		this.name = name;
		this.id = id;
		this.selectable = selectable;
	}
	
	public static @NotNull ItemFilter byId(int id, ItemFilter fallback) {
		for (ItemFilter filter : ItemFilters.values()) {
			if (filter.getId() == id) {
				return filter;
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
	
	protected abstract boolean canKeepItem(ItemStack stack, String searchTerm);
	
	@Override
	public boolean canKeepItem(ItemStack stack, String searchTerm, boolean negate) {
		if (this == NONE) {
			return NONE.canKeepItem(stack, searchTerm);
		}
		return negate != this.canKeepItem(stack, searchTerm);
	}
	
	protected boolean checkCustom(@NotNull ItemStack stack, BiPredicate<CustomBackpackFilterItem, ItemStack> predicate) {
		if (stack.getItem() instanceof CustomBackpackFilterItem custom) {
			return predicate.test(custom, stack);
		}
		return false;
	}
	
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
}
