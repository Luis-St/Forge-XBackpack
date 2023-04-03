package net.luis.xbackpack.network.packet;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.luis.xbackpack.network.NetworkPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 *
 * @author Luis-st
 *
 */

public class UpdateBackpackPacket implements NetworkPacket {
	
	private final CompoundTag tag;
	
	public UpdateBackpackPacket(CompoundTag tag) {
		this.tag = tag;
	}
	
	public UpdateBackpackPacket(@NotNull FriendlyByteBuf buffer) {
		this.tag = buffer.readAnySizeNbt();
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeNbt(this.tag);
	}
	
	@Override
	public void handle(@NotNull Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			XBClientPacketHandler.updateBackpack(this.tag);
		});
	}
	
}
