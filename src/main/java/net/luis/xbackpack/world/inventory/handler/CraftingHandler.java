package net.luis.xbackpack.world.inventory.handler;

import net.luis.xbackpack.world.item.DynamicItemStackHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-st
 *
 */

public class CraftingHandler {
	
	private final ItemStackHandler inputHandler;
	private final ItemStackHandler resultHandler;
	
	public CraftingHandler(DynamicItemStackHandler inputHandler) {
		this(inputHandler, new DynamicItemStackHandler(1));
	}
	
	public CraftingHandler(int input, int result) {
		this(new DynamicItemStackHandler(input), new DynamicItemStackHandler(result));
	}
	
	public CraftingHandler(DynamicItemStackHandler inputHandler, DynamicItemStackHandler resultHandler) {
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
	
	public void deserialize(@NotNull CompoundTag tag) {
		this.inputHandler.deserializeNBT(tag.getCompound("input_handler"));
		this.resultHandler.deserializeNBT(tag.getCompound("result_handler"));
	}
}
