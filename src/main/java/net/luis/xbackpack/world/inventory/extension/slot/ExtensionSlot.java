package net.luis.xbackpack.world.inventory.extension.slot;

import java.util.Optional;

import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.extension.AbstractExtensionMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class ExtensionSlot extends SlotItemHandler {
	
	private final AbstractExtensionMenu extensionMenu;
	
	public ExtensionSlot(AbstractExtensionMenu extensionMenu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.extensionMenu = extensionMenu;
	}
	
	public AbstractExtensionMenu getMenu() {
		return this.extensionMenu;
	}
	
	public BackpackExtension getExtension() {
		return this.extensionMenu.getExtension();
	}
	
	@Override
	public void set(ItemStack stack) {
		super.set(stack);
		this.extensionMenu.slotsChanged();
	}
	
	@Override
	public void initialize(ItemStack stack) {
		super.initialize(stack);
		this.extensionMenu.slotsChanged();
	}
	
	@Override
	public Optional<ItemStack> tryRemove(int minAmount, int maxAmount, Player player) {
		Optional<ItemStack> optional = super.tryRemove(minAmount, maxAmount, player);
		this.extensionMenu.slotsChanged();
		return optional;
	}
	
	@Override
	public ItemStack safeInsert(ItemStack stack, int count) {
		ItemStack remainingStack = super.safeInsert(stack, count);
		this.extensionMenu.slotsChanged();
		return remainingStack;
	}
	
}
