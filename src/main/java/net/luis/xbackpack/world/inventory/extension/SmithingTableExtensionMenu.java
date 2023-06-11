package net.luis.xbackpack.world.inventory.extension;

import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.AbstractExtensionContainerMenu;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionSlot;
import net.luis.xbackpack.world.inventory.handler.CraftingHandler;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SmithingTableExtensionMenu extends AbstractExtensionMenu {
	
	private final CraftingHandler handler;
	private final Level level;
	private SmithingRecipe selectedRecipe;
	
	public SmithingTableExtensionMenu(AbstractExtensionContainerMenu menu, Player player) {
		super(menu, player, BackpackExtensions.SMITHING_TABLE.get());
		this.handler = BackpackProvider.get(this.player).getSmithingHandler();
		this.level = this.player.level();
	}
	
	@Override
	public void open() {
		this.createResult();
	}
	
	@Override
	public void addSlots(@NotNull Consumer<Slot> consumer) {
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 0, 225, 193));
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 1, 260, 193));
		consumer.accept(new ExtensionSlot(this, this.handler.getResultHandler(), 0, 304, 193, false) {
			@Override
			public boolean mayPlace(@NotNull ItemStack stack) {
				return false;
			}
			
			@Override
			public boolean mayPickup(Player player) {
				return SmithingTableExtensionMenu.this.mayPickup(player);
			}
			
			@Override
			public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
				SmithingTableExtensionMenu.this.onTake(player, stack);
				super.onTake(player, stack);
			}
		});
	}
	
	private boolean mayPickup(Player player) {
		return this.selectedRecipe != null && this.selectedRecipe.matches(this.asContainer(), this.level);
	}
	
	private void onTake(Player player, @NotNull ItemStack stack) {
		stack.onCraftedBy(player.level(), player, stack.getCount());
		if (this.selectedRecipe != null) {
			player.awardRecipes(Collections.singleton(this.selectedRecipe));
		}
		this.shrinkStackInSlot(0);
		this.shrinkStackInSlot(1);
		if (player instanceof ServerPlayer serverPlayer) {
			this.playSound(serverPlayer, serverPlayer.serverLevel());
		}
	}
	
	private void shrinkStackInSlot(int slot) {
		ItemStack stack = this.handler.getInputHandler().getStackInSlot(slot);
		stack.shrink(1);
		this.handler.getInputHandler().setStackInSlot(slot, stack);
	}
	
	private void playSound(@NotNull ServerPlayer player, @NotNull ServerLevel level) {
		player.connection.send(new ClientboundSoundPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SMITHING_TABLE_USE), SoundSource.BLOCKS, player.getX(), player.getY(), player.getZ(), 1.0F, level.random.nextFloat() * 0.1F + 0.9F, level.random.nextLong()));
	}
	
	@Override
	public void slotsChanged() {
		this.createResult();
	}
	
	private void createResult() {
		List<SmithingRecipe> recipes = this.level.getRecipeManager().getRecipesFor(RecipeType.SMITHING, this.asContainer(), this.level);
		if (recipes.isEmpty()) {
			this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
		} else {
			this.selectedRecipe = recipes.get(0);
			ItemStack stack = this.selectedRecipe.assemble(this.asContainer(), this.level.registryAccess());
			this.handler.getResultHandler().setStackInSlot(0, stack);
		}
		
	}
	
	private @NotNull SimpleContainer asContainer() {
		return new SimpleContainer(this.handler.getInputHandler().getStackInSlot(0), this.handler.getInputHandler().getStackInSlot(1));
	}
	
	@Override
	public boolean quickMoveStack(ItemStack slotStack, int index) {
		if (908 >= index && index >= 0) { // from container
			if (this.canQuickMoveIngredient(slotStack)) { // into addition
				return this.menu.moveItemStackTo(slotStack, 955, 956);
			} else {  // into input
				return this.menu.moveItemStackTo(slotStack, 954, 955);
			}
		} else if (index == 956) { // from result
			return this.movePreferredMenu(slotStack); // into addition
		}
		return false;
	}
	
	private boolean canQuickMoveIngredient(ItemStack stack) {
		return this.player.level().getRecipeManager().getAllRecipesFor(RecipeType.SMITHING).stream().anyMatch((recipe) -> recipe.isAdditionIngredient(stack));
	}
}
