package net.luis.xbackpack.network.packet.extension;

import java.util.function.Supplier;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.luis.xbackpack.network.NetworkPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

/**
 * 
 * @author Luis-st
 *
 */

public class UpdateAnvilPacket implements NetworkPacket {
	
	private final int cost;
	
	public UpdateAnvilPacket(int cost) {
		this.cost = cost;
	}
	
	public UpdateAnvilPacket(FriendlyByteBuf buffer) {
		this.cost = buffer.readInt();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.cost);
	}
	
	@Override
	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateAnvilExtension(this.cost);
			});
		});
	}
	
}
