package net.luis.xbackpack.network.packet.extension;

import net.luis.xbackpack.network.NetworkPacket;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 *
 * @author Luis-St
 *
 */

public class UpdateExtensionPacket implements NetworkPacket {
	
	private final BackpackExtension extension;
	
	public UpdateExtensionPacket(BackpackExtension extension) {
		this.extension = extension;
	}
	
	public UpdateExtensionPacket(@NotNull FriendlyByteBuf buffer) {
		this.extension = BackpackExtensions.REGISTRY.get().getValue(buffer.readResourceLocation());
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(Objects.requireNonNull(BackpackExtensions.REGISTRY.get().getKey(this.extension)));
	}
	
	@Override
	public void handle(@NotNull CustomPayloadEvent.Context context) {
		ServerPlayer player = context.getSender();
		context.enqueueWork(() -> {
			if (player.containerMenu instanceof BackpackMenu menu) {
				menu.setExtension(this.extension);
			}
		});
	}
}
