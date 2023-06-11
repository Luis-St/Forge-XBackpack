package net.luis.xbackpack.network.packet.modifier;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.luis.xbackpack.network.NetworkPacket;
import net.luis.xbackpack.world.inventory.modifier.filter.ItemFilter;
import net.luis.xbackpack.world.inventory.modifier.filter.ItemFilters;
import net.luis.xbackpack.world.inventory.modifier.sorter.ItemSorter;
import net.luis.xbackpack.world.inventory.modifier.sorter.ItemSorters;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 *
 * @author Luis-st
 *
 */

public class UpdateItemModifiersPacket implements NetworkPacket {
	
	private final ItemFilter filter;
	private final ItemSorter sorter;
	
	public UpdateItemModifiersPacket(ItemFilter filter, ItemSorter sorter) {
		this.filter = filter;
		this.sorter = sorter;
	}
	
	public UpdateItemModifiersPacket(@NotNull FriendlyByteBuf buffer) {
		this.filter = ItemFilters.byId(buffer.readInt(), ItemFilters.NONE);
		this.sorter = ItemSorters.byId(buffer.readInt(), ItemSorters.NONE);
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeInt(this.filter.getId());
		buffer.writeInt(this.sorter.getId());
	}
	
	@Override
	public void handle(@NotNull Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateBackpackItemModifiers(this.filter, this.sorter);
			});
		});
	}
}
