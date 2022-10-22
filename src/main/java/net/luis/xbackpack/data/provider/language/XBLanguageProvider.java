package net.luis.xbackpack.data.provider.language;

import net.luis.xbackpack.XBackpack;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

/**
 *
 * @author Luis-st
 *
 */

public class XBLanguageProvider extends LanguageProvider {

	public XBLanguageProvider(DataGenerator generator) {
		super(generator, XBackpack.MOD_ID, "en_us");
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
		this.add("xbackpack.commands.backpack.get_failure", "Can not get the state of multiple backpack extensions");
		this.add("xbackpack.commands.backpack.get_success", "Backpack extension {0} of player {1} has state {2}");
		this.add("xbackpack.commands.backpack.set_success.single", "Set successfully the state of backpack extension {0} of player {1} to state {2}");
		this.add("xbackpack.commands.backpack.set_success.multiple", "Set successfully the state of {0} backpack extensions of player {1} to state {2}");
	}
	
}
