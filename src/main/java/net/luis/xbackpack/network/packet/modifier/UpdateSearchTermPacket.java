package net.luis.xbackpack.network.packet.modifier;

import net.luis.xbackpack.network.NetworkPacket;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-St
 *
 */

public class UpdateSearchTermPacket implements NetworkPacket {
	
	private final String searchBoxValue;
	
	public UpdateSearchTermPacket(String searchBoxValue) {
		this.searchBoxValue = searchBoxValue;
	}
	
	public UpdateSearchTermPacket(@NotNull FriendlyByteBuf buffer) {
		this.searchBoxValue = buffer.readUtf();
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeUtf(this.searchBoxValue);
	}
	
	@Override
	public void handle(@NotNull CustomPayloadEvent.Context context) {
		ServerPlayer player = context.getSender();
		context.enqueueWork(() -> {
			if (player.containerMenu instanceof BackpackMenu menu) {
				menu.setSearchTerm(this.searchBoxValue);
			}
		});
	}
}
