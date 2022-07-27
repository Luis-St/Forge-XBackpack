package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.inventory.extension.StonecutterExtensionMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class StonecutterExtensionResultSlot extends ExtensionSlot {

	public StonecutterExtensionResultSlot(StonecutterExtensionMenu extensionMenu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(extensionMenu, itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public StonecutterExtensionMenu getMenu() {
		return (StonecutterExtensionMenu) super.getMenu();
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
	
	@Override
	public void onTake(Player player, ItemStack stack) {
		this.getMenu().onTake(player, stack);
		super.onTake(player, stack);
	}
	
}
