package net.luis.xbackpack.network.packet;

import java.util.function.Supplier;

import net.luis.xbackpack.common.BackpackConstans;
import net.luis.xbackpack.common.capability.IBackpack;
import net.luis.xbackpack.common.inventory.slot.BackpackToolSlot;
import net.luis.xbackpack.init.XBackpackCapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackNextToolTop {
	
	/**
	 * encode the data of the {@link BackpackNextToolTop} packet,<br>
	 * at the moment no data to encode
	 */
	public static void encode(BackpackNextToolTop packet, FriendlyByteBuf byteBuf) {
		
	}
	
	/**
	 * decode the data of the {@link BackpackNextTool} packet,<br>
	 * at the moment no data to decode
	 */
	public static BackpackNextToolTop decode(FriendlyByteBuf byteBuf) {
		return new BackpackNextToolTop();
	}
	
	/**
	 * handle the {@link BackpackNextToolTop} packet on server<br>
	 * <br>
	 * cycle the {@link ItemStack} upwards:
	 * <ol>
	 * 	<li>{@link Player#getMainHandItem()}</li>
	 * 	<li>upper {@link BackpackToolSlot}</li>
	 * 	<li>lower {@link BackpackToolSlot}</li>
	 * </ol>
	 * contains a simple anti cheat,<br> 
	 * which checks if the {@link ItemStack} is valid for the {@link BackpackToolSlot}
	 */
	public static void handle(BackpackNextToolTop packet, Supplier<Context> context) {
		ServerPlayer player = context.get().getSender();
		context.get().enqueueWork(() -> {
			IBackpack backpack = player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new);
			ItemStack main = player.getMainHandItem().copy();
			ItemStack top = backpack.getStackInSlot(36).copy();
			ItemStack down = backpack.getStackInSlot(37).copy();
			if (BackpackConstans.VALID_TOOL_SLOT_ITEMS.contains(main.getItem())) {
				player.setItemInHand(InteractionHand.MAIN_HAND, top);
				backpack.setStackInSlot(36, down);
				backpack.setStackInSlot(37, main);
			}
		});
		context.get().setPacketHandled(true);
	}
	
}
