package net.luis.xbackpack.network.packet.tool.direct;

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

public class ToolTopPacket implements NetworkPacket {
	
	public ToolTopPacket() {
		
	}
	
	public ToolTopPacket(FriendlyByteBuf buffer) {
		
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
			if (BackpackConstants.VALID_TOOL_SLOT_ITEMS.contains(main.getItem())) {
				player.setItemInHand(InteractionHand.MAIN_HAND, top);
				backpack.getToolHandler().setStackInSlot(0, main);
			}
		});
	}
}
