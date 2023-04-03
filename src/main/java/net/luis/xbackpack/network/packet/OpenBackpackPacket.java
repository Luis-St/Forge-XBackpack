package net.luis.xbackpack.network.packet;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.network.NetworkPacket;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 *
 * @author Luis-st
 *
 */

public class OpenBackpackPacket implements NetworkPacket {
	
	private static final Component CONTAINER_NAME = Component.translatable(XBackpack.MOD_ID + ".container.backpack");
	
	public OpenBackpackPacket() {
		
	}
	
	public OpenBackpackPacket(FriendlyByteBuf buffer) {
		
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		
	}
	
	@Override
	public void handle(@NotNull Supplier<Context> context) {
		ServerPlayer player = context.get().getSender();
		context.get().enqueueWork(() -> {
			assert player != null;
			if (player.containerMenu == player.inventoryMenu) {
				NetworkHooks.openScreen(player, new SimpleMenuProvider((id, inventory, playerIn) -> new BackpackMenu(id, inventory), CONTAINER_NAME));
			}
		});
	}
	
}
