package net.luis.xbackpack.world.item;

import net.minecraft.world.item.ItemStack;

/**
 *
 * @author Luis-St
 *
 */

public interface CustomBackpackFilterItem {
	
	boolean isStackable(ItemStack stack);
	
	boolean isMaxCount(ItemStack stack);
	
	boolean isEnchantable(ItemStack stack);
	
	boolean isEnchanted(ItemStack stack);
	
	boolean isDamageable(ItemStack stack);
	
	boolean isDamaged(ItemStack stack);
	
	boolean isEdible(ItemStack stack);
	
	boolean isRepairable(ItemStack stack);
	
	boolean isFireResistant(ItemStack stack);
	
	boolean isWeapon(ItemStack stack);
	
	boolean isTool(ItemStack stack);
	
	boolean isArmor(ItemStack stack);
	
	boolean isFood(ItemStack stack);
}
