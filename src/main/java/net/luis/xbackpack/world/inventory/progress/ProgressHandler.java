package net.luis.xbackpack.world.inventory.progress;

import net.minecraft.nbt.CompoundTag;

/**
 *
 * @author Luis-St
 *
 */

public interface ProgressHandler {
	
	void tick();
	
	void broadcastChanges();
	
	CompoundTag serialize();
	
	void deserialize(CompoundTag tag);
}
