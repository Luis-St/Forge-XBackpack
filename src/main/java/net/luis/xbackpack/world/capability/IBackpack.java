package net.luis.xbackpack.world.capability;

import net.luis.xbackpack.world.inventory.handler.BrewingCraftingHandler;
import net.luis.xbackpack.world.inventory.handler.CraftingHandler;
import net.luis.xbackpack.world.inventory.handler.EnchantingHandler;
import net.luis.xbackpack.world.inventory.handler.SmeltingHandler;
import net.luis.xbackpack.world.inventory.handler.progress.ProgressHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Luis-st
 * 
 */

public interface IBackpack {
	
	Player getPlayer();
	
	ItemStackHandler getBackpackHandler();
	
	ItemStackHandler getToolHandler();
	
	ItemStackHandler getCraftingHandler();
	
	SmeltingHandler getSmeltingHandler();
	
	ProgressHandler getSmeltHandler();
	
	CraftingHandler getAnvilHandler();
	
	EnchantingHandler getEnchantingHandler();
	
	CraftingHandler getStonecutterHandler();
	
	BrewingCraftingHandler getBrewingHandler();
	
	ProgressHandler getBrewHandler();
	
	CraftingHandler getGrindstoneHandler();
	
	CraftingHandler getSmithingHandler();
	
	void tick();
	
	CompoundTag serialize();
	
	void deserialize(CompoundTag tag);
	
}
