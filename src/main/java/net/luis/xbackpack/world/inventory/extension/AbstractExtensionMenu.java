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

package net.luis.xbackpack.world.inventory.extension;

import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.AbstractExtensionContainerMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

/**
 *
 * @author Luis-St
 *
 */

public abstract class AbstractExtensionMenu {
	
	private final BackpackExtension extension;
	protected final AbstractExtensionContainerMenu menu;
	protected final Player player;
	
	protected AbstractExtensionMenu(AbstractExtensionContainerMenu menu, Player player, BackpackExtension extension) {
		this.menu = menu;
		this.player = player;
		this.extension = extension;
	}
	
	public void open() {
		
	}
	
	public abstract void addSlots(Consumer<Slot> consumer);
	
	public boolean requiresTickUpdate() {
		return false;
	}
	
	public void slotsChanged(Container container) {
		
	}
	
	public void slotsChanged() {
		
	}
	
	public boolean clickMenuButton(Player player, int button) {
		return true;
	}
	
	public abstract boolean quickMoveStack(ItemStack slotStack, int index);
	
	protected boolean movePreferredMenu(ItemStack slotStack) {
		if (!this.menu.moveItemStackTo(slotStack, 0, 873)) { // into menu
			if (!this.menu.moveItemStackTo(slotStack, 900, 909)) { // into hotbar
				return this.menu.moveItemStackTo(slotStack, 873, 900); // into inventory
			}
		}
		return true;
	}
	
	protected boolean movePreferredInventory(ItemStack slotStack) {
		if (!this.menu.moveItemStackTo(slotStack, 900, 909)) { // into hotbar
			if (!this.menu.moveItemStackTo(slotStack, 873, 900)) { // into inventory
				return this.menu.moveItemStackTo(slotStack, 0, 873); // into menu
			}
		}
		return true;
	}
	
	public void close() {
		
	}
	
	public AbstractExtensionContainerMenu getMenu() {
		return this.menu;
	}
	
	public BackpackExtension getExtension() {
		return this.extension;
	}
}
