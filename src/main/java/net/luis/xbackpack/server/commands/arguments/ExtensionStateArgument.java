package net.luis.xbackpack.server.commands.arguments;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.luis.xbackpack.world.extension.ExtensionState;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

/**
 *
 * @author Luis-st
 *
 */

public class ExtensionStateArgument implements ArgumentType<ExtensionState> {
	
	private static final Dynamic2CommandExceptionType INVALID_EXTENSION_STATE = new Dynamic2CommandExceptionType((name, dummyObject) -> {
		return Component.translatable("xbackpack.commands.arguments.state.invalid", name);
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
	
	public static class Info implements ArgumentTypeInfo<ExtensionStateArgument, Info.Template> {
		
		@Override
		public void serializeToNetwork(Template template, FriendlyByteBuf buffer) {
			
		}
		
		@Override
		public Template deserializeFromNetwork(FriendlyByteBuf buffer) {
			return new Template(new ExtensionStateArgument());
		}
		
		@Override
		public void serializeToJson(Template template, JsonObject object) {
			
		}
		
		@Override
		public Template unpack(ExtensionStateArgument argument) {
			return new Template(argument);
		}
		
		public class Template implements ArgumentTypeInfo.Template<ExtensionStateArgument> {
			
			private final ExtensionStateArgument argument;
			
			public Template(ExtensionStateArgument argument) {
				this.argument = argument;
			}
			
			@Override
			public ExtensionStateArgument instantiate(CommandBuildContext context) {
				return this.argument;
			}
			
			@Override
			public ArgumentTypeInfo<ExtensionStateArgument, ?> type() {
				return Info.this;
			}
			
		}
		
	}
	
}