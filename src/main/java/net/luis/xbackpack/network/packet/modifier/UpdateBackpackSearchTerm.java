package net.luis.xbackpack.network.packet.modifier;

import java.util.function.Supplier;

import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

/**
 *
 * @author Luis-st
 *
 */

public class UpdateBackpackSearchTerm {
	
	private final String searchBoxValue;
	
	public UpdateBackpackSearchTerm(String searchBoxValue) {
		this.searchBoxValue = searchBoxValue;
	}
	
	public UpdateBackpackSearchTerm(FriendlyByteBuf buffer) {
		this.searchBoxValue = buffer.readUtf();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeUtf(this.searchBoxValue);
	}
	
	public void handle(Supplier<Context> context) {
		ServerPlayer player = context.get().getSender();
		context.get().enqueueWork(() -> {
			if (player.containerMenu instanceof BackpackMenu menu) {
				menu.setSearchTerm(this.searchBoxValue);
			}
		});
	}
	
}
