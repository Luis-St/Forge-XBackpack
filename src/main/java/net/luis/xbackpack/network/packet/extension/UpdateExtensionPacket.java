package net.luis.xbackpack.network.packet.extension;

import java.util.function.Supplier;

import net.luis.xbackpack.network.NetworkPacket;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

/**
 * 
 * @author Luis-st
 *
 */

public class UpdateExtensionPacket implements NetworkPacket {
	
	private final BackpackExtension extension;
	
	public UpdateExtensionPacket(BackpackExtension extension) {
		this.extension = extension;
	}
	
	public UpdateExtensionPacket(FriendlyByteBuf buffer) {
		this.extension = BackpackExtensions.REGISTRY.get().getValue(buffer.readResourceLocation());
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(BackpackExtensions.REGISTRY.get().getKey(this.extension));
	}
	
	@Override
	public void handle(Supplier<Context> context) {
		ServerPlayer player = context.get().getSender();
		context.get().enqueueWork(() -> {
			if (player.containerMenu instanceof BackpackMenu menu) {
				menu.setExtension(this.extension);
			}
		});
	}
	
}