package net.luis.xbackpack.event.entity.player;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.network.XBackpackNetworkHandler;
import net.luis.xbackpack.network.packet.UpdateBackpack;
import net.luis.xbackpack.world.capability.XBackpackCapabilities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

/**
 * 
 * @author Luis-st
 *
 */

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID)
public class OnPlayerRespawnEvent {
	
	@SubscribeEvent
	public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			XBackpackNetworkHandler.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new UpdateBackpack(player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new).serialize()));
		}
	}

}
