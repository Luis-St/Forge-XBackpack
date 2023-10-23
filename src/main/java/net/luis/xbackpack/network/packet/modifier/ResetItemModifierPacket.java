package net.luis.xbackpack.network.packet.modifier;

import net.luis.xbackpack.network.NetworkPacket;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.modifier.ItemModifierType;
import net.luis.xbackpack.world.inventory.modifier.ModifiableMenu.UpdateType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-St
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
	public void handle(@NotNull CustomPayloadEvent.Context context) {
		ServerPlayer player = context.getSender();
		context.enqueueWork(() -> {
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
