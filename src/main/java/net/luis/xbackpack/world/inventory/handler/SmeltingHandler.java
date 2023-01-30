package net.luis.xbackpack.world.inventory.handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;

/**
 *
 * @author Luis-st
 *
 */

public class SmeltingHandler extends CraftingFuelHandler {
	
	private final ItemStackHandler inputStorageHandler;
	private final ItemStackHandler resultStorageHandler;
	
	public SmeltingHandler(int input, int inputStorage, int resultStorage) {
		this(input, 1, 1, inputStorage, resultStorage);
	}
	
	public SmeltingHandler(ItemStackHandler inputHandler, ItemStackHandler inputStorageHandler, ItemStackHandler resultStorageHandler) {
		this(inputHandler, new ItemStackHandler(1), new ItemStackHandler(1), inputStorageHandler, resultStorageHandler);
	}
	
	public SmeltingHandler(int input, int fuel, int result, int inputStorage, int resultStorage) {
		this(new ItemStackHandler(input), new ItemStackHandler(fuel), new ItemStackHandler(result), new ItemStackHandler(inputStorage), new ItemStackHandler(resultStorage));
	}
	
	public SmeltingHandler(ItemStackHandler inputHandler, ItemStackHandler fuelHandler, ItemStackHandler resultHandler, ItemStackHandler inputStorageHandler, ItemStackHandler resultStorageHandler) {
		super(inputHandler, fuelHandler, resultHandler);
		this.inputStorageHandler = inputStorageHandler;
		this.resultStorageHandler = resultStorageHandler;
	}
	
	public ItemStackHandler getInputStorageHandler() {
		return this.inputStorageHandler;
	}
	
	public ItemStackHandler getResultStorageHandler() {
		return this.resultStorageHandler;
	}
	
	@Override
	public CompoundTag serialize() {
		CompoundTag tag = super.serialize();
		tag.put("input_storage_handler", this.inputStorageHandler.serializeNBT());
		tag.put("result_storage_handler", this.resultStorageHandler.serializeNBT());
		return tag;
	}
	
	@Override
	public void deserialize(CompoundTag tag) {
		super.deserialize(tag);
		this.inputStorageHandler.deserializeNBT(tag.getCompound("input_storage_handler"));
		this.resultStorageHandler.deserializeNBT(tag.getCompound("result_storage_handler"));
	}
	
}
