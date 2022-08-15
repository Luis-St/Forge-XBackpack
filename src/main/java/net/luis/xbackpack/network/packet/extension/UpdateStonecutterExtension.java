package net.luis.xbackpack.network.packet.extension;

import java.util.function.Supplier;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

/**
 * 
 * @author Luis-st
 *
 */

public class UpdateStonecutterExtension {
	
	private final boolean resetSelected;
	
	public UpdateStonecutterExtension(boolean resetSelected) {
		this.resetSelected = resetSelected;
	}
	
	public UpdateStonecutterExtension(FriendlyByteBuf buffer) {
		this.resetSelected = buffer.readBoolean();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBoolean(this.resetSelected);
	}
	
	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateStonecutterExtension(this.resetSelected);
			});
		});
	}
	
}
