package net.luis.xbackpack.world.inventory.extension;

import java.util.Optional;
import java.util.function.Consumer;

import net.luis.xbackpack.world.capability.XBackpackCapabilities;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionResultSlot;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionSlot;
import net.luis.xbackpack.world.inventory.wrapper.CraftingContainerWrapper;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/**
 * 
 * @author Luis-st
 *
 */

public class CraftingExtensionMenu extends AbstractExtensionMenu {
	
	private final CraftingContainerWrapper craftingWrapper;
	private final ResultContainer resultWrapper;
	
	public CraftingExtensionMenu(BackpackMenu menu, Player player) {
		super(menu, player, BackpackExtension.CRAFTING_TABLE.get());	
		this.craftingWrapper = new CraftingContainerWrapper(this.menu, this.player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new).getCraftingHandler(), 3, 3);
		this.resultWrapper = new ResultContainer();
	}

	@Override
	public void addSlots(Consumer<Slot> consumer) {
		consumer.accept(new ExtensionResultSlot(this.player, this.craftingWrapper, this.resultWrapper, 0, 243, 110, BackpackExtension.CRAFTING_TABLE.get()));
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				consumer.accept(new ExtensionSlot(this.craftingWrapper, j + i * 3, 225 + j * 18, 25 + i * 18, BackpackExtension.CRAFTING_TABLE.get()));
			}
		}
	}
	
	@Override
	public void slotsChanged() {
		this.slotChangedCraftingGrid(this.menu, this.player.level, this.player, this.craftingWrapper, this.resultWrapper);
	}
	
	private void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, CraftingContainer craftingContainer, ResultContainer resultContainer) {
		if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
			ItemStack itemstack = ItemStack.EMPTY;
			Optional<CraftingRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingContainer, level);
			if (optional.isPresent()) {
				CraftingRecipe recipe = optional.get();
				if (resultContainer.setRecipeUsed(level, serverPlayer, recipe)) {
					itemstack = recipe.assemble(craftingContainer);
				}
			}
			resultContainer.setItem(0, itemstack);
			menu.setRemoteSlot(0, itemstack);
			serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemstack));
		}
	}

}
