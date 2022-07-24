package net.luis.xbackpack.world.capability;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.inventory.handler.FurnaceCraftingHandler;
import net.luis.xbackpack.world.inventory.handler.FurnaceSmeltHandler;
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
	private final ItemStackHandler anvilHandler = new ItemStackHandler(3);
	private final ItemStackHandler enchantingHandler = new ItemStackHandler(3);
	private final ItemStackHandler stonecutterHandler = new ItemStackHandler(2);
	private final ItemStackHandler brewingHandler = new ItemStackHandler(5);
	private final ItemStackHandler grindstoneHandler = new ItemStackHandler(3);
	private final ItemStackHandler smithingHandler = new ItemStackHandler(3);
	
	public BackpackHandler(Player player) {
		this.player = player;
		this.smeltHandler = new FurnaceSmeltHandler(this.player, this.furnaceHandler, BackpackConstans.FURNACE_RECIPE_TYPES);
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
	public FurnaceSmeltHandler getSmeltHandler() {
		return this.smeltHandler;
	}

	@Override
	public ItemStackHandler getAnvilHandler() {
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
	public ItemStackHandler getBrewingHandler() {
		return this.brewingHandler;
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
	}
	
	@Override
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.put("backpack_handler", this.backpackHandler.serializeNBT());
		tag.put("tool_handler", this.toolHandler.serializeNBT());
		tag.put("crafting_handler", this.craftingHandler.serializeNBT());
		tag.put("furnace_handler", this.furnaceHandler.serialize());
		tag.put("smelt_handler", this.smeltHandler.serialize());
		tag.put("anvil_handler", this.anvilHandler.serializeNBT());
		tag.put("enchanting_handler", this.enchantingHandler.serializeNBT());
		tag.put("stonecutter_handler", this.stonecutterHandler.serializeNBT());
		tag.put("brewing_handler", this.brewingHandler.serializeNBT());
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
			this.anvilHandler.deserializeNBT(tag.getCompound("anvil_handler"));
			this.enchantingHandler.deserializeNBT(tag.getCompound("enchanting_handler"));
			this.stonecutterHandler.deserializeNBT(tag.getCompound("stonecutter_handler"));
			this.brewingHandler.deserializeNBT(tag.getCompound("brewing_handler"));
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
