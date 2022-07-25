package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.world.inventory.extension.FurnaceExtensionMenu;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.IItemHandler;

public class FurnaceExtensionInputSlot extends ExtensionSlot {
	
	private final Player player;
	
	public FurnaceExtensionInputSlot(FurnaceExtensionMenu extensionMenu, Player player, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(extensionMenu, itemHandler, index, xPosition, yPosition);
		this.player = player;
	}
	
	@Override
	public FurnaceExtensionMenu getMenu() {
		return (FurnaceExtensionMenu) super.getMenu();
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return this.canSmelt(stack);
	}
	
	@SuppressWarnings("unchecked")
	private boolean canSmelt(ItemStack stack) {
		for (RecipeType<? extends AbstractCookingRecipe> recipeType : BackpackConstans.FURNACE_RECIPE_TYPES) {
			if (this.player.level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>) recipeType, new SimpleContainer(stack), this.player.level).isPresent()) {
				return true;
			}
		}
		return false;
	}

}
