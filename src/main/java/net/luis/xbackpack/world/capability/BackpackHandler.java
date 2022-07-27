package net.luis.xbackpack.world.capability;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.inventory.handler.BrewingStandBrewHandler;
import net.luis.xbackpack.world.inventory.handler.BrewingStandCraftingHandler;
import net.luis.xbackpack.world.inventory.handler.CraftingHandler;
import net.luis.xbackpack.world.inventory.handler.FurnaceCraftingHandler;
import net.luis.xbackpack.world.inventory.handler.FurnaceSmeltHandler;
import net.luis.xbackpack.world.inventory.handler.ProgressHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackHandler implements IBackpack {
	
	private final Player player;
	private final ItemStackHandler backpackHandler = new ItemStackHandler(873);
	private final ItemStackHandler toolHandler = new ItemStackHandler(3);
	private final ItemStackHandler craftingHandler = new ItemStackHandler(9);
	private final FurnaceCraftingHandler furnaceHandler = new FurnaceCraftingHandler(1, 4, 4);
	private final FurnaceSmeltHandler smeltHandler;
	private final CraftingHandler anvilHandler = new CraftingHandler(2, 1);
	private final ItemStackHandler enchantingHandler = new ItemStackHandler(3);
	private final ItemStackHandler stonecutterHandler = new ItemStackHandler(2);
	private final BrewingStandCraftingHandler brewingHandler = new BrewingStandCraftingHandler(1, 3);
	private final BrewingStandBrewHandler brewHandler;
	private final ItemStackHandler grindstoneHandler = new ItemStackHandler(3);
	private final ItemStackHandler smithingHandler = new ItemStackHandler(3);
	
	public BackpackHandler(Player player) {
		this.player = player;
		this.smeltHandler = new FurnaceSmeltHandler(this.player, this.furnaceHandler, BackpackConstans.FURNACE_RECIPE_TYPES);
		this.brewHandler = new BrewingStandBrewHandler(this.player, this.brewingHandler);
	}
	
	@Override
	public Player getPlayer() {
		return this.player;
	}
	
	@Override
	public ItemStackHandler getBackpackHandler() {
		return this.backpackHandler;
	}

	@Override
	public ItemStackHandler getToolHandler() {
		return this.toolHandler;
	}

	@Override
	public ItemStackHandler getCraftingHandler() {
		return this.craftingHandler;
	}

	@Override
	public FurnaceCraftingHandler getFurnaceHandler() {
		return this.furnaceHandler;
	}
	
	@Override
	public ProgressHandler getSmeltHandler() {
		return this.smeltHandler;
	}

	@Override
	public CraftingHandler getAnvilHandler() {
		return this.anvilHandler;
	}

	@Override
	public ItemStackHandler getEnchantingHandler() {
		return this.enchantingHandler;
	}

	@Override
	public ItemStackHandler getStonecutterHandler() {
		return this.stonecutterHandler;
	}

	@Override
	public BrewingStandCraftingHandler getBrewingHandler() {
		return this.brewingHandler;
	}
	
	@Override
	public ProgressHandler getBrewHandler() {
		return this.brewHandler;
	}
	
	@Override
	public ItemStackHandler getGrindstoneHandler() {
		return this.grindstoneHandler;
	}

	@Override
	public ItemStackHandler getSmithingHandler() {
		return this.smithingHandler;
	}
	
	@Override
	public void tick() {
		this.smeltHandler.tick();
		this.brewHandler.tick();
	}
	
	@Override
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.put("backpack_handler", this.backpackHandler.serializeNBT());
		tag.put("tool_handler", this.toolHandler.serializeNBT());
		tag.put("crafting_handler", this.craftingHandler.serializeNBT());
		tag.put("furnace_handler", this.furnaceHandler.serialize());
		tag.put("smelt_handler", this.smeltHandler.serialize());
		tag.put("anvil_handler", this.anvilHandler.serialize());
		tag.put("enchanting_handler", this.enchantingHandler.serializeNBT());
		tag.put("stonecutter_handler", this.stonecutterHandler.serializeNBT());
		tag.put("brewing_handler", this.brewingHandler.serialize());
		tag.put("brew_handler", this.brewHandler.serialize());
		tag.put("grindstone_handler", this.grindstoneHandler.serializeNBT());
		tag.put("smithing_handler", this.smithingHandler.serializeNBT());
		return tag;
	}
	
	@Override
	public void deserialize(CompoundTag tag) {
		if (tag.contains("Size") && tag.contains("Items")) {
			this.deserializeOld(tag);
		} else {
			this.backpackHandler.deserializeNBT(tag.getCompound("backpack_handler"));
			this.toolHandler.deserializeNBT(tag.getCompound("tool_handler"));
			this.craftingHandler.deserializeNBT(tag.getCompound("crafting_handler"));
			this.furnaceHandler.deserialize(tag.getCompound("furnace_handler"));
			this.smeltHandler.deserialize(tag.getCompound("smelt_handler"));
			this.anvilHandler.deserialize(tag.getCompound("anvil_handler"));
			this.enchantingHandler.deserializeNBT(tag.getCompound("enchanting_handler"));
			this.stonecutterHandler.deserializeNBT(tag.getCompound("stonecutter_handler"));
			this.brewingHandler.deserialize(tag.getCompound("brewing_handler"));
			this.brewHandler.deserialize(tag.getCompound("brew_handler"));
			this.grindstoneHandler.deserializeNBT(tag.getCompound("grindstone_handler"));
			this.smithingHandler.deserializeNBT(tag.getCompound("smithing_handler"));
		}
	}
	
	private void deserializeOld(CompoundTag tag) {
		XBackpack.LOGGER.info("Convert the old disk format into new format");
		ListTag items = tag.getList("Items", Tag.TAG_COMPOUND);
		for (int i = 0; i < items.size(); i++) {
			CompoundTag item = items.getCompound(i);
			int slot = item.getInt("Slot");
			ItemStack stack = ItemStack.of(item);
			if (slot >= 0 && slot < this.backpackHandler.getSlots()) {
				this.backpackHandler.setStackInSlot(slot, stack);
			} else {
				int toolSlot = slot - 873;
				if (2 >= toolSlot && toolSlot >= 0) {
					this.toolHandler.setStackInSlot(toolSlot, stack);
				} else if (!stack.isEmpty()) {
					XBackpack.LOGGER.error("Fail to convert slot {} with item {} into the new disk format, the item will be destroyed", slot, stack.getItem());
				}
			}
		}
	}

}
