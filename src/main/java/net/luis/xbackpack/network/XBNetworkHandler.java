package net.luis.xbackpack.network;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.network.packet.OpenBackpackPacket;
import net.luis.xbackpack.network.packet.UpdateBackpackPacket;
import net.luis.xbackpack.network.packet.extension.*;
import net.luis.xbackpack.network.packet.modifier.ResetItemModifierPacket;
import net.luis.xbackpack.network.packet.modifier.UpdateItemModifiersPacket;
import net.luis.xbackpack.network.packet.modifier.UpdateSearchTermPacket;
import net.luis.xbackpack.network.packet.tool.direct.ToolDownPacket;
import net.luis.xbackpack.network.packet.tool.direct.ToolMidPacket;
import net.luis.xbackpack.network.packet.tool.direct.ToolTopPacket;
import net.luis.xbackpack.network.packet.tool.next.NextToolDownPacket;
import net.luis.xbackpack.network.packet.tool.next.NextToolPacket;
import net.luis.xbackpack.network.packet.tool.next.NextToolTopPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author Luis-st
 *
 */

public enum XBNetworkHandler {
	
	INSTANCE();
	
	private static final String VERSION = "5";
	private int id = 0;
	private SimpleChannel simpleChannel;
	
	public void initChannel() {
		this.simpleChannel = NetworkRegistry.newSimpleChannel(new ResourceLocation(XBackpack.MOD_ID, "simple_chnanel"), () -> VERSION, VERSION::equals, VERSION::equals);
	}
	
	public void registerPackets() {
		this.registerPacket(OpenBackpackPacket.class, NetworkDirection.PLAY_TO_SERVER, OpenBackpackPacket::encode, OpenBackpackPacket::new, OpenBackpackPacket::handle);
		this.registerPacket(UpdateBackpackPacket.class, NetworkDirection.PLAY_TO_CLIENT, UpdateBackpackPacket::encode, UpdateBackpackPacket::new, UpdateBackpackPacket::handle);
		this.registerPacket(NextToolPacket.class, NetworkDirection.PLAY_TO_SERVER, NextToolPacket::encode, NextToolPacket::new, NextToolPacket::handle);
		this.registerPacket(ToolTopPacket.class, NetworkDirection.PLAY_TO_SERVER, ToolTopPacket::encode, ToolTopPacket::new, ToolTopPacket::handle);
		this.registerPacket(ToolMidPacket.class, NetworkDirection.PLAY_TO_SERVER, ToolMidPacket::encode, ToolMidPacket::new, ToolMidPacket::handle);
		this.registerPacket(ToolDownPacket.class, NetworkDirection.PLAY_TO_SERVER, ToolDownPacket::encode, ToolDownPacket::new, ToolDownPacket::handle);
		this.registerPacket(NextToolTopPacket.class, NetworkDirection.PLAY_TO_SERVER, NextToolTopPacket::encode, NextToolTopPacket::new, NextToolTopPacket::handle);
		this.registerPacket(NextToolDownPacket.class, NetworkDirection.PLAY_TO_SERVER, NextToolDownPacket::encode, NextToolDownPacket::new, NextToolDownPacket::handle);
		this.registerPacket(UpdateExtensionPacket.class, NetworkDirection.PLAY_TO_SERVER, UpdateExtensionPacket::encode, UpdateExtensionPacket::new, UpdateExtensionPacket::handle);
		this.registerPacket(UpdateFurnacePacket.class, NetworkDirection.PLAY_TO_CLIENT, UpdateFurnacePacket::encode, UpdateFurnacePacket::new, UpdateFurnacePacket::handle);
		this.registerPacket(UpdateAnvilPacket.class, NetworkDirection.PLAY_TO_CLIENT, UpdateAnvilPacket::encode, UpdateAnvilPacket::new, UpdateAnvilPacket::handle);
		this.registerPacket(UpdateEnchantmentTablePacket.class, NetworkDirection.PLAY_TO_CLIENT, UpdateEnchantmentTablePacket::encode, UpdateEnchantmentTablePacket::new, UpdateEnchantmentTablePacket::handle);
		this.registerPacket(UpdateStonecutterPacket.class, NetworkDirection.PLAY_TO_CLIENT, UpdateStonecutterPacket::encode, UpdateStonecutterPacket::new, UpdateStonecutterPacket::handle);
		this.registerPacket(UpdateBrewingStandPacket.class, NetworkDirection.PLAY_TO_CLIENT, UpdateBrewingStandPacket::encode, UpdateBrewingStandPacket::new, UpdateBrewingStandPacket::handle);
		this.registerPacket(UpdateSearchTermPacket.class, NetworkDirection.PLAY_TO_SERVER, UpdateSearchTermPacket::encode, UpdateSearchTermPacket::new, UpdateSearchTermPacket::handle);
		this.registerPacket(UpdateItemModifiersPacket.class, NetworkDirection.PLAY_TO_CLIENT, UpdateItemModifiersPacket::encode, UpdateItemModifiersPacket::new, UpdateItemModifiersPacket::handle);
		this.registerPacket(ResetItemModifierPacket.class, NetworkDirection.PLAY_TO_SERVER, ResetItemModifierPacket::encode, ResetItemModifierPacket::new, ResetItemModifierPacket::handle);
	}
	
	private <T extends NetworkPacket> void registerPacket(Class<T> clazz, NetworkDirection direction, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> consumer) {
		this.simpleChannel.messageBuilder(clazz, id++, direction).encoder(encoder).decoder(decoder).consumerMainThread(consumer).add();
	}
	
	private SimpleChannel getChannel() {
		return this.simpleChannel;
	}
	
	public <T extends NetworkPacket> void sendToServer(T packet) {
		this.getChannel().sendToServer(packet);
	}
	
	public <T extends NetworkPacket> void sendToPlayer(Player player, T packet) {
		if (player instanceof ServerPlayer serverPlayer) {
			this.sendToPlayer(serverPlayer, packet);
		}
	}
	
	public <T extends NetworkPacket> void sendToPlayer(ServerPlayer player, T packet) {
		this.getChannel().send(PacketDistributor.PLAYER.with(() -> player), packet);
	}
	
}
