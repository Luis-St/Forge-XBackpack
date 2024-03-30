/*
 * XBackpack
 * Copyright (C) 2024 Luis Staudt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
