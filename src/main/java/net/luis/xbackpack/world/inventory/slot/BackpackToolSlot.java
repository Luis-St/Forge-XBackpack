package net.luis.xbackpack.world.inventory.slot;

import com.mojang.datafixers.util.Pair;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.XBackpack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackToolSlot extends SlotItemHandler {
	
	private static final ResourceLocation EMPTY_TOOL_SLOT = new ResourceLocation(XBackpack.MOD_ID, "item/empty_tool_slot");
	
	public BackpackToolSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public int getMaxStackSize() {
		return 1;
	}
	
	@Override
	public int getMaxStackSize(ItemStack stack) {
		return 1;
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return BackpackConstans.VALID_TOOL_SLOT_ITEMS.contains(stack.getItem());
	}
	
	@Override
	public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
		return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_TOOL_SLOT);
	}

}
