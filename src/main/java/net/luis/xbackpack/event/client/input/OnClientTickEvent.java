package net.luis.xbackpack.event.client.input;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.client.XBKeyMappings;
import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.network.packet.BackpackOpen;
import net.luis.xbackpack.network.packet.tool.BackpackNextTool;
import net.luis.xbackpack.network.packet.tool.BackpackToolDown;
import net.luis.xbackpack.network.packet.tool.BackpackToolMid;
import net.luis.xbackpack.network.packet.tool.BackpackToolTop;
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
	
	private static int lastPacket;
	
	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.START) {
			if (0 >= lastPacket) {
				if (XBKeyMappings.BACKPACK_OPEN.isDown()) {
					XBNetworkHandler.getChannel().sendToServer(new BackpackOpen());
					lastPacket = 4;
				} else if (XBKeyMappings.BACKPACK_NEXT.isDown()) {
					XBNetworkHandler.getChannel().sendToServer(new BackpackNextTool());
					lastPacket = 4;
				} else if (XBKeyMappings.BACKPACK_SLOT_TOP.isDown()) {
					XBNetworkHandler.getChannel().sendToServer(new BackpackToolTop());
					lastPacket = 4;
				} else if (XBKeyMappings.BACKPACK_SLOT_MID.isDown()) {
					XBNetworkHandler.getChannel().sendToServer(new BackpackToolMid());
					lastPacket = 4;
				} else if (XBKeyMappings.BACKPACK_SLOT_DOWN.isDown()) {
					XBNetworkHandler.getChannel().sendToServer(new BackpackToolDown());
					lastPacket = 4;
				}
			} else {
				lastPacket--;
			}
		}
	}
	
}