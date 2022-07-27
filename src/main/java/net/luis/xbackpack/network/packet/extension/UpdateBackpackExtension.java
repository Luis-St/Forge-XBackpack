package net.luis.xbackpack.network.packet.extension;

import java.util.function.Supplier;

import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

/**
 * 
 * @author Luis-st
 *
 */

public class UpdateBackpackExtension {
	
	private final BackpackExtension extension;
	
	public UpdateBackpackExtension(BackpackExtension extension) {
		this.extension = extension;
	}
	
	public UpdateBackpackExtension(FriendlyByteBuf buffer) {
		this.extension = BackpackExtension.REGISTRY.get().getValue(buffer.readResourceLocation());
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(BackpackExtension.REGISTRY.get().getKey(this.extension));
	}
	
	public void handle(Supplier<Context> context) {
		ServerPlayer player = context.get().getSender();
		context.get().enqueueWork(() -> {
			if (player.containerMenu instanceof BackpackMenu menu) {
				menu.setExtension(this.extension);
			}
		});
	}
	
}
