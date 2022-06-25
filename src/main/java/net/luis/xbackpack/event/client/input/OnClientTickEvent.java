package net.luis.xbackpack.event.client.input;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.client.XBackpackKeyMappings;
import net.luis.xbackpack.network.XBackpackNetworkHandler;
import net.luis.xbackpack.network.packet.BackpackNextTool;
import net.luis.xbackpack.network.packet.BackpackOpen;
import net.luis.xbackpack.network.packet.BackpackToolDown;
import net.luis.xbackpack.network.packet.BackpackToolTop;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 
 * @author Luis-st
 *
 */

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID, value = Dist.CLIENT)
public class OnClientTickEvent {
	
	/**
	 * small cooldown for the Tool cycle
	 */
	private static int lastPacket;
	
	/**
	 * handle the {@link XBackpackKeyMappings},<br>
	 * and send if the {@link KeyMapping#isDown()} returns {@code true}<br>
	 * the related packet via the {@link XBackpackNetworkHandler#getChannel()} to the server
	 */
	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.START) {
			if (0 >= lastPacket) {
				if (XBackpackKeyMappings.BACKPACK_OPEN.isDown()) {
					XBackpackNetworkHandler.getChannel().sendToServer(new BackpackOpen());
					lastPacket = 2;
				} else if (XBackpackKeyMappings.BACKPACK_NEXT.isDown()) {
					XBackpackNetworkHandler.getChannel().sendToServer(new BackpackNextTool());
					lastPacket = 2;
				} else if (XBackpackKeyMappings.BACKPACK_SLOT_TOP.isDown()) {
					XBackpackNetworkHandler.getChannel().sendToServer(new BackpackToolTop());
					lastPacket = 2;
				} else if (XBackpackKeyMappings.BACKPACK_SLOT_DOWN.isDown()) {
					XBackpackNetworkHandler.getChannel().sendToServer(new BackpackToolDown());
					lastPacket = 2;
				}
			} else {
				lastPacket--;
			}
		}
	}
	
}