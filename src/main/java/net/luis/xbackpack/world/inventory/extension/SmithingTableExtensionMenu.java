package net.luis.xbackpack.world.inventory.extension;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtension;
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
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraft.world.level.Level;

public class SmithingTableExtensionMenu extends AbstractExtensionMenu {
	
	private final CraftingHandler handler;
	private final Level level;
	private UpgradeRecipe selectedRecipe;
	
	public SmithingTableExtensionMenu(BackpackMenu menu, Player player) {
		super(menu, player, BackpackExtension.SMITHING_TABLE.get());
		this.handler = BackpackProvider.get(this.player).getSmithingHandler();
		this.level = this.player.level;
	}
	
	@Override
	public void open() {
		this.createResult();
	}

	@Override
	public void addSlots(Consumer<Slot> consumer) {
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 0, 225, 193));
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 1, 260, 193));
		consumer.accept(new ExtensionSlot(this, this.handler.getResultHandler(), 0, 304, 193, false) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
			
			@Override
			public boolean mayPickup(Player player) {
				return SmithingTableExtensionMenu.this.mayPickup(player);
			}
			
			@Override
			public void onTake(Player player, ItemStack stack) {
				SmithingTableExtensionMenu.this.onTake(player, stack);
				super.onTake(player, stack);
			}
		});
	}
	
	private boolean mayPickup(Player player) {
		return this.selectedRecipe != null && this.selectedRecipe.matches(this.asContainer(), this.level);
	}

	private void onTake(Player player, ItemStack stack) {
		stack.onCraftedBy(player.level, player, stack.getCount());
		if (this.selectedRecipe != null) {
			player.awardRecipes(Collections.singleton(this.selectedRecipe));
		}
		this.shrinkStackInSlot(0);
		this.shrinkStackInSlot(1);
		if (player instanceof ServerPlayer serverPlayer) {
			this.playSound(serverPlayer, serverPlayer.getLevel());
		}
	}

	private void shrinkStackInSlot(int slot) {
		ItemStack stack = this.handler.getInputHandler().getStackInSlot(slot);
		stack.shrink(1);
		this.handler.getInputHandler().setStackInSlot(slot, stack);
	}
	
	private void playSound(ServerPlayer player, ServerLevel level) {
		player.connection.send(new ClientboundSoundPacket(SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, player.getX(), player.getY(), player.getZ(), 1.0F, level.random.nextFloat() * 0.1F + 0.9F, level.random.nextLong()));
	}
	
	@Override
	public void slotsChanged() {
		this.createResult();
	}

	private void createResult() {
		List<UpgradeRecipe> recipes = this.level.getRecipeManager().getRecipesFor(RecipeType.SMITHING, this.asContainer(), this.level);
		if (recipes.isEmpty()) {
			this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
		} else {
			this.selectedRecipe = recipes.get(0);
			ItemStack stack = this.selectedRecipe.assemble(this.asContainer());
			this.handler.getResultHandler().setStackInSlot(0, stack);
		}

	}
	
	private SimpleContainer asContainer() {
		return new SimpleContainer(this.handler.getInputHandler().getStackInSlot(0), this.handler.getInputHandler().getStackInSlot(1));
	}

}
