package net.luis.xbackpack.network.packet;

import java.util.function.Supplier;

import net.luis.xbackpack.common.BackpackConstans;
import net.luis.xbackpack.init.XBackpackCapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.network.NetworkEvent.Context;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackToolTop {
	
	public static void encode(BackpackToolTop packet, FriendlyByteBuf byteBuf) {
		// no data to encode
	}
	
	public static BackpackToolTop decode(FriendlyByteBuf byteBuf) {
		return new BackpackToolTop(); // no data to decode
	}
	
	public static void handle(BackpackToolTop packet, Supplier<Context> context) {
		ServerPlayer player = context.get().getSender();
		context.get().enqueueWork(() -> {
			IItemHandlerModifiable itemModifiable = player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new);
			ItemStack main = player.getMainHandItem().copy();
			ItemStack top = itemModifiable.getStackInSlot(36).copy();	
			if (BackpackConstans.VALID_TOOL_SLOT_ITEMS.contains(main.getItem())) { // simple anti cheat
				player.setItemInHand(InteractionHand.MAIN_HAND, top);
				itemModifiable.setStackInSlot(36, main);
			}
		});
		context.get().setPacketHandled(true);
	}
	
}
