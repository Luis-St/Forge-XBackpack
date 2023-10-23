package net.luis.xbackpack.network.packet;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.network.NetworkPacket;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-St
 *
 */

public class OpenBackpackPacket implements NetworkPacket {
	
	private static final Component CONTAINER_NAME = Component.translatable(XBackpack.MOD_ID + ".container.backpack");
	
	public OpenBackpackPacket() {
		
	}
	
	public OpenBackpackPacket(FriendlyByteBuf buffer) {
		
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		
	}
	
	@Override
	public void handle(@NotNull CustomPayloadEvent.Context context) {
		ServerPlayer player = context.getSender();
		context.enqueueWork(() -> {
			assert player != null;
			if (player.containerMenu == player.inventoryMenu) {
				player.openMenu(new SimpleMenuProvider((id, inventory, playerIn) -> new BackpackMenu(id, inventory), CONTAINER_NAME));
			}
		});
	}
}
