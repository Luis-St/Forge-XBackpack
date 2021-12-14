package net.luis.xbackpack.network.packet;

import java.util.function.Supplier;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.common.inventory.menu.BackpackMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkHooks;

public class BackpackOpen {
	
	private static final Component CONTAINER_NAME = new TranslatableComponent(XBackpack.MOD_ID + ".container.backpack");
	
	public static void encode(BackpackOpen packet, FriendlyByteBuf byteBuf) {
		
	}
	
	public static BackpackOpen decode(FriendlyByteBuf byteBuf) {
		return new BackpackOpen();
	}

	public static void handle(BackpackOpen packet, Supplier<Context> networkContext) {
		ServerPlayer player = networkContext.get().getSender();
		networkContext.get().enqueueWork(() -> {
			NetworkHooks.openGui(player, new SimpleMenuProvider((id, inventory, playerIn) -> {
				return new BackpackMenu(id, inventory);
			}, CONTAINER_NAME));
		});
		networkContext.get().setPacketHandled(true);
	}
	
}
