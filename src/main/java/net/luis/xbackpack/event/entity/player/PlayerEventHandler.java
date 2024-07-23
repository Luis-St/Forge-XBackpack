/*
 * XBackpack
 * Copyright (C) 2024 Luis Staudt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.luis.xbackpack.event.entity.player;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.minecraft.core.RegistryAccess;
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
	public static void playerChangedDimension(PlayerEvent.@NotNull PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			BackpackProvider.get(player).broadcastChanges();
		}
	}
	
	@SubscribeEvent
	public static void playerClone(PlayerEvent.@NotNull Clone event) {
		Player original = event.getOriginal();
		Player player = event.getEntity();
		original.reviveCaps();
		original.getCapability(BackpackProvider.BACKPACK, null).ifPresent(oldBackpack -> {
			player.getCapability(BackpackProvider.BACKPACK, null).ifPresent(newBackpack -> {
				RegistryAccess access = original.registryAccess();
				newBackpack.deserialize(access, oldBackpack.serialize(access));
			});
		});
		original.invalidateCaps();
	}
	
	@SubscribeEvent
	public static void playerRespawn(PlayerEvent.@NotNull PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			BackpackProvider.get(player).broadcastChanges();
		}
	}
	
	@SubscribeEvent
	public static void playerTick(TickEvent.@NotNull PlayerTickEvent event) {
		if (event.phase == Phase.START && event.side == LogicalSide.SERVER) {
			event.player.getCapability(BackpackProvider.BACKPACK, null).ifPresent(IBackpack::tick);
			if (event.player.containerMenu instanceof BackpackMenu menu) {
				menu.tick();
			}
		}
	}
}