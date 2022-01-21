package net.luis.xbackpack.common.capability;

import net.luis.xbackpack.init.XBackpackCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackProvider implements ICapabilitySerializable<CompoundTag> {

	/**
	 * the inventory of the Backpack, a {@link BackpackHandler} with 38 slots
	 */
	private final BackpackHandler handler = new BackpackHandler(38);
	
	/**
	 * {@link LazyOptional} of the {@link BackpackProvider#handler},<br>
	 * which is returned by {@link ICapabilityProvider#getCapability()}
	 */
	private final LazyOptional<IBackpack> optional = LazyOptional.of(() -> handler);

	/**
	 * returns the {@link BackpackProvider#optional}<br> 
	 * if the given Capability is {@link XBackpackCapabilities#BACKPACK}
	 */
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		return capability == XBackpackCapabilities.BACKPACK ? this.optional.cast() : LazyOptional.empty();
	}

	/**
	 * handled in the {@link BackpackHandler#serializeNBT()}
	 */
	@Override
	public CompoundTag serializeNBT() {
		return this.handler.serializeNBT();
	}
	
	/**
	 * handled in the {@link BackpackHandler#deserializeNBT()}
	 */
	@Override
	public void deserializeNBT(CompoundTag tag) {
		this.handler.deserializeNBT(tag);
	}

}
