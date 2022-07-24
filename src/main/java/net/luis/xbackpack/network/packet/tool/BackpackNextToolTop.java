package net.luis.xbackpack.network.packet.tool;

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
	
	public BackpackNextToolTop() {
		
	}
	
	public BackpackNextToolTop(FriendlyByteBuf buffer) {
		
	}
	
	public void encode(FriendlyByteBuf buffer) {
		
	}
	
	public void handle(Supplier<Context> context) {
		ServerPlayer player = context.get().getSender();
		context.get().enqueueWork(() -> {
			IBackpack backpack = player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new);
			ItemStack main = player.getMainHandItem().copy();
			ItemStack top = backpack.getToolHandler().getStackInSlot(0).copy();
			ItemStack mid = backpack.getToolHandler().getStackInSlot(1).copy();
			ItemStack down = backpack.getToolHandler().getStackInSlot(2).copy();
			if (BackpackConstans.VALID_TOOL_SLOT_ITEMS.contains(main.getItem())) {
				backpack.getToolHandler().setStackInSlot(0, mid);
				backpack.getToolHandler().setStackInSlot(1, down);
				backpack.getToolHandler().setStackInSlot(2, main);
				player.setItemInHand(InteractionHand.MAIN_HAND, top);
			}
		});
	}
	
}
