package net.luis.xbackpack.network.packet.extension;

import java.util.function.Supplier;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class UpdateBrewingStandExtension {
	
	private final int fuel;
	private final int brewTime;
	
	public UpdateBrewingStandExtension(int fuel, int brewTime) {
		this.fuel = fuel;
		this.brewTime = brewTime;
	}
	
	public UpdateBrewingStandExtension(FriendlyByteBuf buffer) {
		this.fuel = buffer.readInt();
		this.brewTime = buffer.readInt();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.fuel);
		buffer.writeInt(this.brewTime);
	}
	
	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateBrewingStandExtension(this.fuel, this.brewTime);
			});
		});
	}
	
}
