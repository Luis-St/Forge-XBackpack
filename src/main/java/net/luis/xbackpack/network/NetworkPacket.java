package net.luis.xbackpack.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

/**
 *
 * @author Luis-st
 *
 */

public interface NetworkPacket {
	
	void encode(FriendlyByteBuf buffer);
	
	void handle(Supplier<Context> context);
	
}
