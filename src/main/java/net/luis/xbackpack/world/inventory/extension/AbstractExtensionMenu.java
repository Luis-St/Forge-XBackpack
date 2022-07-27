package net.luis.xbackpack.world.inventory.extension;

import java.util.function.Consumer;

import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

/**
 * 
 * @author Luis-st
 *
 */

public abstract class AbstractExtensionMenu {
	
	protected final BackpackMenu menu;
	protected final Player player;
	private final BackpackExtension extension;
	
	protected AbstractExtensionMenu(BackpackMenu menu, Player player, BackpackExtension extension) {
		this.menu = menu;
		this.player = player;
		this.extension = extension;
	}
	
	public void open() {
		
	}
	
	public abstract void addSlots(Consumer<Slot> consumer);
	
	public void slotsChanged(Container container) {
		
	}
	
	public void slotsChanged() {
		
	}
	
	public boolean clickMenuButton(Player player, int button) {
		return true;
	}
	
	public void close() {
		
	}
	
	public BackpackMenu getMenu() {
		return this.menu;
	}
	
	public BackpackExtension getExtension() {
		return this.extension;
	}
	
}
