package net.luis.xbackpack.network.packet.extension;

import java.util.function.Supplier;

import net.luis.xbackpack.client.XBackpackClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class UpdateAnvilExtension {
	
	private final int cost;
	
	public UpdateAnvilExtension(int cost) {
		this.cost = cost;
	}
	
	public UpdateAnvilExtension(FriendlyByteBuf buffer) {
		this.cost = buffer.readInt();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.cost);
	}
	
	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBackpackClientPacketHandler.handleUpdateAnvilExtension(this.cost);
			});
		});
	}
	
}
