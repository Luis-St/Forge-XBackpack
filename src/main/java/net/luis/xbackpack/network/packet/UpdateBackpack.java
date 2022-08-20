package net.luis.xbackpack.network.packet;

import java.util.function.Supplier;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

/**
 * 
 * @author Luis-st
 *
 */

public class UpdateBackpack {
	
	private final CompoundTag tag;
	
	public UpdateBackpack(CompoundTag tag) {
		this.tag = tag;
	}
	
	public UpdateBackpack(FriendlyByteBuf buffer) {
		this.tag = buffer.readAnySizeNbt();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeNbt(this.tag);
	}
	
	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			XBClientPacketHandler.updateBackpack(this.tag);
		});
	}
	
}
