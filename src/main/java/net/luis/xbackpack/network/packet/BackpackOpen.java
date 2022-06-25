package net.luis.xbackpack.network.packet;

import java.util.function.Supplier;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkHooks;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackOpen {
	
	/**
	 * {@link BackpackMenu} component
	 */
	private static final Component CONTAINER_NAME = Component.translatable(XBackpack.MOD_ID + ".container.backpack");
	
	/**
	 * encode the data of the {@link BackpackOpen} packet,<br>
	 * at the moment no data to encode
	 */
	public static void encode(BackpackOpen packet, FriendlyByteBuf byteBuf) {
		
	}
	
	/**
	 * decode the data of the {@link BackpackNextTool} packet,<br>
	 * at the moment no data to decode
	 */
	public static BackpackOpen decode(FriendlyByteBuf byteBuf) {
		return new BackpackOpen();
	}

	/**
	 * handle the {@link BackpackOpen} packet on server<br>
	 * <br>
	 * open the {@link BackpackMenu} via {@link NetworkHooks#openGui()}<br>
	 * <br>
	 * no anti cheat necessary,<br> 
	 * since the {@link Player} can always open the Backpack
	 */
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
