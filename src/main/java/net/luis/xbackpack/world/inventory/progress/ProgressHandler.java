package net.luis.xbackpack.world.inventory.progress;

import net.minecraft.nbt.CompoundTag;

/**
 * 
 * @author Luis-st
 *
 */

public interface ProgressHandler {
	
	void tick();
	
	void broadcastChanges();
	
	CompoundTag serialize();
	
	void deserialize(CompoundTag tag);
	
}
