package net.luis.xbackpack.network.packet.extension;

import java.util.function.Supplier;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.luis.xbackpack.network.NetworkPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class UpdateBrewingStandPacket implements NetworkPacket {
	
	private final int fuel;
	private final int brewTime;
	
	public UpdateBrewingStandPacket(int fuel, int brewTime) {
		this.fuel = fuel;
		this.brewTime = brewTime;
	}
	
	public UpdateBrewingStandPacket(FriendlyByteBuf buffer) {
		this.fuel = buffer.readInt();
		this.brewTime = buffer.readInt();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.fuel);
		buffer.writeInt(this.brewTime);
	}
	
	@Override
	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateBrewingStandExtension(this.fuel, this.brewTime);
			});
		});
	}
	
}
