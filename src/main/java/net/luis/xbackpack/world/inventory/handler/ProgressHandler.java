package net.luis.xbackpack.world.inventory.handler;

import net.minecraft.nbt.CompoundTag;

/**
 * 
 * @author Luis-st
 *
 */

public interface ProgressHandler {
	
	void tick();
	
	CompoundTag serialize();
	
	void deserialize(CompoundTag tag);
	
}
