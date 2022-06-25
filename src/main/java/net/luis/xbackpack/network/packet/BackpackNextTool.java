package net.luis.xbackpack.network.packet;

import java.util.function.Supplier;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.capability.XBackpackCapabilities;
import net.luis.xbackpack.world.inventory.BackpackToolSlot;
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

public class BackpackNextTool {
	
	/**
	 * encode the data of the {@link BackpackNextTool} packet,<br>
	 * at the moment no data to encode
	 */
	public static void encode(BackpackNextTool packet, FriendlyByteBuf byteBuf) {
		
	}
	
	/**
	 * decode the data of the {@link BackpackNextTool} packet,<br>
	 * at the moment no data to decode
	 */
	public static BackpackNextTool decode(FriendlyByteBuf byteBuf) {
		return new BackpackNextTool();
	}
	
	/**
	 * handle the {@link BackpackNextTool} packet on server<br>
	 * <br>
	 * cycle the {@link ItemStack} downwards:
	 * <ol>
	 * 	<li>{@link Player#getMainHandItem()}</li>
	 * 	<li>upper {@link BackpackToolSlot}</li>
	 * 	<li>lower {@link BackpackToolSlot}</li>
	 * </ol>
	 * contains a simple anti cheat,<br> 
	 * which checks if the {@link ItemStack} is valid for the {@link BackpackToolSlot}
	 */
	public static void handle(BackpackNextTool packet, Supplier<Context> context) {
		ServerPlayer player = context.get().getSender();
		context.get().enqueueWork(() -> {
			IBackpack backpack = player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new);
			ItemStack main = player.getMainHandItem().copy();
			ItemStack top = backpack.getStackInSlot(36).copy();
			ItemStack down = backpack.getStackInSlot(37).copy();
			if (BackpackConstans.VALID_TOOL_SLOT_ITEMS.contains(main.getItem())) {
				player.setItemInHand(InteractionHand.MAIN_HAND, down);
				backpack.setStackInSlot(36, main);
				backpack.setStackInSlot(37, top);
			}
		});
		context.get().setPacketHandled(true);
	}
	
}
