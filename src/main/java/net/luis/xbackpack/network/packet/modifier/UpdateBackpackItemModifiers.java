package net.luis.xbackpack.network.packet.modifier;

import java.util.function.Supplier;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.luis.xbackpack.world.inventory.modifier.filter.ItemFilter;
import net.luis.xbackpack.world.inventory.modifier.filter.ItemFilters;
import net.luis.xbackpack.world.inventory.modifier.sorter.ItemSorter;
import net.luis.xbackpack.world.inventory.modifier.sorter.ItemSorters;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

/**
 *
 * @author Luis-st
 *
 */

public class UpdateBackpackItemModifiers {
	
	private final ItemFilter filter;
	private final ItemSorter sorter;
	
	public UpdateBackpackItemModifiers(ItemFilter filter, ItemSorter sorter) {
		this.filter = filter;
		this.sorter = sorter;
	}

	public UpdateBackpackItemModifiers(FriendlyByteBuf buffer) {
		this.filter = ItemFilters.byId(buffer.readInt(), ItemFilters.NONE);
		this.sorter = ItemSorters.byId(buffer.readInt(), ItemSorters.NONE);
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.filter.getId());
		buffer.writeInt(this.sorter.getId());
	}
	
	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateBackpackItemModifiers(this.filter, this.sorter);
			});
		});
	}
	
}
