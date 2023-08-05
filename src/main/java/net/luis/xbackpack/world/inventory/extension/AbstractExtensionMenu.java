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
