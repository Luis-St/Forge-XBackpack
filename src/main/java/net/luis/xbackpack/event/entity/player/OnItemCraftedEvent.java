package net.luis.xbackpack.event.entity.player;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 *
 * @author Luis-st
 *
 */

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID)
public class OnItemCraftedEvent {
	
	@SubscribeEvent
	public static void itemCrafted(ItemCraftedEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			BackpackProvider.get(player).broadcastChanges();
		}
	}
	
}
