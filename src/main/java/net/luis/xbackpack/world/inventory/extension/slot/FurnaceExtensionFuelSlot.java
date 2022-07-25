package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.world.inventory.extension.FurnaceExtensionMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;

public class FurnaceExtensionFuelSlot extends ExtensionSlot {
	
	public FurnaceExtensionFuelSlot(FurnaceExtensionMenu extensionMenu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(extensionMenu, itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public FurnaceExtensionMenu getMenu() {
		return (FurnaceExtensionMenu) super.getMenu();
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return this.isFuel(stack) || stack.is(Items.BUCKET);
	}
	
	private boolean isFuel(ItemStack stack) {
		for (RecipeType<? extends AbstractCookingRecipe> recipeType : BackpackConstans.FURNACE_RECIPE_TYPES) {
			if (ForgeHooks.getBurnTime(stack, recipeType) > 0) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getMaxStackSize(ItemStack stack) {
		return stack.is(Items.BUCKET) ? 1 : super.getMaxStackSize(stack);
	}

}
