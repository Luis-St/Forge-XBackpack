package net.luis.xbackpack.network.packet;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.luis.xbackpack.network.NetworkPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-St
 *
 */

public class UpdateBackpackPacket implements NetworkPacket {
	
	private final CompoundTag tag;
	
	public UpdateBackpackPacket(CompoundTag tag) {
		this.tag = tag;
	}
	
	public UpdateBackpackPacket(@NotNull FriendlyByteBuf buffer) {
		this.tag = buffer.readNbt();
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeNbt(this.tag);
	}
	
	@Override
	public void handle(@NotNull CustomPayloadEvent.Context context) {
		context.enqueueWork(() -> {
			XBClientPacketHandler.updateBackpack(this.tag);
		});
	}
}
