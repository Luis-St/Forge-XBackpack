package net.luis.xbackpack.common.inventory.slot;

import com.mojang.datafixers.util.Pair;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.common.BackpackConstans;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackToolSlot extends SlotItemHandler {

	/**
	 * tool slot background texture as a {@link ResourceLocation}
	 */
	public static final ResourceLocation EMPTY_TOOL_SLOT = new ResourceLocation(XBackpack.MOD_ID, "item/empty_tool_slot");
	
	/**
	 * constructor for a {@link BackpackToolSlot}
	 */
	public BackpackToolSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	/**
	 * @return the max stack size for {@link TieredItem}s
	 */
	@Override
	public int getMaxStackSize() {
		return 1;
	}
	
	/**
	 * @return the max stack size for {@link TieredItem}s
	 */
	@Override
	public int getMaxStackSize(ItemStack stack) {
		return 1;
	}
	
	/**
	 * only {@link Item}s from {@link BackpackConstans#VALID_TOOL_SLOT_ITEMS}<br> 
	 * are valid for this Slot
	 */
	@Override
	public boolean mayPlace(ItemStack stack) {
		return BackpackConstans.VALID_TOOL_SLOT_ITEMS.contains(stack.getItem());
	}
	
	/**
	 * @return custom ToolSlot background
	 */
	@Override
	public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
		return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_TOOL_SLOT);
	}

}
