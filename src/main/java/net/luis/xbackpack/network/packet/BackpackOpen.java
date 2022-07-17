package net.luis.xbackpack.network.packet;

import java.util.function.Supplier;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkHooks;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackOpen {
	
	private static final Component CONTAINER_NAME = Component.translatable(XBackpack.MOD_ID + ".container.backpack");
	
	public BackpackOpen() {
		
	}
	
	public BackpackOpen(FriendlyByteBuf buffer) {
		
	}
	
	public void encode(FriendlyByteBuf buffer) {
		
	}
	
	public void handle(Supplier<Context> context) {
		ServerPlayer player = context.get().getSender();
		context.get().enqueueWork(() -> {
			NetworkHooks.openScreen(player, new SimpleMenuProvider((id, inventory, playerIn) -> {
				return new BackpackMenu(id, inventory);
			}, CONTAINER_NAME));
		});
	}
	
}
