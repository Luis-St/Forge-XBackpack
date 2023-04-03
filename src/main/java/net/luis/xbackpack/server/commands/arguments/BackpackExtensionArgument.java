package net.luis.xbackpack.server.commands.arguments;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 *
 * @author Luis-st
 *
 */

public class BackpackExtensionArgument implements ArgumentType<BackpackExtension> {
	
	private static final Dynamic2CommandExceptionType INVALID_BACKPACK_EXTENSION = new Dynamic2CommandExceptionType((name, dummyObject) -> {
		return Component.translatable("xbackpack.commands.arguments.extension.invalid", name);
	});
	
	private final Supplier<IForgeRegistry<BackpackExtension>> registrySupplier;
	
	private BackpackExtensionArgument() {
		this.registrySupplier = BackpackExtensions.REGISTRY;
	}
	
	public static @NotNull BackpackExtensionArgument extension() {
		return new BackpackExtensionArgument();
	}
	
	public static <S> BackpackExtension get(@NotNull CommandContext<S> context, String name) {
		return context.getArgument(name, BackpackExtension.class);
	}
	
	@Override
	public BackpackExtension parse(StringReader reader) throws CommandSyntaxException {
		ResourceLocation location = ResourceLocation.read(reader);
		if (this.registrySupplier.get().containsKey(location)) {
			return this.registrySupplier.get().getValue(location);
		}
		throw INVALID_BACKPACK_EXTENSION.create(location, "");
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggest(this.registrySupplier.get().getKeys().stream().map(ResourceLocation::toString), builder);
	}
	
	@Override
	public Collection<String> getExamples() {
		return this.registrySupplier.get().getKeys().stream().map(ResourceLocation::toString).collect(Collectors.toList());
	}
	
	public static class Info implements ArgumentTypeInfo<BackpackExtensionArgument, Info.Template> {
		
		@Override
		public void serializeToNetwork(@NotNull Template template, @NotNull FriendlyByteBuf buffer) {
			
		}
		
		@Override
		public @NotNull Template deserializeFromNetwork(@NotNull FriendlyByteBuf buffer) {
			return new Template(new BackpackExtensionArgument());
		}
		
		@Override
		public void serializeToJson(@NotNull Template template, @NotNull JsonObject object) {
			
		}
		
		@Override
		public @NotNull Template unpack(@NotNull BackpackExtensionArgument argument) {
			return new Template(argument);
		}
		
		public class Template implements ArgumentTypeInfo.Template<BackpackExtensionArgument> {
			
			private final BackpackExtensionArgument argument;
			
			public Template(BackpackExtensionArgument argument) {
				this.argument = argument;
			}
			
			@Override
			public @NotNull BackpackExtensionArgument instantiate(@NotNull CommandBuildContext context) {
				return this.argument;
			}
			
			@Override
			public @NotNull ArgumentTypeInfo<BackpackExtensionArgument, ?> type() {
				return Info.this;
			}
			
		}
		
	}
	
}
