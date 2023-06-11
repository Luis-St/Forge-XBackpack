package net.luis.xbackpack.world.inventory.extension;

import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.AbstractExtensionContainerMenu;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionResultSlot;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionSlot;
import net.luis.xbackpack.world.inventory.wrapper.CraftingContainerWrapper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 *
 * @author Luis-st
 *
 */

public class CraftingExtensionMenu extends AbstractExtensionMenu {
	
	private final CraftingContainerWrapper craftingWrapper;
	private final ResultContainer resultWrapper;
	
	public CraftingExtensionMenu(AbstractExtensionContainerMenu menu, Player player) {
		super(menu, player, BackpackExtensions.CRAFTING_TABLE.get());
		this.craftingWrapper = new CraftingContainerWrapper(this.menu, BackpackProvider.get(this.player).getCraftingHandler(), 3, 3);
		this.resultWrapper = new ResultContainer();
	}
	
	@Override
	public void open() {
		this.slotChangedCraftingGrid();
	}
	
	@Override
	public void addSlots(Consumer<Slot> consumer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				consumer.accept(new ExtensionSlot(this, this.craftingWrapper, j + i * 3, 225 + j * 18, 25 + i * 18));
			}
		}
		consumer.accept(new ExtensionResultSlot(this, this.player, this.craftingWrapper, this.resultWrapper, 0, 243, 110));
	}
	
	@Override
	public void slotsChanged(Container container) {
		if (container == this.craftingWrapper) {
			this.slotChangedCraftingGrid();
		}
	}
	
	private void slotChangedCraftingGrid() {
		Level level = this.player.level();
		if (!level.isClientSide && this.player instanceof ServerPlayer player) {
			ItemStack stack = ItemStack.EMPTY;
			Optional<CraftingRecipe> optional = Objects.requireNonNull(level.getServer()).getRecipeManager().getRecipeFor(RecipeType.CRAFTING, this.craftingWrapper, level);
			if (optional.isPresent()) {
				CraftingRecipe recipe = optional.get();
				if (this.resultWrapper.setRecipeUsed(level, player, recipe)) {
					stack = recipe.assemble(this.craftingWrapper, player.level().registryAccess());
				}
			}
			this.resultWrapper.setItem(0, stack);
			BackpackProvider.get(this.player).broadcastChanges();
		}
	}
	
	@Override
	public boolean quickMoveStack(ItemStack slotStack, int index) {
		if (908 >= index && index >= 0) { // from container
			return this.menu.moveItemStackTo(slotStack, 917, 926); // into input
		} else if (index == 926) { // from result
			return this.movePreferredMenu(slotStack); // into container
		}
		return false;
	}
}
