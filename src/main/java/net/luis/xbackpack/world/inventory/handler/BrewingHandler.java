package net.luis.xbackpack.world.inventory.handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class BrewingHandler extends CraftingHandler {
	
	private final ItemStackHandler fuelHandler;
	
	public BrewingHandler(int input, int result) {
		this(new ItemStackHandler(input), new ItemStackHandler(result));
	}
	
	public BrewingHandler(ItemStackHandler inputHandler, ItemStackHandler resultHandler) {
		this(inputHandler, new ItemStackHandler(1), resultHandler);
	}
	
	public BrewingHandler(int input, int fuel, int result) {
		this(new ItemStackHandler(input), new ItemStackHandler(fuel), new ItemStackHandler(result));
	}
	
	public BrewingHandler(ItemStackHandler inputHandler, ItemStackHandler fuelHandler, ItemStackHandler resultHandler) {
		super(inputHandler, resultHandler);
		this.fuelHandler = fuelHandler;
	}
	
	public ItemStackHandler getFuelHandler() {
		return this.fuelHandler;
	}
	
	@Override
	public CompoundTag serialize() {
		CompoundTag tag = super.serialize();
		tag.put("fuel_handler", this.fuelHandler.serializeNBT());
		return tag;
	}
	
	@Override
	public void deserialize(CompoundTag tag) {
		super.deserialize(tag);
		this.fuelHandler.deserializeNBT(tag.getCompound("fuel_handler"));
	}
	
}
