package net.luis.xbackpack.network.packet;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent.Context;

/**
 * 
 * @author Luis-st
 *
 */

public class UpdateBackpack {
	
	private final CompoundTag tag;
	private final List<ResourceLocation> usableExtensions;
	
	public UpdateBackpack(CompoundTag tag, List<BackpackExtension> usableExtensions) {
		this.tag = tag;
		this.usableExtensions = usableExtensions.stream().map(BackpackExtension.REGISTRY.get()::getKey).collect(Collectors.toList());
	}
	
	public UpdateBackpack(FriendlyByteBuf buffer) {
		this.tag = buffer.readAnySizeNbt();
		this.usableExtensions = buffer.readList(FriendlyByteBuf::readResourceLocation);
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeNbt(this.tag);
		buffer.writeCollection(this.usableExtensions, FriendlyByteBuf::writeResourceLocation);
	}
	
	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			XBClientPacketHandler.updateBackpack(this.tag, this.usableExtensions.stream().map(BackpackExtension.REGISTRY.get()::getValue).collect(Collectors.toList()));
		});
	}
	
}
