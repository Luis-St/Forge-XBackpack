package net.luis.xbackpack.network.packet.extension;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.luis.xbackpack.network.NetworkPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

/**
 *
 * @author Luis-st
 *
 */

public class UpdateStonecutterPacket implements NetworkPacket {
	
	private final boolean resetSelected;
	
	public UpdateStonecutterPacket(boolean resetSelected) {
		this.resetSelected = resetSelected;
	}
	
	public UpdateStonecutterPacket(FriendlyByteBuf buffer) {
		this.resetSelected = buffer.readBoolean();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBoolean(this.resetSelected);
	}
	
	@Override
	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateStonecutterExtension(this.resetSelected);
			});
		});
	}
	
}
