package net.luis.xbackpack.world.inventory;

import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class CraftingContainerWrapper extends CraftingContainer implements IItemHandlerModifiable {
	
	private final IItemHandlerModifiable itemHandler;
	
	public CraftingContainerWrapper(AbstractContainerMenu menu, IItemHandlerModifiable itemHandler, int width, int height) {
		super(menu, width, height);
		this.itemHandler = itemHandler;
	}
	
	@Override
	public int getSlots() {
		return this.itemHandler.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.itemHandler.getStackInSlot(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		ItemStack itemStack = this.itemHandler.insertItem(slot, stack, simulate);
		this.menu.slotsChanged(this);
		return itemStack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack stack = this.itemHandler.extractItem(slot, amount, simulate);
		if (!stack.isEmpty()) {
			this.menu.slotsChanged(this);
		}
		return stack;
	}

	@Override
	public int getSlotLimit(int slot) {
		return this.itemHandler.getSlotLimit(slot);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return this.itemHandler.isItemValid(slot, stack);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		this.itemHandler.setStackInSlot(slot, stack);
		this.menu.slotsChanged(this);
	}
	
	@Override
	public int getContainerSize() {
		return this.getSlots();
	}
	
	@Override
	public boolean isEmpty() {
		for (int i = 0; i < this.getSlots(); i++) {
			if (!this.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public ItemStack getItem(int slot) {
		return this.getStackInSlot(slot);
	}
	
	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return this.extractItem(slot, slot, true);
	}
	
	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack stack = this.extractItem(slot, amount, false);
		if (!stack.isEmpty()) {
			this.menu.slotsChanged(this);
		}
		return stack;
	}
	
	@Override
	public void setItem(int slot, ItemStack stack) {
		this.insertItem(slot, stack, false);
		this.menu.slotsChanged(this);
	}
	
	@Override
	public void clearContent() {
		for (int i = 0; i < this.getSlots(); i++) {
			this.setStackInSlot(i, ItemStack.EMPTY);
		}
	}
	
	@Override
	public void fillStackedContents(StackedContents contents) {
		for (int i = 0; i < this.getSlots(); i++) {
			contents.accountSimpleStack(this.getStackInSlot(i));
		}
	}

}
