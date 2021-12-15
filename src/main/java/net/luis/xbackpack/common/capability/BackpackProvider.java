package net.luis.xbackpack.common.capability;

import net.luis.xbackpack.init.XBackpackCapabilities;
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

	private final BackpackHandler handler = new BackpackHandler(38);
	private final LazyOptional<IBackpack> optional = LazyOptional.of(() -> handler);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return cap == XBackpackCapabilities.BACKPACK ? this.optional.cast() : LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		return this.handler.serializeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		this.handler.deserializeNBT(tag);
	}

}
