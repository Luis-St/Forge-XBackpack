package net.luis.xbackpack.world.capability;

import net.luis.xbackpack.world.backpack.config.BackpackConfig;
import net.luis.xbackpack.world.inventory.handler.*;
import net.luis.xbackpack.world.inventory.progress.ProgressHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.items.ItemStackHandler;

/**
 *
 * @author Luis-St
 *
 */

@AutoRegisterCapability
public interface IBackpack {
	
	Player getPlayer();
	
	BackpackConfig getConfig();
	
	ItemStackHandler getBackpackHandler();
	
	ItemStackHandler getToolHandler();
	
	ItemStackHandler getCraftingHandler();
	
	SmeltingHandler getSmeltingHandler();
	
	ProgressHandler getSmeltHandler();
	
	CraftingHandler getAnvilHandler();
	
	EnchantingHandler getEnchantingHandler();
	
	CraftingHandler getStonecutterHandler();
	
	CraftingFuelHandler getBrewingHandler();
	
	ProgressHandler getBrewHandler();
	
	CraftingHandler getGrindstoneHandler();
	
	CraftingHandler getSmithingHandler();
	
	void tick();
	
	boolean broadcastChanges();
	
	CompoundTag serialize();
	
	void deserialize(CompoundTag tag);
}
