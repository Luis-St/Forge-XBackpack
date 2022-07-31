package net.luis.xbackpack.world.capability;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.inventory.handler.BrewingHandler;
import net.luis.xbackpack.world.inventory.handler.CraftingHandler;
import net.luis.xbackpack.world.inventory.handler.EnchantingHandler;
import net.luis.xbackpack.world.inventory.handler.SmeltingHandler;
import net.luis.xbackpack.world.inventory.handler.progress.BrewingProgressHandler;
import net.luis.xbackpack.world.inventory.handler.progress.ProgressHandler;
import net.luis.xbackpack.world.inventory.handler.progress.SmeltingProgressHandler;
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
	private final SmeltingHandler furnaceHandler = new SmeltingHandler(1, 4, 4);
	private final SmeltingProgressHandler smeltHandler;
	private final CraftingHandler anvilHandler = new CraftingHandler(2, 1);
	private final EnchantingHandler enchantingHandler = new EnchantingHandler(1, 1, 1);
	private final CraftingHandler stonecutterHandler = new CraftingHandler(1, 1);
	private final BrewingHandler brewingHandler = new BrewingHandler(1, 3);
	private final BrewingProgressHandler brewHandler;
	private final CraftingHandler grindstoneHandler = new CraftingHandler(2, 1);
	private final CraftingHandler smithingHandler = new CraftingHandler(2, 1);
	
	public BackpackHandler(Player player) {
		this.player = player;
		this.smeltHandler = new SmeltingProgressHandler(this.player, this.furnaceHandler, BackpackConstans.FURNACE_RECIPE_TYPES);
		this.brewHandler = new BrewingProgressHandler(this.player, this.brewingHandler);
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
	public SmeltingHandler getSmeltingHandler() {
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
	public EnchantingHandler getEnchantingHandler() {
		return this.enchantingHandler;
	}

	@Override
	public CraftingHandler getStonecutterHandler() {
		return this.stonecutterHandler;
	}

	@Override
	public BrewingHandler getBrewingHandler() {
		return this.brewingHandler;
	}
	
	@Override
	public ProgressHandler getBrewHandler() {
		return this.brewHandler;
	}
	
	@Override
	public CraftingHandler getGrindstoneHandler() {
		return this.grindstoneHandler;
	}

	@Override
	public CraftingHandler getSmithingHandler() {
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
		tag.put("enchanting_handler", this.enchantingHandler.serialize());
		tag.put("stonecutter_handler", this.stonecutterHandler.serialize());
		tag.put("brewing_handler", this.brewingHandler.serialize());
		tag.put("brew_handler", this.brewHandler.serialize());
		tag.put("grindstone_handler", this.grindstoneHandler.serialize());
		tag.put("smithing_handler", this.smithingHandler.serialize());
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
			this.enchantingHandler.deserialize(tag.getCompound("enchanting_handler"));
			this.stonecutterHandler.deserialize(tag.getCompound("stonecutter_handler"));
			this.brewingHandler.deserialize(tag.getCompound("brewing_handler"));
			this.brewHandler.deserialize(tag.getCompound("brew_handler"));
			this.grindstoneHandler.deserialize(tag.getCompound("grindstone_handler"));
			this.smithingHandler.deserialize(tag.getCompound("smithing_handler"));
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
	
	@Override
	public CompoundTag serializeNetwork() {
		CompoundTag tag = new CompoundTag();
		tag.put("tool_handler", this.toolHandler.serializeNBT());
		return tag;
	}

	@Override
	public void deserializeNetwork(CompoundTag tag) {
		this.toolHandler.deserializeNBT(tag.getCompound("tool_handler"));
	}

}
