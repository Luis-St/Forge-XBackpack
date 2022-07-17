package net.luis.xbackpack.world.inventory;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.capability.XBackpackCapabilities;
import net.luis.xbackpack.world.inventory.slot.BackpackArmorSlot;
import net.luis.xbackpack.world.inventory.slot.BackpackOffhandSlot;
import net.luis.xbackpack.world.inventory.slot.BackpackSlot;
import net.luis.xbackpack.world.inventory.slot.BackpackToolSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackMenu extends AbstractContainerMenu {
	
	public BackpackMenu(int id, Inventory inventory, FriendlyByteBuf byteBuf) {
		this(id, inventory);
	}
	
	public BackpackMenu(int id, Inventory inventory) {
		super(XBackpackMenuTypes.BACKPACK_MENU.get(), id);
		Player player = inventory.player;
		IBackpack backpack = player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new);
		int rows = (BackpackConstans.BACKPACK_SLOT_COUNT - 3) / 9;
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new BackpackSlot(backpack, index++, 30 + j * 18, (i * 18) + 18));
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(inventory, j + i * 9 + 9, 30 + j * 18, 138 + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(inventory, i, 30 + i * 18, 196));
		}
		this.addSlot(new BackpackToolSlot(backpack, index++, 196, 138));
		this.addSlot(new BackpackToolSlot(backpack, index++, 196, 156));
		this.addSlot(new BackpackToolSlot(backpack, index++, 196, 174));
		this.addSlot(new BackpackArmorSlot(inventory, EquipmentSlot.HEAD, 39, 8, 18));
		this.addSlot(new BackpackArmorSlot(inventory, EquipmentSlot.CHEST, 38, 8, 36));
		this.addSlot(new BackpackArmorSlot(inventory, EquipmentSlot.LEGS, 37, 8, 54));
		this.addSlot(new BackpackArmorSlot(inventory, EquipmentSlot.FEET, 36, 8, 72));
		this.addSlot(new BackpackOffhandSlot(inventory, 40, 8, 196));
	}
	
	@Override
	public boolean stillValid(Player player) {
		return true;
	}
	
	/**
	 * handle the quick move of {@link ItemStack}s:
	 * <ol>
	 * 	<li>try to move ItemStack frome Menu to Inventory</li>
	 * 	<li>try to move ItemStack frome Inventory to Menu</li>
	 * 	<li>try to move ItemStack into {@link BackpackToolSlot},<br> 
	 * 		if the item is in {@link BackpackConstans#VALID_TOOL_SLOT_ITEMS}<br>
	 * 		else try to move into Menu
	 * 	</li>
	 * 	<li>try to move ItemStack into {@link BackpackOffhandSlot},<br>
	 * 		if the item is in {@link BackpackConstans#SHIFTABLE_OFFHAND_SLOT_ITEMS}<br>
	 * 		else try to move into Menu
	 * 	</li>
	 *	<li>try to move ItemStack into {@link BackpackArmorSlot},<br>
	 *		if the item is in {@link BackpackConstans#VALID_ARMOR_SLOT_ITEMS}<br>
	 *		else try to move into Menu
	 *	</li>
	 * 	<li>fallback move for all other Items</li>
	 * </ol>
	 */
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		XBackpack.LOGGER.info("index {}", index);
		ItemStack stack = ItemStack.EMPTY;
//		Slot slot = this.getSlot(index);
//		if (slot != null && slot.hasItem()) {
//			ItemStack slotStack = slot.getItem();
//			stack = slotStack.copy();
//			if (35 >= index && index >= 0) {
//				if (!this.moveItemStackTo(slotStack, 63, 72, false)) {
//					if (!this.moveItemStackTo(slotStack, 36, 63, false)) {
//						return ItemStack.EMPTY;
//					}
//				}
//			} else if (71 >= index && index >= 36) {
//				if (BackpackConstans.VALID_TOOL_SLOT_ITEMS.contains(slotStack.getItem())) {
//					if (!this.moveItemStackTo(slotStack, 72, 74, false)) {
//						if (!this.moveItemStackTo(slotStack, 0, 36, false)) {
//							return ItemStack.EMPTY;
//						}
//					}
//				} else if (BackpackConstans.SHIFTABLE_OFFHAND_SLOT_ITEMS.contains(slotStack.getItem())) {
//					if (!this.moveItemStackTo(slotStack, 78, 79, false)) {
//						if (!this.moveItemStackTo(slotStack, 0, 36, false)) {
//							return ItemStack.EMPTY;
//						}
//					}
//				} else if (BackpackConstans.VALID_ARMOR_SLOT_ITEMS.contains(slotStack.getItem())) {
//					if (!this.moveItemStackTo(slotStack, 74, 78, false)) {
//						if (!this.moveItemStackTo(slotStack, 0, 36, false)) {
//							return ItemStack.EMPTY;
//						}
//					}
//				} else if (!this.moveItemStackTo(slotStack, 0, 36, false)) {
//					return ItemStack.EMPTY;
//				}
//			} else if (78 >= index && index >= 72) {
//				if (!this.moveItemStackTo(slotStack, 63, 72, false)) {
//					if (!this.moveItemStackTo(slotStack, 36, 63, false)) {
//						if (!this.moveItemStackTo(slotStack, 0, 36, false)) {
//							return ItemStack.EMPTY;
//						}
//					}
//				}
//			}
//		}
		return stack;
	}

}
