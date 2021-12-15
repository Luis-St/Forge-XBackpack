package net.luis.xbackpack.event.client.input;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.common.BackpackConstans;
import net.luis.xbackpack.common.capability.IBackpack;
import net.luis.xbackpack.init.XBackpackCapabilities;
import net.luis.xbackpack.network.XBackpackNetworkHandler;
import net.luis.xbackpack.network.packet.BackpackNextToolDown;
import net.luis.xbackpack.network.packet.BackpackNextToolTop;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 
 * @author Luis-st
 *
 */

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID, value = Dist.CLIENT)
public class OnMouseScrollEvent {

	@SubscribeEvent
	@SuppressWarnings("resource")
	public static void MouseScroll(InputEvent.MouseScrollEvent event) {
		double delta = event.getScrollDelta();
		Player player = Minecraft.getInstance().player;
		if (player.isShiftKeyDown()) {
			ItemStack main = player.getMainHandItem();
			if (BackpackConstans.VALID_TOOL_SLOT_ITEMS.contains(main.getItem())) {
				IBackpack backpack = player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new);
				ItemStack top = backpack.getStackInSlot(36).copy();
				ItemStack down = backpack.getStackInSlot(37).copy();
				if (!top.isEmpty() && !down.isEmpty()) {
					if (delta > 0) {
						event.setCanceled(true);
						XBackpackNetworkHandler.getChannel().sendToServer(new BackpackNextToolTop());
					} else if (delta < 0) {
						event.setCanceled(true);
						XBackpackNetworkHandler.getChannel().sendToServer(new BackpackNextToolDown());
					}
				}
			}
		}
	}
	
}
