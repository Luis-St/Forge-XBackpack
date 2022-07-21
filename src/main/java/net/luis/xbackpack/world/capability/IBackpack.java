package net.luis.xbackpack.world.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Luis-st
 * 
 */

public interface IBackpack {
	
	ItemStackHandler getBackpackHandler();
	
	ItemStackHandler getToolHandler();
	
	ItemStackHandler getCraftingHandler();
	
	ItemStackHandler getFurnaceHandler();
	
	ItemStackHandler getAnvilHandler();
	
	ItemStackHandler getEnchantingHandler();
	
	ItemStackHandler getStonecutterHandler();
	
	ItemStackHandler getBrewingHandler();
	
	ItemStackHandler getGrindstoneHandler();
	
	ItemStackHandler getSmithingHandler();
	
	CompoundTag serialize();
	
	void deserialize(CompoundTag tag);
	
}
