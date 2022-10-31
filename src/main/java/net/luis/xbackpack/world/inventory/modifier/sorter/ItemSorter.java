package net.luis.xbackpack.world.inventory.modifier.sorter;

import java.util.List;

import net.luis.xbackpack.world.inventory.modifier.ItemModifier;
import net.luis.xbackpack.world.inventory.modifier.ItemModifierType;
import net.minecraft.world.item.ItemStack;

/**
 *
 * @author Luis-st
 *
 */

public interface ItemSorter extends ItemModifier {
	
	@Override
	default ItemModifierType getType() {
		return ItemModifierType.SORTER;
	}
	
	List<ItemStack> sort(List<ItemStack> stacks, String searchTerm);
	
}
