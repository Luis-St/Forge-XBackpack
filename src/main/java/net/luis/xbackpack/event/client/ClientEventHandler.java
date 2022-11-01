package net.luis.xbackpack.event.client;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.client.XBKeyMappings;
import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.network.packet.BackpackOpen;
import net.luis.xbackpack.network.packet.tool.BackpackNextTool;
import net.luis.xbackpack.network.packet.tool.BackpackNextToolDown;
import net.luis.xbackpack.network.packet.tool.BackpackNextToolTop;
import net.luis.xbackpack.network.packet.tool.BackpackToolDown;
import net.luis.xbackpack.network.packet.tool.BackpackToolMid;
import net.luis.xbackpack.network.packet.tool.BackpackToolTop;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.capability.IBackpack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.MouseScrollingEvent;
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
public class ClientEventHandler {
	
	public static int lastPacket;
	
	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.START) {
			if (0 >= lastPacket) {
				if (XBKeyMappings.BACKPACK_OPEN.isDown()) {
					XBNetworkHandler.sendToServer(new BackpackOpen());
					lastPacket = 4;
				} else if (XBKeyMappings.BACKPACK_NEXT.isDown()) {
					XBNetworkHandler.sendToServer(new BackpackNextTool());
					lastPacket = 4;
				} else if (XBKeyMappings.BACKPACK_SLOT_TOP.isDown()) {
					XBNetworkHandler.sendToServer(new BackpackToolTop());
					lastPacket = 4;
				} else if (XBKeyMappings.BACKPACK_SLOT_MID.isDown()) {
					XBNetworkHandler.sendToServer(new BackpackToolMid());
					lastPacket = 4;
				} else if (XBKeyMappings.BACKPACK_SLOT_DOWN.isDown()) {
					XBNetworkHandler.sendToServer(new BackpackToolDown());
					lastPacket = 4;
				}
			} else {
				lastPacket--;
			}
		}
	}
	
	@SubscribeEvent
	public static void mouseScroll(MouseScrollingEvent event) {
		double delta = event.getScrollDelta();
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		if (player.isShiftKeyDown() && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
			ItemStack main = player.getMainHandItem();
			if (BackpackConstans.VALID_TOOL_SLOT_ITEMS.contains(main.getItem())) {
				IBackpack backpack = BackpackProvider.get(player);
				ItemStack top = backpack.getToolHandler().getStackInSlot(0).copy();
				ItemStack down = backpack.getToolHandler().getStackInSlot(2).copy();
				if (!top.isEmpty() && !down.isEmpty()) {
					if (delta > 0) {
						event.setCanceled(true);
						XBNetworkHandler.sendToServer(new BackpackNextToolTop());
					} else if (delta < 0) {
						event.setCanceled(true);
						XBNetworkHandler.sendToServer(new BackpackNextToolDown());
					}
				}
			}
		}
	}
	
}
