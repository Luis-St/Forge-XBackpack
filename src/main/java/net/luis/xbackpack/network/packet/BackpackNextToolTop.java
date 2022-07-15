package net.luis.xbackpack.network.packet;

import java.util.function.Supplier;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.capability.XBackpackCapabilities;
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

public class BackpackNextToolTop {
	
	public static void encode(BackpackNextToolTop packet, FriendlyByteBuf byteBuf) {
		
	}
	
	public static BackpackNextToolTop decode(FriendlyByteBuf byteBuf) {
		return new BackpackNextToolTop();
	}
	
	public static void handle(BackpackNextToolTop packet, Supplier<Context> context) {
		ServerPlayer player = context.get().getSender();
		context.get().enqueueWork(() -> {
			IBackpack backpack = player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new);
			ItemStack main = player.getMainHandItem().copy();
			ItemStack top = backpack.getStackInSlot(BackpackConstans.BACKPACK_TOOL_SLOT_TOP).copy();
			ItemStack down = backpack.getStackInSlot(BackpackConstans.BACKPACK_TOOL_SLOT_DOWN).copy();
			if (BackpackConstans.VALID_TOOL_SLOT_ITEMS.contains(main.getItem())) {
				player.setItemInHand(InteractionHand.MAIN_HAND, top);
				backpack.setStackInSlot(BackpackConstans.BACKPACK_TOOL_SLOT_TOP, down);
				backpack.setStackInSlot(BackpackConstans.BACKPACK_TOOL_SLOT_DOWN, main);
			}
		});
		context.get().setPacketHandled(true);
	}
	
}
