package net.luis.xbackpack.world.backpack;

import net.luis.xbackpack.BackpackConstants;
import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.network.packet.UpdateBackpackPacket;
import net.luis.xbackpack.world.backpack.config.BackpackConfig;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.inventory.handler.*;
import net.luis.xbackpack.world.inventory.progress.*;
import net.luis.xbackpack.world.item.DynamicItemStackHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.ItemStackHandler;

/**
 *
 * @author Luis-St
 *
 */

public class BackpackHandler implements IBackpack {
	
	public static final int DATA_VERSION = 1;
	
	private final Player player;
	private final BackpackConfig config;
	private final ItemStackHandler backpackHandler = new DynamicItemStackHandler(873);
	private final ItemStackHandler toolHandler = new DynamicItemStackHandler(3);
	private final ItemStackHandler craftingHandler = new DynamicItemStackHandler(9);
	private final SmeltingHandler furnaceHandler = new SmeltingHandler(1, 4, 4);
	private final SmeltingProgressHandler smeltHandler;
	private final CraftingHandler anvilHandler = new CraftingHandler(2, 1);
	private final EnchantingHandler enchantingHandler = new EnchantingHandler(1, 1, 1);
	private final CraftingHandler stonecutterHandler = new CraftingHandler(1, 1);
	private final CraftingFuelHandler brewingHandler = new CraftingFuelHandler(1, 3);
	private final BrewingProgressHandler brewHandler;
	private final CraftingHandler grindstoneHandler = new CraftingHandler(2, 1);
	private final CraftingHandler smithingHandler = new CraftingHandler(3, 1);
	
	public BackpackHandler(Player player) {
		this.player = player;
		this.config = new BackpackConfig(this.player);
		this.smeltHandler = new SmeltingProgressHandler(this.player, this.furnaceHandler, BackpackConstants.FURNACE_RECIPE_TYPES);
		this.brewHandler = new BrewingProgressHandler(this.player, this.brewingHandler);
	}
	
	@Override
	public Player getPlayer() {
		return this.player;
	}
	
	@Override
	public BackpackConfig getConfig() {
		return this.config;
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
	public CraftingFuelHandler getBrewingHandler() {
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
	public boolean broadcastChanges() {
		if (this.player instanceof ServerPlayer player) {
			this.config.updateServer();
			XBNetworkHandler.INSTANCE.sendToPlayer(player, new UpdateBackpackPacket(this.serialize()));
			return true;
		}
		XBackpack.LOGGER.warn("Can not broadcast changes on the client");
		return false;
	}
	
	@Override
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("data_version", DATA_VERSION);
		tag.put("backpack_config", this.config.serialize());
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
		int dataVersion = 0;
		if (tag.contains("data_version")) {
			dataVersion = tag.getInt("data_version");
		}
		if (dataVersion == DATA_VERSION) {
			this.config.deserialize(tag.getCompound("backpack_config"));
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
		} else {
			XBackpack.LOGGER.error("The data version has changed, to prevent the loss of the backpack inventory, the game will be terminated");
			XBackpack.LOGGER.info("If you want to know how to update the data version, check out the linked wiki on CurseForge");
			System.exit(-1);
		}
	}
}
