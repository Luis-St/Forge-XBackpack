package net.luis.xbackpack.event.player;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.common.capability.BackpackHandler;
import net.luis.xbackpack.init.XBackpackCapabilities;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 
 * @author Luis-st
 *
 */

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID)
public class OnPlayerCloneEvent {
	
	@SubscribeEvent
	public static void PlayerClone(PlayerEvent.Clone event) {
		Player original = event.getOriginal();
		Player player = event.getPlayer();
		original.reviveCaps(); // required since 1.17
		original.getCapability(XBackpackCapabilities.BACKPACK, null).ifPresent(oldBackpack -> {
			player.getCapability(XBackpackCapabilities.BACKPACK, null).ifPresent(newBackpack -> {
				if (oldBackpack instanceof BackpackHandler && newBackpack instanceof BackpackHandler) {
					((BackpackHandler) newBackpack).deserializeNBT(((BackpackHandler) oldBackpack).serializeNBT());
				} else {
					XBackpack.LOGGER.error("Fail to clone Backpack for {}", event.getOriginal().getName().getString());
				}
			});
		});
		original.invalidateCaps(); // required since 1.17
	}

}
