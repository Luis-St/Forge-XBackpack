package net.luis.xbackpack.world.inventory.handler;

import net.luis.xbackpack.world.item.DynamicItemStackHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-St
 *
 */

public class SmeltingHandler extends CraftingFuelHandler {
	
	private final ItemStackHandler inputStorageHandler;
	private final ItemStackHandler resultStorageHandler;
	
	public SmeltingHandler(int input, int inputStorage, int resultStorage) {
		this(input, 1, 1, inputStorage, resultStorage);
	}
	
	public SmeltingHandler(int input, int fuel, int result, int inputStorage, int resultStorage) {
		this(new DynamicItemStackHandler(input), new DynamicItemStackHandler(fuel), new DynamicItemStackHandler(result), new DynamicItemStackHandler(inputStorage), new DynamicItemStackHandler(resultStorage));
	}
	
	public SmeltingHandler(DynamicItemStackHandler inputHandler, DynamicItemStackHandler fuelHandler, DynamicItemStackHandler resultHandler, DynamicItemStackHandler inputStorageHandler, DynamicItemStackHandler resultStorageHandler) {
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
	public void deserialize(@NotNull CompoundTag tag) {
		super.deserialize(tag);
		this.inputStorageHandler.deserializeNBT(tag.getCompound("input_storage_handler"));
		this.resultStorageHandler.deserializeNBT(tag.getCompound("result_storage_handler"));
	}
}
