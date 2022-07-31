package net.luis.xbackpack.event.player;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.capability.XBackpackCapabilities;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

/**
 * 
 * @author Luis-st
 *
 */

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID)
public class OnPlayerTickEvent {
	
	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == Phase.START && event.side == LogicalSide.SERVER) {
			event.player.getCapability(XBackpackCapabilities.BACKPACK, null).ifPresent(IBackpack::tick);
			if (event.player.containerMenu instanceof BackpackMenu menu) {
				if (menu.requiresTickUpdate()) {
					menu.tick();
				}
			}
		}
	}

}
