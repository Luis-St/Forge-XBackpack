package net.luis.xbackpack.world.inventory.extension;

import java.util.function.Consumer;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionSlot;
import net.luis.xbackpack.world.inventory.extension.slot.FurnaceExtensionResultSlot;
import net.luis.xbackpack.world.inventory.handler.SmeltingHandler;
import net.luis.xbackpack.world.inventory.handler.progress.ProgressHandler;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;

/**
 * 
 * @author Luis-st
 *
 */

public class FurnaceExtensionMenu extends AbstractExtensionMenu {
	
	private final SmeltingHandler handler;
	private final ProgressHandler progressHandler;
	
	public FurnaceExtensionMenu(BackpackMenu menu, Player player) {
		super(menu, player, BackpackExtensions.FURNACE.get());
		IBackpack backpack = BackpackProvider.get(this.player);
		this.handler = backpack.getSmeltingHandler();
		this.progressHandler = backpack.getSmeltHandler();
	}
	
	@Override
	public void open() {
		this.progressHandler.broadcastChanges();
	}
	
	@Override
	public void addSlots(Consumer<Slot> consumer) {
		for (int i = 0; i < 4; i++) {
			consumer.accept(new ExtensionSlot(this, this.handler.getInputStorageHandler(), i, 225 + i * 18, 49) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return FurnaceExtensionMenu.canSmelt(FurnaceExtensionMenu.this.player, stack);
				}
			});
		}
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 0, 225, 71) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FurnaceExtensionMenu.canSmelt(FurnaceExtensionMenu.this.player, stack);
			}
		});
		consumer.accept(new ExtensionSlot(this, this.handler.getFuelHandler(), 0, 225, 107) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FurnaceExtensionMenu.isFuel(stack) || stack.is(Items.BUCKET);
			}
			
			@Override
			public int getMaxStackSize(ItemStack stack) {
				return stack.is(Items.BUCKET) ? 1 : super.getMaxStackSize(stack);
			}
		});
		consumer.accept(new FurnaceExtensionResultSlot(this, this.player, this.handler.getResultHandler(), 0, 275, 89));
		for (int i = 0; i < 4; i++) {
			consumer.accept(new ExtensionSlot(this, this.handler.getResultStorageHandler(), i, 225 + i * 18, 129) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return false;
				}
			});
		}
	}
	
	@SuppressWarnings("unchecked")
	public static boolean canSmelt(Player player, ItemStack stack) {
		for (RecipeType<? extends AbstractCookingRecipe> recipeType : BackpackConstans.FURNACE_RECIPE_TYPES) {
			if (player.level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>) recipeType, new SimpleContainer(stack), player.level).isPresent()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isFuel(ItemStack stack) {
		for (RecipeType<? extends AbstractCookingRecipe> recipeType : BackpackConstans.FURNACE_RECIPE_TYPES) {
			if (ForgeHooks.getBurnTime(stack, recipeType) > 0) {
				return true;
			}
		}
		return false;
	}

}
