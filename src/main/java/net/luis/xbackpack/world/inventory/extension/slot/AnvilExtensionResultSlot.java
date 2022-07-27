package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.inventory.extension.AnvilExtensionMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class AnvilExtensionResultSlot extends ExtensionSlot {
	
	public AnvilExtensionResultSlot(AnvilExtensionMenu extensionMenu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(extensionMenu, itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public AnvilExtensionMenu getMenu() {
		return (AnvilExtensionMenu) super.getMenu();
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean mayPickup(Player player) {
		return this.getMenu().mayPickup(player, this.hasItem());
	}
	
	@Override
	public void onTake(Player player, ItemStack stack) {
		this.getMenu().onTake(player, stack);
		super.onTake(player, stack);
	}
	
}
