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

public class UpdateFurnacePacket implements NetworkPacket {
	
	private final int cookingProgress;
	private final int fuelProgress;
	
	public UpdateFurnacePacket(int cookingProgress, int fuelProgress) {
		this.cookingProgress = cookingProgress;
		this.fuelProgress = fuelProgress;
	}
	
	public UpdateFurnacePacket(@NotNull FriendlyByteBuf buffer) {
		this.cookingProgress = buffer.readInt();
		this.fuelProgress = buffer.readInt();
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeInt(this.cookingProgress);
		buffer.writeInt(this.fuelProgress);
	}
	
	@Override
	public void handle(@NotNull CustomPayloadEvent.Context context) {
		context.enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateFurnaceExtension(this.cookingProgress, this.fuelProgress);
			});
		});
	}
}
