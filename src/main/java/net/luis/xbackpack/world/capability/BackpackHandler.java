package net.luis.xbackpack.world.capability;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.XBackpack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackHandler extends ItemStackHandler implements IBackpack {
	
	BackpackHandler(int size) {
		super(size);
	}
	
	@Override
	public void deserializeNBT(CompoundTag tag) {
		int size = tag.getInt("Size");
		if (BackpackConstans.BACKPACK_SLOT_COUNT != size) {
			if (BackpackConstans.BACKPACK_SLOT_COUNT > size) {
				XBackpack.LOGGER.info("Increase the backpack slot count to {}", BackpackConstans.BACKPACK_SLOT_COUNT);
			} else {
				XBackpack.LOGGER.info("Decrease the backpack slot count to {}", BackpackConstans.BACKPACK_SLOT_COUNT);
			}
			size = BackpackConstans.BACKPACK_SLOT_COUNT;
		}
		ListTag items = tag.getList("Items", Tag.TAG_COMPOUND);
		for (int i = 0; i < items.size(); i++) {
			CompoundTag item = items.getCompound(i);
			int slot = item.getInt("Slot");
			if (slot >= 0 && slot < this.stacks.size()) {
				this.stacks.set(slot, ItemStack.of(item));
			}
		}
		this.onLoad();
	}

}
