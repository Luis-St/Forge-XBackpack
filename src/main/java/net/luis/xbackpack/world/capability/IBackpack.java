package net.luis.xbackpack.world.capability;

import net.luis.xbackpack.world.inventory.handler.FurnaceCraftingHandler;
import net.luis.xbackpack.world.inventory.handler.FurnaceSmeltHandler;
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
	
	FurnaceCraftingHandler getFurnaceHandler();
	
	FurnaceSmeltHandler getSmeltHandler();
	
	ItemStackHandler getAnvilHandler();
	
	ItemStackHandler getEnchantingHandler();
	
	ItemStackHandler getStonecutterHandler();
	
	ItemStackHandler getBrewingHandler();
	
	ItemStackHandler getGrindstoneHandler();
	
	ItemStackHandler getSmithingHandler();
	
	void tick();
	
	CompoundTag serialize();
	
	void deserialize(CompoundTag tag);
	
}
