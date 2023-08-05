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

public class CraftingFuelHandler extends CraftingHandler {
	
	private final ItemStackHandler fuelHandler;
	
	public CraftingFuelHandler(int input, int result) {
		this(new DynamicItemStackHandler(input), new DynamicItemStackHandler(result));
	}
	
	public CraftingFuelHandler(DynamicItemStackHandler inputHandler, DynamicItemStackHandler resultHandler) {
		this(inputHandler, new DynamicItemStackHandler(1), resultHandler);
	}
	
	public CraftingFuelHandler(int input, int fuel, int result) {
		this(new DynamicItemStackHandler(input), new DynamicItemStackHandler(fuel), new DynamicItemStackHandler(result));
	}
	
	public CraftingFuelHandler(DynamicItemStackHandler inputHandler, DynamicItemStackHandler fuelHandler, DynamicItemStackHandler resultHandler) {
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
	public void deserialize(@NotNull CompoundTag tag) {
		super.deserialize(tag);
		this.fuelHandler.deserializeNBT(tag.getCompound("fuel_handler"));
	}
}
