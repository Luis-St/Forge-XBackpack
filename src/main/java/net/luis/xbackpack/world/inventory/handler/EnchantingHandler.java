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

public class EnchantingHandler {
	
	private final ItemStackHandler powerHandler;
	private final ItemStackHandler inputHandler;
	private final ItemStackHandler fuelHandler;
	
	public EnchantingHandler(int power, int input) {
		this(power, input, 1);
	}
	
	public EnchantingHandler(DynamicItemStackHandler powerHandler, DynamicItemStackHandler inputHandler) {
		this(powerHandler, inputHandler, new DynamicItemStackHandler(1));
	}
	
	public EnchantingHandler(int power, int input, int fuel) {
		this(new DynamicItemStackHandler(power), new DynamicItemStackHandler(input), new DynamicItemStackHandler(fuel));
	}
	
	public EnchantingHandler(DynamicItemStackHandler powerHandler, DynamicItemStackHandler inputHandler, DynamicItemStackHandler fuelHandler) {
		this.powerHandler = powerHandler;
		this.inputHandler = inputHandler;
		this.fuelHandler = fuelHandler;
	}
	
	public ItemStackHandler getPowerHandler() {
		return this.powerHandler;
	}
	
	public ItemStackHandler getInputHandler() {
		return this.inputHandler;
	}
	
	public ItemStackHandler getFuelHandler() {
		return this.fuelHandler;
	}
	
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.put("power_handler", this.powerHandler.serializeNBT());
		tag.put("input_handler", this.inputHandler.serializeNBT());
		tag.put("fuel_handler", this.fuelHandler.serializeNBT());
		return tag;
	}
	
	public void deserialize(@NotNull CompoundTag tag) {
		this.powerHandler.deserializeNBT(tag.getCompound("power_handler"));
		this.inputHandler.deserializeNBT(tag.getCompound("input_handler"));
		this.fuelHandler.deserializeNBT(tag.getCompound("fuel_handler"));
	}
}
