package net.luis.xbackpack.world.inventory.modifier.filter;

import net.luis.xbackpack.world.inventory.modifier.ItemModifier;
import net.luis.xbackpack.world.inventory.modifier.ItemModifierType;
import net.minecraft.world.item.ItemStack;

/**
 *
 * @author Luis-st
 *
 */

public interface ItemFilter extends ItemModifier {
	
	@Override
	default ItemModifierType getType() {
		return ItemModifierType.FILTER;
	}
	
	boolean canKeepItem(ItemStack stack, String searchTerm);
	
}
