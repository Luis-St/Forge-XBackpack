package net.luis.xbackpack.network.packet.extension;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.luis.xbackpack.network.NetworkPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-St
 *
 */

public class UpdateAnvilPacket implements NetworkPacket {
	
	private final int cost;
	
	public UpdateAnvilPacket(int cost) {
		this.cost = cost;
	}
	
	public UpdateAnvilPacket(@NotNull FriendlyByteBuf buffer) {
		this.cost = buffer.readInt();
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeInt(this.cost);
	}
	
	@Override
	public void handle(@NotNull CustomPayloadEvent.Context context) {
		context.enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateAnvilExtension(this.cost);
			});
		});
	}
}
