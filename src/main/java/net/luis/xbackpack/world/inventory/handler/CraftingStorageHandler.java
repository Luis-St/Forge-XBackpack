package net.luis.xbackpack.world.inventory.handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class CraftingStorageHandler extends CraftingHandler {
	
	private final ItemStackHandler storageHandler;
	
	public CraftingStorageHandler(int input, int storage) {
		this(new ItemStackHandler(input), new ItemStackHandler(1), new ItemStackHandler(storage));
	}
	
	public CraftingStorageHandler(ItemStackHandler inputHandler, ItemStackHandler storageHandler) {
		this(inputHandler, new ItemStackHandler(1), storageHandler);
	}
	
	public CraftingStorageHandler(int input, int result, int storage) {
		this(new ItemStackHandler(input), new ItemStackHandler(result), new ItemStackHandler(storage));
	}
	
	public CraftingStorageHandler(ItemStackHandler inputHandler, ItemStackHandler resultHandler, ItemStackHandler storageHandler) {
		super(inputHandler, resultHandler);
		this.storageHandler = storageHandler;
	}
	
	public ItemStackHandler getStorageHandler() {
		return this.storageHandler;
	}
	
	@Override
	public CompoundTag serialize() {
		CompoundTag tag = super.serialize();
		tag.put("storage_handler", this.storageHandler.serializeNBT());
		return tag;
	}
	
	@Override
	public void deserialize(CompoundTag tag) {
		super.deserialize(tag);
		this.storageHandler.deserializeNBT(tag.getCompound("storage_handler"));
	}

}
