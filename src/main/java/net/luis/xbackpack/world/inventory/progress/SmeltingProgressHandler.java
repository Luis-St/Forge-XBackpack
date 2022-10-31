package net.luis.xbackpack.world.inventory.progress;

import java.util.List;
import java.util.Optional;

import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.network.packet.extension.UpdateFurnaceExtension;
import net.luis.xbackpack.world.inventory.handler.SmeltingHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class SmeltingProgressHandler implements ProgressHandler {
	
	private final Player player;
	private final SmeltingHandler handler;
	private final List<RecipeType<? extends AbstractCookingRecipe>> recipeTypes;
	private int cookingProgress;
	private int cookingTime;
	private AbstractCookingRecipe progressingRecipe;
	private int fuelTime;
	private int maxFuel;
	
	public SmeltingProgressHandler(Player player, SmeltingHandler handler, List<RecipeType<? extends AbstractCookingRecipe>> recipeTypes) {
		this.player = player;
		this.handler = handler;
		this.recipeTypes = recipeTypes;
	}
	
	@Override
	public void tick() {
		int oldcookingProgress = this.getCookingProgress();
		int oldFuelTime = this.getFuelProgress();
		this.checkRecipe();
		this.forceStorages();
		if (this.fuelTime > 0) {
			--this.fuelTime;
		}
		this.checkFuel();
		if (this.progressingRecipe == null && !this.getInputItem().isEmpty()) {
			ItemStack stack = this.getInputItem();
			if (this.canSmelt(stack)) {
				this.progressingRecipe = this.getRecipe(stack);
				if (this.progressingRecipe != null) {
					this.cookingTime = this.progressingRecipe.getCookingTime();
					this.cookingProgress = 0;
				}
			}
		}
		AbstractCookingRecipe progressingRecipe = this.progressingRecipe;
		if (progressingRecipe != null) {
			if (this.getInputItem().isEmpty()) {
				this.resetRecipe();
				this.broadcastChanges();
			} else if (this.fuelTime > 0) {
				this.cookingProgress++;
			} else if (this.cookingProgress > 0) {
				this.cookingProgress--;
			}
			if (this.cookingProgress >= this.cookingTime && !this.handler.getInputHandler().extractItem(0, 1, false).isEmpty()) {
				ItemStack stack = this.handler.getResultHandler().insertItem(0, progressingRecipe.assemble(new SimpleContainer()), false);
				if (!stack.isEmpty()) {
					this.player.drop(stack, true, true);
				}
				this.giveExperience();
				this.resetRecipe();
				this.broadcastChanges();
			}
		}
		if (oldcookingProgress != this.getCookingProgress() || oldFuelTime != this.getFuelProgress()) {
			this.broadcastChanges();
		}
	}
	
	private void checkRecipe() {
		if (this.cookingProgress > 0 && this.cookingTime > 0 && this.progressingRecipe == null) {
			this.progressingRecipe = this.getRecipe(this.getInputItem());
			this.broadcastChanges();
		} else if (this.progressingRecipe == null) {
			this.resetRecipe();
		}
	}
	
	private void forceStorages() {
		this.forceInputStorage();
		this.forceResultStorage();
	}
	
	private void forceInputStorage() {
		ItemStackHandler handler = this.getInputStorage();
		for (int i = 0; i < handler.getSlots(); i++) {
			ItemStack stack = handler.getStackInSlot(i);
			if (stack.isEmpty()) {
				continue;
			} else if (this.getInputItem().isEmpty()) {
				this.handler.getInputHandler().setStackInSlot(0, stack);
				handler.setStackInSlot(i, ItemStack.EMPTY);
			} else if (this.areMergable(this.getInputItem(), stack)) {
				ItemStack input = this.getInputItem();
				int count = input.getCount() + stack.getCount();
				input.setCount(Math.min(count, input.getMaxStackSize()));
				this.handler.getInputHandler().setStackInSlot(0, input);
				handler.setStackInSlot(i, ItemHandlerHelper.copyStackWithSize(input, Math.max(count - input.getMaxStackSize(), 0)));
			}
			ItemStack input = this.getInputItem();
			if (input.getCount() >= input.getMaxStackSize()) {
				break;
			}
		}
	}
	
	private void forceResultStorage() {
		ItemStackHandler handler = this.getResultStorage();
		for (int i = 0; i < handler.getSlots(); i++) {
			ItemStack result = this.getResultItem();
			if (result.isEmpty()) {
				break;
			} else {
				ItemStack stack = handler.getStackInSlot(i);
				if (stack.isEmpty()) {
					handler.setStackInSlot(i, result);
					this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
				} else if (this.areMergable(stack, result)) {
					int count = stack.getCount() + result.getCount();
					int remaining = Math.min(count, stack.getMaxStackSize());
					result.setCount(Math.max(count - stack.getMaxStackSize(), 0));
					this.handler.getResultHandler().setStackInSlot(0, result);
					handler.setStackInSlot(i, ItemHandlerHelper.copyStackWithSize(stack, remaining));
				}
			}
		}
	}
	
	private boolean areMergable(ItemStack toStack, ItemStack stack) {
		if (toStack.getCount() >= toStack.getMaxStackSize()) {
			return false;
		} else if (!toStack.is(stack.getItem())) {
			return false;
		} else if (toStack.hasTag() ^ stack.hasTag()) {
			return false;
		} else if (!toStack.areCapsCompatible(stack)) {
			return false;
		} else {
			return !toStack.hasTag() || toStack.getTag().equals(stack.getTag());
		}
	}
	
	private void checkFuel() {
		if (0 >= this.fuelTime && !this.getInputItem().isEmpty()) {
			int fuelTime = this.getFuelTime(this.getFuelItem());
			if (fuelTime > 0) {
				this.consumeFuel(fuelTime);
			}
		}
	}
	
	private void consumeFuel(int fuelTime) {
		ItemStack stack = this.handler.getFuelHandler().extractItem(0, 1, false);
		if (!stack.isEmpty()) {
			if (this.getFuelItem().isEmpty() && stack.hasCraftingRemainingItem()) {
				this.handler.getFuelHandler().insertItem(0, stack.getCraftingRemainingItem(), false);
			}
			this.maxFuel = fuelTime;
			this.fuelTime = fuelTime;
		}
	}
	
	private void giveExperience() {
		RandomSource rng = this.player.getRandom();
		float experience;
		if (this.progressingRecipe != null) {
			experience = this.progressingRecipe.getExperience();
		} else {
			experience = rng.nextFloat() + rng.nextFloat();
		}
		int usedRecipes = rng.nextInt(3);
		this.player.giveExperiencePoints((int) (experience * usedRecipes));
	}
	
	private void resetRecipe() {
		this.cookingProgress = 0;
		this.cookingTime = 0;
		this.progressingRecipe = null;
	}
	
	@SuppressWarnings("unchecked")
	private AbstractCookingRecipe getRecipe(ItemStack stack) {
		AbstractCookingRecipe cookingRecipe = null;
		for (RecipeType<? extends AbstractCookingRecipe> recipeType : this.recipeTypes) {
			Optional<AbstractCookingRecipe> optional = this.player.level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>) recipeType, new SimpleContainer(stack), this.player.level);
			if (optional.isPresent()) {
				AbstractCookingRecipe recipe = optional.get();
				if (cookingRecipe == null) {
					cookingRecipe = recipe;
				} else if (cookingRecipe.getCookingTime() > recipe.getCookingTime()) {
					cookingRecipe = recipe;
				}
			}
		}
		return cookingRecipe;
	}
	
	private boolean canSmelt(ItemStack stack) {
		return 0 >= this.cookingProgress && 0 >= this.cookingTime && this.fuelTime > 0 && this.canProgress(stack);
	}
	
	private boolean canProgress(ItemStack stack) {
		AbstractCookingRecipe recipe = this.getRecipe(stack);
		if (recipe != null) {
			return ItemEntity.areMergable(recipe.getResultItem(), this.getResultItem()) || this.getResultItem().isEmpty();
		}
		return false;
	}
	
	private int getFuelTime(ItemStack stack) {
		int fuelTime = 0;
		for (RecipeType<? extends AbstractCookingRecipe> recipeType : this.recipeTypes) {
			fuelTime = Math.max(ForgeHooks.getBurnTime(stack, recipeType), fuelTime);
		}
		return fuelTime;
	}
	
	public ItemStack getInputItem() {
		return this.handler.getInputHandler().getStackInSlot(0);
	}
	
	public ItemStack getFuelItem() {
		return this.handler.getFuelHandler().getStackInSlot(0);
	}
	
	public ItemStack getResultItem() {
		return this.handler.getResultHandler().getStackInSlot(0);
	}
	
	public ItemStackHandler getInputStorage() {
		return this.handler.getInputStorageHandler();
	}
	
	public ItemStackHandler getResultStorage() {
		return this.handler.getResultStorageHandler();
	}
	
	public int getCookingProgress() {
		int cookingProgress = this.cookingProgress;
		int cookingTime = this.cookingTime;
		return cookingTime != 0 && cookingProgress != 0 ? cookingProgress * 24 / cookingTime : 0;
	}

	public int getFuelProgress() {
		int maxFuel = this.maxFuel == 0 ? 200 : this.maxFuel;
		return this.fuelTime * 13 / maxFuel;
	}
	
	@Override
	public void broadcastChanges() {
		if (this.player instanceof ServerPlayer player) {
			XBNetworkHandler.sendToPlayer(player, new UpdateFurnaceExtension(this.getCookingProgress(), this.getFuelProgress()));
		}
	}
	
	@Override
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("cooking_progress", this.cookingProgress);
		tag.putInt("cooking_time", this.cookingTime);
		tag.putInt("fuel_time", this.fuelTime);
		tag.putInt("max_fuel", this.maxFuel);
		return tag;
	}
	
	@Override
	public void deserialize(CompoundTag tag) {
		this.cookingProgress = tag.getInt("cooking_progress");
		this.cookingTime = tag.getInt("cooking_time");
		this.fuelTime = tag.getInt("fuel_time");
		this.maxFuel = tag.getInt("max_fuel");
	}
	
}
