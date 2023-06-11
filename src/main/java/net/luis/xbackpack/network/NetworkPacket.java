package net.luis.xbackpack.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

/**
 *
 * @author Luis-st
 *
 */

public interface NetworkPacket {
	
	void encode(FriendlyByteBuf buffer);
	
	void handle(Supplier<Context> context);
}
