package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.extension.AnvilExtensionMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AnvilExtensionResultSlot extends SlotItemHandler {

	private final AnvilExtensionMenu extensionMenu;
	
	public AnvilExtensionResultSlot(AnvilExtensionMenu extensionMenu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.extensionMenu = extensionMenu;
	}
	
	public AnvilExtensionMenu getMenu() {
		return this.extensionMenu;
	}
	
	public BackpackExtension getExtension() {
		return this.extensionMenu.getExtension();
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean mayPickup(Player player) {
		return this.extensionMenu.mayPickup(player, this.hasItem());
	}
	
	@Override
	public void onTake(Player player, ItemStack stack) {
		this.extensionMenu.onTake(player, stack);
		super.onTake(player, stack);
	}
	
}
