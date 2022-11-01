package net.luis.xbackpack.event.entity;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 *
 * @author Luis-st
 *
 */

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID)
public class EntityEventHandler {
	
	@SubscribeEvent
	public static void entityJoinLevel(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			BackpackProvider.get(player).broadcastChanges();
		}
	}
	
}