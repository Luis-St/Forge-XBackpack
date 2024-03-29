package net.luis.xbackpack.network.packet.tool.next;

import net.luis.xbackpack.BackpackConstants;
import net.luis.xbackpack.network.NetworkPacket;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.capability.IBackpack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 *
 * @author Luis-St
 *
 */

public class NextToolDownPacket implements NetworkPacket {
	
	public NextToolDownPacket() {
		
	}
	
	public NextToolDownPacket(FriendlyByteBuf buffer) {
		
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		
	}
	
	@Override
	public void handle(@NotNull CustomPayloadEvent.Context context) {
		ServerPlayer player = context.getSender();
		context.enqueueWork(() -> {
			IBackpack backpack = BackpackProvider.get(Objects.requireNonNull(player));
			ItemStack main = player.getMainHandItem().copy();
			ItemStack top = backpack.getToolHandler().getStackInSlot(0).copy();
			ItemStack mid = backpack.getToolHandler().getStackInSlot(1).copy();
			ItemStack down = backpack.getToolHandler().getStackInSlot(2).copy();
			if (BackpackConstants.VALID_TOOL_SLOT_ITEMS.contains(main.getItem())) {
				backpack.getToolHandler().setStackInSlot(0, main);
				backpack.getToolHandler().setStackInSlot(1, top);
				backpack.getToolHandler().setStackInSlot(2, mid);
				player.setItemInHand(InteractionHand.MAIN_HAND, down);
			}
		});
	}
}
