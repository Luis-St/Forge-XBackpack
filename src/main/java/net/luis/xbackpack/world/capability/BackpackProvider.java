package net.luis.xbackpack.world.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackProvider implements ICapabilitySerializable<CompoundTag> {
	
	private final BackpackHandler handler = new BackpackHandler();
	
	private final LazyOptional<IBackpack> optional = LazyOptional.of(() -> this.handler);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		return capability == XBackpackCapabilities.BACKPACK ? this.optional.cast() : LazyOptional.empty();
	}
	
	@Override
	public CompoundTag serializeNBT() {
		return this.handler.serialize();
	}
	
	@Override
	public void deserializeNBT(CompoundTag tag) {
		this.handler.deserialize(tag);
	}

}
