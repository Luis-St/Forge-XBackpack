package net.luis.xbackpack.client.commands;

import com.mojang.brigadier.CommandDispatcher;

import net.luis.xbackpack.world.capability.BackpackProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

/**
 *
 * @author Luis-st
 *
 */

public class TooltipCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("tooltip").executes((command) -> {
			return getTooltipVisibility(command.getSource());
		}).then(Commands.literal("enable").executes((command) -> {
			return setTooltipVisibility(command.getSource(), true);
		})).then(Commands.literal("disable").executes((command) -> {
			return setTooltipVisibility(command.getSource(), false);
		})));
	}
	
	private static int getTooltipVisibility(CommandSourceStack source) {
		if (source.getEntity() instanceof Player player) {
			boolean showModifierInfo = BackpackProvider.get(player).getConfig().getClientConfig().shouldShowModifierInfo();
			if (showModifierInfo) {
				source.sendSuccess(Component.translatable("xbackpack.commands.tooltip.get.success.true"), false);
			} else {
				source.sendSuccess(Component.translatable("xbackpack.commands.tooltip.get.success.false"), false);
			}
		} else {
			source.sendFailure(Component.translatable("xbackpack.commands.tooltip.failure"));
		}
		return 1;
	}
	
	private static int setTooltipVisibility(CommandSourceStack source, boolean value) {
		if (source.getEntity() instanceof Player player) {
			BackpackProvider.get(player).getConfig().getClientConfig().setShowModifierInfo(value);
			if (value) {
				source.sendSuccess(Component.translatable("xbackpack.commands.tooltip.set.success.true"), false);
			} else {
				source.sendSuccess(Component.translatable("xbackpack.commands.tooltip.set.success.false"), false);
			}
		} else {
			source.sendFailure(Component.translatable("xxbackpack.commands.tooltip.failure"));
		}
		return 1;
	}
	
}
