package net.luis.xbackpack.world.capability;

import net.luis.xbackpack.world.backpack.BackpackHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-st
 *
 */

public class BackpackProvider implements ICapabilitySerializable<CompoundTag> {
	
	public static final Capability<IBackpack> BACKPACK = CapabilityManager.get(new CapabilityToken<>() {});
	
	private final BackpackHandler handler;
	private final LazyOptional<IBackpack> optional;
	
	public BackpackProvider(Player player) {
		this.handler = new BackpackHandler(player);
		this.optional = LazyOptional.of(() -> this.handler);
	}
	
	public static IBackpack get(@NotNull Player player) {
		return player.getCapability(BACKPACK, null).orElseThrow(NullPointerException::new);
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, Direction side) {
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
	
}
