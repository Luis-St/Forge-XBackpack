package net.luis.xbackpack.network.packet.extension;

import java.util.function.Supplier;

import net.luis.xbackpack.client.XBackpackClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class UpdateFurnaceExtension {
	
	private final int cookingProgress;
	private final int fuelProgress;
	
	public UpdateFurnaceExtension(int cookingProgress, int fuelProgress) {
		this.cookingProgress = cookingProgress;
		this.fuelProgress = fuelProgress;
	}
	
	public UpdateFurnaceExtension(FriendlyByteBuf buffer) {
		this.cookingProgress = buffer.readInt();
		this.fuelProgress = buffer.readInt();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.cookingProgress);
		buffer.writeInt(this.fuelProgress);
	}
	
	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBackpackClientPacketHandler.handleUpdateFurnaceExtension(this.cookingProgress, this.fuelProgress);
			});
		});
	}
	
}
