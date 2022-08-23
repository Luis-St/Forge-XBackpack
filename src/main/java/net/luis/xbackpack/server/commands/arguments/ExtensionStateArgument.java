package net.luis.xbackpack.server.commands.arguments;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.luis.xbackpack.world.extension.ExtensionState;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;

/**
 *
 * @author Luis-st
 *
 */

public class ExtensionStateArgument implements ArgumentType<ExtensionState> {
	
	private static final Dynamic2CommandExceptionType INVALID_EXTENSION_STATE = new Dynamic2CommandExceptionType((name, dummyObject) -> {
		return new TranslatableComponent("xbackpack.commands.arguments.state.invalid", name);
	});
	
	private ExtensionStateArgument() {
		
	}
	
	public static ExtensionStateArgument state() {
		return new ExtensionStateArgument();
	}
	
	public static <S> ExtensionState get(CommandContext<S> context, String name) {
		return context.getArgument(name, ExtensionState.class);
	}
	
	@Override
	public ExtensionState parse(StringReader reader) throws CommandSyntaxException {
		ExtensionState state = ExtensionState.fromString(reader.readUnquotedString());
		if (state != null) {
			return state;
		}
		throw INVALID_EXTENSION_STATE.create(state, "");
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggest(Lists.newArrayList(ExtensionState.values()).stream().map(ExtensionState::getName), builder);
	}
	
	@Override
	public Collection<String> getExamples() {
		return Lists.newArrayList(ExtensionState.values()).stream().map(ExtensionState::getName).collect(Collectors.toList());
	}
	
}