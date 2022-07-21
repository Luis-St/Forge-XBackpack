package net.luis.xbackpack.world.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class CraftingHandler {
	
	private final ItemStackHandler inputHandler;
	private final ItemStackHandler resultHandler;
	
	public CraftingHandler(ItemStackHandler inputHandler) {
		this(inputHandler, new ItemStackHandler(1));
	}
	
	public CraftingHandler(int input, int result) {
		this(new ItemStackHandler(input), new ItemStackHandler(result));
	}
	
	public CraftingHandler(ItemStackHandler inputHandler, ItemStackHandler resultHandler) {
		this.inputHandler = inputHandler;
		this.resultHandler = resultHandler;
	}
	
	public ItemStackHandler getInputHandler() {
		return this.inputHandler;
	}
	
	public ItemStackHandler getResultHandler() {
		return this.resultHandler;
	}
	
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.put("input_handler", this.inputHandler.serializeNBT());
		tag.put("result_handler", this.resultHandler.serializeNBT());
		return tag;
	}
	
	public void deserialize(CompoundTag tag) {
		this.inputHandler.deserializeNBT(tag.getCompound("input_handler"));
		this.resultHandler.deserializeNBT(tag.getCompound("result_handler"));
	}
	
}
