package net.luis.xbackpack.world.inventory.extension;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.network.packet.extension.UpdateStonecutterExtension;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionSlot;
import net.luis.xbackpack.world.inventory.handler.CraftingHandler;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.StonecutterRecipe;

/**
 * 
 * @author Luis-st
 *
 */

public class StonecutterExtensionMenu extends AbstractExtensionMenu {
	
	private final CraftingHandler handler;
	private final List<StonecutterRecipe> recipes = Lists.newArrayList();
	private ItemStack input = ItemStack.EMPTY;
	private int selectedRecipe = -1;
	private StonecutterRecipe recipe;
	
	public StonecutterExtensionMenu(BackpackMenu menu, Player player) {
		super(menu, player, BackpackExtensions.STONECUTTER.get());
		this.handler = BackpackProvider.get(this.player).getStonecutterHandler();
	}
	
	@Override
	public void open() {
		this.slotsChanged();
	}
	
	@Override
	public void addSlots(Consumer<Slot> consumer) {
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 0, 249, 121));
		consumer.accept(new ExtensionSlot(this, this.handler.getResultHandler(), 0, 249, 207) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
			
			@Override
			public void onTake(Player player, ItemStack stack) {
				StonecutterExtensionMenu.this.onTake(player, stack);
				super.onTake(player, stack);
			}
		});
	}
	
	private void onTake(Player player, ItemStack stack) {
		stack.onCraftedBy(player.level, player, stack.getCount());
		if (this.recipe != null && !this.recipe.isSpecial()) {
			player.awardRecipes(Collections.singleton(this.recipe));
		}
		ItemStack inputStack = this.handler.getInputHandler().extractItem(0, 1, false);
		if (!inputStack.isEmpty() && !this.handler.getInputHandler().getStackInSlot(0).isEmpty()) {
			this.setupResult();
		}
		this.menu.broadcastChanges();
		if (player instanceof ServerPlayer serverPlayer) {
			this.playSound(serverPlayer, serverPlayer.getLevel());
		}
	}
	
	private void playSound(ServerPlayer player, ServerLevel level) {
		player.connection.send(new ClientboundSoundPacket(SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, player.getX(), player.getY(), player.getZ(), 1.0F, level.random.nextFloat() * 0.1F + 0.9F));
	}
	
	@Override
	public boolean requiresTickUpdate() {
		ItemStack input = this.handler.getInputHandler().getStackInSlot(0);
		ItemStack result = this.handler.getResultHandler().getStackInSlot(0);
		if (result.isEmpty() && !this.recipes.isEmpty()) {
			return true;
		}
		return !input.isEmpty() && this.recipes.isEmpty();
	}
	
	@Override
	public void slotsChanged() {
		ItemStack stack = this.handler.getInputHandler().getStackInSlot(0);
		if (!stack.is(this.input.getItem())) {
			this.input = stack.copy();
			this.setupRecipes(stack);
		} else if (this.player instanceof ServerPlayer player) {
			XBNetworkHandler.sendToPlayer(player, new UpdateStonecutterExtension(false));
		}
	}
	
	private void setupRecipes(ItemStack stack) {
		this.recipes.clear();
		this.selectedRecipe = -1;
		this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
		this.recipes.addAll(this.player.level.getRecipeManager().getRecipesFor(RecipeType.STONECUTTING, new SimpleContainer(stack), this.player.level));
		if (this.player instanceof ServerPlayer player) {
			XBNetworkHandler.sendToPlayer(player, new UpdateStonecutterExtension(true));
		}
	}
	
	@Override
	public boolean clickMenuButton(Player player, int button) {
		if (this.isValidIndex(button)) {
			this.selectedRecipe = button;
			this.setupResult();
			return true;
		}
		return true;
	}
	
	private void setupResult() {
		if (!this.recipes.isEmpty() && this.isValidIndex(this.selectedRecipe)) {
			StonecutterRecipe recipe = this.recipes.get(this.selectedRecipe);
			this.handler.getResultHandler().setStackInSlot(0, recipe.assemble(new SimpleContainer(this.handler.getInputHandler().getStackInSlot(0))));
			this.recipe = recipe;
		} else {
			this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
		}
		this.menu.broadcastChanges();
	}
	
	private boolean isValidIndex(int index) {
		return this.recipes.size() > index && index >= 0;
	}

}
