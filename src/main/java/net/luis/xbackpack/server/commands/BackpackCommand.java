package net.luis.xbackpack.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import net.luis.xbackpack.server.commands.arguments.BackpackExtensionArgument;
import net.luis.xbackpack.server.commands.arguments.BackpackExtensionStateArgument;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.extension.*;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Luis-st
 *
 */

public class BackpackCommand {
	
	public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
		dispatcher.register(Commands.literal("backpack").requires((source) -> {
			return source.hasPermission(2);
		}).then(Commands.literal("extension").then(Commands.argument("player", EntityArgument.player()).then(Commands.literal("*").then(Commands.argument("state", BackpackExtensionStateArgument.state()).executes((command) -> {
			return setExtensionState(command.getSource(), EntityArgument.getPlayer(command, "player"), Lists.newArrayList(BackpackExtensions.REGISTRY.get().getValues()), BackpackExtensionStateArgument.get(command, "state"));
		}))).then(Commands.argument("extension", BackpackExtensionArgument.extension()).executes((command) -> {
			return getExtensionState(command.getSource(), EntityArgument.getPlayer(command, "player"), Lists.newArrayList(BackpackExtensionArgument.get(command, "extension")));
		}).then(Commands.argument("state", BackpackExtensionStateArgument.state()).executes((command) -> {
			return setExtensionState(command.getSource(), EntityArgument.getPlayer(command, "player"), Lists.newArrayList(BackpackExtensionArgument.get(command, "extension")), BackpackExtensionStateArgument.get(command, "state"));
		})))))/* Add here other settings */);
	}
	
	private static int getExtensionState(CommandSourceStack source, ServerPlayer player, @NotNull List<BackpackExtension> extensions) {
		if (extensions.size() > 1) {
			source.sendFailure(Component.translatable("xbackpack.commands.backpack.get.failure"));
		} else {
			source.sendSuccess(() -> Component.translatable("xbackpack.commands.backpack.get.success", getName(extensions.get(0)), player.getName().getString(), BackpackProvider.get(player).getConfig().getExtensionConfig().getState(extensions.get(0)).getName()), false);
		}
		return 1;
	}
	
	private static int setExtensionState(CommandSourceStack source, ServerPlayer player, @NotNull List<BackpackExtension> extensions, BackpackExtensionState state) {
		MutableInt i = new MutableInt(0);
		IBackpack backpack = BackpackProvider.get(player);
		for (BackpackExtension extension : extensions) {
			if (extension == BackpackExtensions.NO.get()) {
				continue;
			}
			if (extension.isDisabled()) {
				source.sendFailure(Component.translatable("xbackpack.commands.backpack.set.failure.disabled", getName(extension)));
				continue;
			}
			backpack.getConfig().getExtensionConfig().setState(player, extension, state);
			i.increment();
		}
		backpack.broadcastChanges();
		if (i.getValue() == 1) {
			source.sendSuccess(() -> Component.translatable("xbackpack.commands.backpack.set.success.single", getName(extensions.get(0)), player.getName().getString(), state.getName()), false);
		} else if (i.getValue() > 1) {
			source.sendSuccess(() -> Component.translatable("xbackpack.commands.backpack.set.success.multiple", i.getValue(), player.getName().getString(), state.getName()), false);
		}
		return 1;
	}
	
	private static @NotNull String getName(BackpackExtension extension) {
		String[] nameParts = Objects.requireNonNull(BackpackExtensions.REGISTRY.get().getKey(extension)).getPath().split("_");
		StringBuilder name = new StringBuilder();
		for (String namePart : nameParts) {
			String startChar = namePart.substring(0, 1).toUpperCase();
			name.append(startChar).append(namePart.substring(1)).append(" ");
		}
		return name.toString().trim();
	}
}
