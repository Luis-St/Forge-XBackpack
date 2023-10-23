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

public class UpdateStonecutterPacket implements NetworkPacket {
	
	private final boolean resetSelected;
	
	public UpdateStonecutterPacket(boolean resetSelected) {
		this.resetSelected = resetSelected;
	}
	
	public UpdateStonecutterPacket(@NotNull FriendlyByteBuf buffer) {
		this.resetSelected = buffer.readBoolean();
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeBoolean(this.resetSelected);
	}
	
	@Override
	public void handle(@NotNull CustomPayloadEvent.Context context) {
		context.enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateStonecutterExtension(this.resetSelected);
			});
		});
	}
}
