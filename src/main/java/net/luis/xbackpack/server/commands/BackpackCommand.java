package net.luis.xbackpack.server.commands;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;

import net.luis.xbackpack.server.commands.arguments.BackpackExtensionArgument;
import net.luis.xbackpack.server.commands.arguments.ExtensionStateArgument;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.ExtensionState;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 *
 * @author Luis-st
 *
 */

public class BackpackCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
		dispatcher.register(Commands.literal("backpack").requires((source) -> {
			return source.hasPermission(2);
		}).then(Commands.argument("players", EntityArgument.players()).then(Commands.argument("extension", BackpackExtensionArgument.extension()).then(Commands.literal("get").executes((command) -> {
			return getExtensionState(command.getSource(), Lists.newArrayList(EntityArgument.getPlayers(command, "players")), BackpackExtensionArgument.get(command, "extension"));
		})).then(Commands.literal("set").then(Commands.argument("state", ExtensionStateArgument.state()).executes((command) -> {
			return setExtensionState(command.getSource(), Lists.newArrayList(EntityArgument.getPlayers(command, "players")), BackpackExtensionArgument.get(command, "extension"), ExtensionStateArgument.get(command, "state"));
		}))))));
	}
	
	private static int getExtensionState(CommandSourceStack source, List<ServerPlayer> players, BackpackExtension extension) {
		if (players.size() > 1) {
			source.sendFailure(Component.translatable("xbackpack.commands.backpack.get_failure"));
		} else {
			ServerPlayer player = players.get(0);
			source.sendSuccess(Component.translatable("xbackpack.commands.backpack.get_success", extension, player.getName().getString(), BackpackProvider.get(player).getConfig().getState(extension).getName()), false);
		}
		return 1;
	}
	
	private static int setExtensionState(CommandSourceStack source, List<ServerPlayer> players, BackpackExtension extension, ExtensionState state) {
		int i = 0;
		for (ServerPlayer player : players) {
			IBackpack backpack = BackpackProvider.get(player);
			if (backpack.getConfig().setState(extension, state)) {
				++i;
			}
			backpack.broadcastChanges();
		}
		if (players.size() > 0) {
			source.sendSuccess(Component.translatable("xbackpack.commands.backpack.set_success", extension, i, state.getName()), false);
		}
		return 1;
	}
	
}
