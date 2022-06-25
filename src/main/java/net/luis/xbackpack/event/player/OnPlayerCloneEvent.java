package net.luis.xbackpack.event.player;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.capability.BackpackHandler;
import net.luis.xbackpack.world.capability.XBackpackCapabilities;
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
	
	/**
	 * clone the {@link BackpackHandler} from the old {@link XBackpackCapabilities#BACKPACK}<br> 
	 * into the new Capability, when the {@link Player} die
	 */
	@SubscribeEvent
	public static void playerClone(PlayerEvent.Clone event) {
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
