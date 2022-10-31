package net.luis.xbackpack.network.packet.modifier;

import java.util.function.Supplier;

import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.modifier.ItemModifierType;
import net.luis.xbackpack.world.inventory.modifier.ModifiableMenu.UpdateType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

/**
 *
 * @author Luis-st
 *
 */

public class ResetBackpackItemModifier {
	
	private final ItemModifierType modifierType;
	
	public ResetBackpackItemModifier(ItemModifierType modifierType) {
		this.modifierType = modifierType;
	}

	public ResetBackpackItemModifier(FriendlyByteBuf buffer) {
		this.modifierType = ItemModifierType.byId(buffer.readInt());
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.modifierType.getId());
	}
	
	public void handle(Supplier<Context> context) {
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
