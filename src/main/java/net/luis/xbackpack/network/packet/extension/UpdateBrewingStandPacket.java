package net.luis.xbackpack.network.packet.extension;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.luis.xbackpack.network.NetworkPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

public class UpdateBrewingStandPacket implements NetworkPacket {
	
	private final int fuel;
	private final int brewTime;
	
	public UpdateBrewingStandPacket(int fuel, int brewTime) {
		this.fuel = fuel;
		this.brewTime = brewTime;
	}
	
	public UpdateBrewingStandPacket(@NotNull FriendlyByteBuf buffer) {
		this.fuel = buffer.readInt();
		this.brewTime = buffer.readInt();
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeInt(this.fuel);
		buffer.writeInt(this.brewTime);
	}
	
	@Override
	public void handle(@NotNull CustomPayloadEvent.Context context) {
		context.enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateBrewingStandExtension(this.fuel, this.brewTime);
			});
		});
	}
}
