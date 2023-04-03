package net.luis.xbackpack.network.packet.modifier;

import net.luis.xbackpack.network.NetworkPacket;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.modifier.ItemModifierType;
import net.luis.xbackpack.world.inventory.modifier.ModifiableMenu.UpdateType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 *
 * @author Luis-st
 *
 */

public class ResetItemModifierPacket implements NetworkPacket {
	
	private final ItemModifierType modifierType;
	
	public ResetItemModifierPacket(ItemModifierType modifierType) {
		this.modifierType = modifierType;
	}
	
	public ResetItemModifierPacket(@NotNull FriendlyByteBuf buffer) {
		this.modifierType = ItemModifierType.byId(buffer.readInt());
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeInt(this.modifierType.getId());
	}
	
	@Override
	public void handle(@NotNull Supplier<Context> context) {
		ServerPlayer player = context.get().getSender();
		context.get().enqueueWork(() -> {
			if (player.containerMenu instanceof BackpackMenu menu) {
				if (this.modifierType == ItemModifierType.FILTER) {
					menu.updateFilter(null, UpdateType.RESET, null);
				} else if (this.modifierType == ItemModifierType.SORTER) {
					menu.updateSorter(null, UpdateType.RESET, null);
				}
			}
		});
	}
	
}
