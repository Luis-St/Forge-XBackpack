package net.luis.xbackpack.event.entity;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.network.packet.UpdateBackpack;
import net.luis.xbackpack.world.capability.XBCapabilities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

/**
 * 
 * @author Luis-st
 *
 */

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID)
public class OnEntityJoinLevelEvent {
	
	@SubscribeEvent
	public static void entityJoinLevel(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			XBNetworkHandler.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new UpdateBackpack(player.getCapability(XBCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new).serialize()));
		}
	}

}
