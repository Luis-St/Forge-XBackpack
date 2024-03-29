package net.luis.xbackpack.event.entity.player;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-St
 *
 */

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID)
public class PlayerEventHandler {
	
	@SubscribeEvent
	public static void itemCrafted(@NotNull ItemCraftedEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			BackpackProvider.get(player).broadcastChanges();
		}
	}
	
	@SubscribeEvent
	public static void playerChangedDimension(@NotNull PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			BackpackProvider.get(player).broadcastChanges();
		}
	}
	
	@SubscribeEvent
	public static void playerClone(@NotNull PlayerEvent.Clone event) {
		Player original = event.getOriginal();
		Player player = event.getEntity();
		original.reviveCaps();
		original.getCapability(BackpackProvider.BACKPACK, null).ifPresent(oldBackpack -> {
			player.getCapability(BackpackProvider.BACKPACK, null).ifPresent(newBackpack -> {
				newBackpack.deserialize(oldBackpack.serialize());
			});
		});
		original.invalidateCaps();
	}
	
	@SubscribeEvent
	public static void playerRespawn(@NotNull PlayerEvent.PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			BackpackProvider.get(player).broadcastChanges();
		}
	}
	
	@SubscribeEvent
	public static void playerTick(@NotNull TickEvent.PlayerTickEvent event) {
		if (event.phase == Phase.START && event.side == LogicalSide.SERVER) {
			event.player.getCapability(BackpackProvider.BACKPACK, null).ifPresent(IBackpack::tick);
			if (event.player.containerMenu instanceof BackpackMenu menu) {
				menu.tick();
			}
		}
	}
}