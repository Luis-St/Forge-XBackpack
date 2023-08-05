package net.luis.xbackpack.data.provider.language;

import net.luis.xbackpack.XBackpack;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

/**
 *
 * @author Luis-st
 *
 */

public class XBLanguageProvider extends LanguageProvider {
	
	public XBLanguageProvider(PackOutput output) {
		super(output, XBackpack.MOD_ID, "en_us");
	}
	
	@Override
	protected void addTranslations() {
		this.add("xbackpack.container.backpack", "Backpack");
		this.add("xbackpack.key.categories.backpack", "Backpack");
		this.add("xbackpack.key.backpack_open", "Open Backpack");
		this.add("xbackpack.key.backpack_next", "Next Tool");
		this.add("xbackpack.key.backpack_slot_top", "Tool Top");
		this.add("xbackpack.key.backpack_slot_mid", "Tool Mid");
		this.add("xbackpack.key.backpack_slot_down", "Tool Down");
		this.add("xbackpack.backpack_extension.crafting_table.title", "Crafting");
		this.add("xbackpack.backpack_extension.crafting_table.tooltip", "Crafting");
		this.add("xbackpack.backpack_extension.furnace.title", "Smelting");
		this.add("xbackpack.backpack_extension.furnace.tooltip", "Smelting");
		this.add("xbackpack.backpack_extension.anvil.title", "Repair");
		this.add("xbackpack.backpack_extension.anvil.tooltip", "Repair");
		this.add("xbackpack.backpack_extension.anvil.cost", "Cost: {0}");
		this.add("xbackpack.backpack_extension.enchantment_table.title", "Enchanting");
		this.add("xbackpack.backpack_extension.enchantment_table.tooltip", "Enchanting");
		this.add("xbackpack.backpack_extension.stonecutter.title", "Stonecutter");
		this.add("xbackpack.backpack_extension.stonecutter.tooltip", "Stonecutter");
		this.add("xbackpack.backpack_extension.brewing_stand.title", "Brewing");
		this.add("xbackpack.backpack_extension.brewing_stand.tooltip", "Brewing");
		this.add("xbackpack.backpack_extension.grindstone.title", "Disenchanting");
		this.add("xbackpack.backpack_extension.grindstone.tooltip", "Disenchanting");
		this.add("xbackpack.backpack_extension.smithing_table.title", "Smithing");
		this.add("xbackpack.backpack_extension.smithing_table.tooltip", "Smithing");
		this.add("xbackpack.commands.arguments.extension.invalid", "Unknown extension {0}");
		this.add("xbackpack.commands.arguments.state.invalid", "Unknown extension state {0}");
		this.add("xbackpack.commands.backpack.get.failure", "Can not get the state of multiple backpack extensions");
		this.add("xbackpack.commands.backpack.get.success", "Backpack extension {0} of player {1} has state {2}");
		this.add("xbackpack.commands.backpack.set.failure.disabled", "Can not set the state of {0}, because the backpack extension is disabled by the mod");
		this.add("xbackpack.commands.backpack.set.success.single", "Set successfully the state of backpack extension {0} of player {1} to state {2}");
		this.add("xbackpack.commands.backpack.set.success.multiple", "Set successfully the state of {0} backpack extensions of player {1} to state {2}");
		this.add("xbackpack.commands.tooltip.set.success.true", "The item modification information was enabled");
		this.add("xbackpack.commands.tooltip.set.success.false", "The item modification information was disabled");
		this.add("xbackpack.commands.tooltip.get.success.true", "The item modification information is enabled");
		this.add("xbackpack.commands.tooltip.get.success.false", "The item modification information is disabled");
		this.add("xbackpack.commands.tooltip.failure", "The command can be executed only by a player");
		this.add("xbackpack.backpack_action.filter.none", "No Filter");
		this.add("xbackpack.backpack_action.filter.none.info.0", "No item filter active");
		this.add("xbackpack.backpack_action.filter.none.info.1", "All items are displayed");
		this.add("xbackpack.backpack_action.filter.name_search", "Name Filter");
		this.add("xbackpack.backpack_action.filter.name_search.info.0", "Only items that match the search term will be displayed");
		this.add("xbackpack.backpack_action.filter.namespace_search", "Namespace Filter");
		this.add("xbackpack.backpack_action.filter.namespace_search.info.0", "Only items are displayed whose namespace corresponds the entered search term");
		this.add("xbackpack.backpack_action.filter.tag_search", "Tag Filter");
		this.add("xbackpack.backpack_action.filter.tag_search.info.0", "Only items that are subordinate the entered tag are displayed");
		this.add("xbackpack.backpack_action.filter.count_search", "Count Filter");
		this.add("xbackpack.backpack_action.filter.count_search.info.0", "Only items that correspond to the entered number are displayed");
		this.add("xbackpack.backpack_action.filter.stackable", "Stackable Filter");
		this.add("xbackpack.backpack_action.filter.stackable.info.0", "Only items that are stackable are displayed");
		this.add("xbackpack.backpack_action.filter.none_stackable", "None Stackable Filter");
		this.add("xbackpack.backpack_action.filter.none_stackable.info.0", "Only items that are not stackable are displayed");
		this.add("xbackpack.backpack_action.filter.max_count", "Max Count Filter");
		this.add("xbackpack.backpack_action.filter.max_count.info.0", "Only items that have the maximum stack size are displayed");
		this.add("xbackpack.backpack_action.filter.enchantable", "Enchantable Filter");
		this.add("xbackpack.backpack_action.filter.enchantable.info.0", "Only items that can be enchanted are displayed");
		this.add("xbackpack.backpack_action.filter.enchanted", "Enchanted Filter");
		this.add("xbackpack.backpack_action.filter.enchanted.info.0", "Only items that are enchanted are displayed");
		this.add("xbackpack.backpack_action.filter.damageable", "Damageable Filter");
		this.add("xbackpack.backpack_action.filter.damageable.info.0", "Only items that can be damaged are displayed");
		this.add("xbackpack.backpack_action.filter.damaged", "Damaged Filter");
		this.add("xbackpack.backpack_action.filter.damaged.info.0", "Only items that are damaged are displayed");
		this.add("xbackpack.backpack_action.filter.edible", "Edible Filter");
		this.add("xbackpack.backpack_action.filter.edible.info.0", "Only items that are edible are displayed");
		this.add("xbackpack.backpack_action.filter.repairable", "Repairable Filter");
		this.add("xbackpack.backpack_action.filter.repairable.info.0", "Only items that can be repaired are displayed");
		this.add("xbackpack.backpack_action.filter.fire_resistant", "Fire Resistant Filter");
		this.add("xbackpack.backpack_action.filter.fire_resistant.info.0", "Only items that are resistant to fire are displayed");
		this.add("xbackpack.backpack_action.filter.weapon", "Weapon Filter");
		this.add("xbackpack.backpack_action.filter.weapon.info.0", "Only weapons are displayed");
		this.add("xbackpack.backpack_action.filter.tool", "Tool Filter");
		this.add("xbackpack.backpack_action.filter.tool.info.0", "Only tools are displayed");
		this.add("xbackpack.backpack_action.filter.armor", "Armor Filter");
		this.add("xbackpack.backpack_action.filter.armor.info.0", "Only armor is displayed");
		this.add("xbackpack.backpack_action.filter.food", "Food Filter");
		this.add("xbackpack.backpack_action.filter.food.info.0", "Only food is displayed");
		this.add("xbackpack.backpack_action.sorter.none", "No Sorter");
		this.add("xbackpack.backpack_action.sorter.none.info.0", "No item sorter active");
		this.add("xbackpack.backpack_action.sorter.none.info.1", "All items are displayed in the normal order");
		this.add("xbackpack.backpack_action.sorter.name_search", "Name Sorter");
		this.add("xbackpack.backpack_action.sorter.name_search.info.0", "The items are sorted by the search term");
		this.add("xbackpack.backpack_action.sorter.name_search.info.1", "The item that most matches the search term comes first");
		this.add("xbackpack.backpack_action.sorter.namespace_search", "Namespace Sorter");
		this.add("xbackpack.backpack_action.sorter.namespace_search.info.0", "The items are sorted by the searched namespace");
		this.add("xbackpack.backpack_action.sorter.tag_search", "Tag Sorter");
		this.add("xbackpack.backpack_action.sorter.tag_search.info.0", "The items are sorted by the searched tag");
		this.add("xbackpack.backpack_action.sorter.count_search", "Count Sorter");
		this.add("xbackpack.backpack_action.sorter.count_search.info.0", "The items are sorted by the searched count");
		this.add("xbackpack.backpack_action.sorter.alphabetically", "Alphabetical Sorter");
		this.add("xbackpack.backpack_action.sorter.alphabetically.info.0", "The items are sorted alphabetically");
		this.add("xbackpack.backpack_action.sorter.count_downwards", "Count Downwards Sorter");
		this.add("xbackpack.backpack_action.sorter.count_downwards.info.0", "The items are sorted by the stack size");
		this.add("xbackpack.backpack_action.sorter.count_downwards.info.1", "The item with the largest stack size comes first");
		this.add("xbackpack.backpack_action.sorter.count_upwards", "Count Upwards Sorter");
		this.add("xbackpack.backpack_action.sorter.count_upwards.info.0", "The items are sorted by the stack size");
		this.add("xbackpack.backpack_action.sorter.count_upwards.info.1", "The item with the smallest stack size comes first");
		this.add("xbackpack.backpack_action.item_merger", "Item Merger");
		this.add("xbackpack.backpack_action.item_merger.info", "Tried to merge all items that are not maximally stacked");
	}
}
