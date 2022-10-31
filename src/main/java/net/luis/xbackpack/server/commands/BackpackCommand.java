package net.luis.xbackpack.server.commands;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;

import net.luis.xbackpack.server.commands.arguments.BackpackExtensionArgument;
import net.luis.xbackpack.server.commands.arguments.BackpackExtensionStateArgument;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensionState;
import net.luis.xbackpack.world.extension.BackpackExtensions;
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
		dispatcher.register(Commands.literal("backpack").then(Commands.literal("tooltip").then(Commands.argument("value", BoolArgumentType.bool()).executes((command) -> {
			return setTooltipVisibility(command.getSource(), BoolArgumentType.getBool(command, "value"));
		}))).requires((source) -> {
			return source.hasPermission(2);
		}).then(Commands.argument("player", EntityArgument.player()).then(Commands.literal("*").then(Commands.argument("state", BackpackExtensionStateArgument.state()).executes((command) -> {
			return setExtensionState(command.getSource(), EntityArgument.getPlayer(command, "player"), Lists.newArrayList(BackpackExtensions.REGISTRY.get().getValues()), BackpackExtensionStateArgument.get(command, "state"));
		}))).then(Commands.argument("extension", BackpackExtensionArgument.extension()).executes((command) -> {
			return getExtensionState(command.getSource(), EntityArgument.getPlayer(command, "player"), Lists.newArrayList(BackpackExtensionArgument.get(command, "extension")));
		}).then(Commands.argument("state", BackpackExtensionStateArgument.state()).executes((command) -> {
			return setExtensionState(command.getSource(), EntityArgument.getPlayer(command, "player"), Lists.newArrayList(BackpackExtensionArgument.get(command, "extension")), BackpackExtensionStateArgument.get(command, "state"));
		})))));
	}
	
	private static int setTooltipVisibility(CommandSourceStack source, boolean value) {
		if (source.getEntity() instanceof ServerPlayer player) {
			IBackpack backpack = BackpackProvider.get(player);
			backpack.getConfig().setShowModifierInfo(value);
			if (value) {
				source.sendSuccess(Component.translatable("xbackpack.commands.backpack.tooltip_success_true"), false);
			} else {
				source.sendSuccess(Component.translatable("xbackpack.commands.backpack.tooltip_success_false"), false);
			}
			backpack.broadcastChanges();
		} else {
			source.sendFailure(Component.translatable("xbackpack.commands.backpack.tooltip_failure"));
		}
		return 1;
	}
	
	private static int getExtensionState(CommandSourceStack source, ServerPlayer player, List<BackpackExtension> extensions) {
		if (extensions.size() > 1) {
			source.sendFailure(Component.translatable("xbackpack.commands.backpack.get_failure"));
		} else {
			
			source.sendSuccess(Component.translatable("xbackpack.commands.backpack.get_success", getName(extensions.get(0)), player.getName().getString(), BackpackProvider.get(player).getConfig().getExtensionConfig().getState(extensions.get(0)).getName()), false);	
		}
		return 1;
	}
	
	private static int setExtensionState(CommandSourceStack source, ServerPlayer player, List<BackpackExtension> extensions, BackpackExtensionState state) {
		int i = 0;
		IBackpack backpack = BackpackProvider.get(player);
		for (BackpackExtension extension : extensions) {
			backpack.getConfig().getExtensionConfig().setState(player, extension, state);
			++i;
		}
		backpack.broadcastChanges();
		if (i == 1) {
			source.sendSuccess(Component.translatable("xbackpack.commands.backpack.set_success.single", getName(extensions.get(0)), player.getName().getString(), state.getName()), false);
		} else if (i > 1) {
			source.sendSuccess(Component.translatable("xbackpack.commands.backpack.set_success.multiple", i, player.getName().getString(), state.getName()), false);
		}
		return 1;
	}
	
	private static String getName(BackpackExtension extension) { 
		String[] nameParts = BackpackExtensions.REGISTRY.get().getKey(extension).getPath().split("_");
		String name = "";
		for (String namePart : nameParts) {
			String startChar = namePart.substring(0, 1).toUpperCase();
			name += startChar + namePart.substring(1, namePart.length()) + " ";
		}
		return name.trim();
	}
	
}
