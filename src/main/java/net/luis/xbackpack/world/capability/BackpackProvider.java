package net.luis.xbackpack.world.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackProvider implements ICapabilitySerializable<CompoundTag> {
	
	public static final Capability<IBackpack> BACKPACK = CapabilityManager.get(new CapabilityToken<IBackpack>() {});
	
	private final Player player;
	private final BackpackHandler handler;
	private final LazyOptional<IBackpack> optional;
	
	public BackpackProvider(Player player) {
		this.player = player;
		this.handler = new BackpackHandler(this.player);
		this.optional = LazyOptional.of(() -> this.handler);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		return BACKPACK.orEmpty(capability, this.optional);
	}
	
	@Override
	public CompoundTag serializeNBT() {
		return this.handler.serialize();
	}
	
	@Override
	public void deserializeNBT(CompoundTag tag) {
		this.handler.deserialize(tag);
	}
	
	public static IBackpack get(Player player) {
		return player.getCapability(BACKPACK, null).orElseThrow(NullPointerException::new);
	}
	
}
