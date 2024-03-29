package net.luis.xbackpack.world.item;

import net.luis.xbackpack.XBackpack;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/**
 *
 * @author Luis-St
 *
 */

public class DynamicItemStackHandler extends ItemStackHandler {
	
	private final int initialSize;
	
	public DynamicItemStackHandler(int size) {
		super(size);
		this.initialSize = size;
	}
	
	@Override
	public CompoundTag serializeNBT() {
		ListTag itemsTag = new ListTag();
		for (int i = 0; i < this.stacks.size(); i++) {
			if (!this.stacks.get(i).isEmpty()) {
				CompoundTag itemTag = new CompoundTag();
				itemTag.putInt("Slot", i);
				this.stacks.get(i).save(itemTag);
				itemsTag.add(itemTag);
			}
		}
		CompoundTag handlerTag = new CompoundTag();
		handlerTag.put("Items", itemsTag);
		handlerTag.putInt("Size", this.initialSize);
		return handlerTag;
	}
	
	@Override
	public void deserializeNBT(CompoundTag tag) {
		int size = tag.contains("Size", Tag.TAG_INT) ? tag.getInt("Size") : this.stacks.size();
		boolean reduced = false;
		if (this.initialSize >= size) {
			this.setSize(this.initialSize);
		} else {
			this.setSize(size);
			reduced = true;
		}
		ListTag itemsTag = tag.getList("Items", Tag.TAG_COMPOUND);
		if (reduced) {
			XBackpack.LOGGER.error("DynamicItemStackHandler does currently not support shrinking of the inventory size");
			throw new RuntimeException("Tried to deserialize to an ItemStackHandler with more slots than it was created with");
		} else {
			for (int i = 0; i < itemsTag.size(); i++) {
				CompoundTag itemTag = itemsTag.getCompound(i);
				int slot = itemTag.getInt("Slot");
				if (slot >= 0 && slot < this.stacks.size()) {
					this.stacks.set(slot, ItemStack.of(itemTag));
				}
			}
		}
		this.onLoad();
	}
	
	@Override
	protected void validateSlotIndex(int slot) {
		if (this.initialSize > this.stacks.size()) {
			NonNullList<ItemStack> stacks = NonNullList.withSize(this.initialSize, ItemStack.EMPTY);
			for (int i = 0; i < this.stacks.size(); i++) {
				stacks.set(i, this.stacks.get(i));
			}
			this.stacks = stacks;
		} else if (this.initialSize < this.stacks.size()) {
			XBackpack.LOGGER.error("DynamicItemStackHandler does currently not support shrinking of the inventory size");
			throw new RuntimeException("Tried to decrease an ItemStackHandler by " + (this.stacks.size() - this.initialSize) + " while it was created with " + this.stacks.size() + " slots");
		}
		if (slot < 0 || slot >= this.stacks.size()) {
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.stacks.size() + ")");
		}
	}
}
