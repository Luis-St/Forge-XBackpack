package net.luis.xbackpack.event.capability;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 *
 * @author Luis-st
 *
 */

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID)
public class AttachCapabilitiesEventHandler {
	
	@SubscribeEvent
	public static void attachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof Player player) {
			event.addCapability(new ResourceLocation(XBackpack.MOD_ID, "backpack"), new BackpackProvider(player));
		}
	}
	
}