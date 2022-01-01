package net.luis.xbackpack.network.packet;

import java.util.function.Supplier;

import net.luis.xbackpack.common.BackpackConstans;
import net.luis.xbackpack.common.capability.IBackpack;
import net.luis.xbackpack.init.XBackpackCapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackNextTool {
	
	public static void encode(BackpackNextTool packet, FriendlyByteBuf byteBuf) {
		// no data to encode
	}
	
	public static BackpackNextTool decode(FriendlyByteBuf byteBuf) {
		return new BackpackNextTool(); // no data to decode
	}
	
	public static void handle(BackpackNextTool packet, Supplier<Context> context) {
		ServerPlayer player = context.get().getSender();
		context.get().enqueueWork(() -> {
			IBackpack backpack = player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new);
			ItemStack main = player.getMainHandItem().copy();
			ItemStack top = backpack.getStackInSlot(36).copy();
			ItemStack down = backpack.getStackInSlot(37).copy();
			if (BackpackConstans.VALID_TOOL_SLOT_ITEMS.contains(main.getItem())) { // simple anti cheat
				player.setItemInHand(InteractionHand.MAIN_HAND, down);
				backpack.setStackInSlot(36, main);
				backpack.setStackInSlot(37, top);
			}
		});
		context.get().setPacketHandled(true);
	}
	
}
