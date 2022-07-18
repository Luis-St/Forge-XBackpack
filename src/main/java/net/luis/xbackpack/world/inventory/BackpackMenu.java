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
		this.addSlot(new BackpackToolSlot(backpack, index, 196, 174));
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
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		XBackpack.LOGGER.info("index {}", index);
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.getSlot(index);
		if (slot != null && slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			if (872 >= index && index >= 0) { // from menu
				stack = slotStack.copy();
				if (!this.moveItemStackTo(slotStack, 900, 909, false)) { // into hotbar
					if (!this.moveItemStackTo(slotStack, 873, 900, false)) { // into inv
						return ItemStack.EMPTY;
					}
				}
			} else if (908 >= index && index >= 873) { // from inv
				stack = slotStack.copy();
				if (BackpackConstans.VALID_TOOL_SLOT_ITEMS.contains(slotStack.getItem())) {
					if (!this.moveItemStackTo(slotStack, 909, 912, false)) { // into tool slot
						if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
							return ItemStack.EMPTY;
						}
					}
				} else if (BackpackConstans.SHIFTABLE_OFFHAND_SLOT_ITEMS.contains(slotStack.getItem())) {
					if (!this.moveItemStackTo(slotStack, 916, 917, false)) { // into offhand slot
						if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
							return ItemStack.EMPTY;
						}
					}
				} else if (BackpackConstans.VALID_ARMOR_SLOT_ITEMS.contains(slotStack.getItem())) {
					if (!this.moveItemStackTo(slotStack, 912, 916, false)) { // into armor slot
						if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
							return ItemStack.EMPTY;
						}
					}
				} else if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
					return ItemStack.EMPTY;
				}
			} else if (916 >= index && index >= 909) { // from tool, armor or offhand slot
				stack = slotStack.copy();
				if (!this.moveItemStackTo(slotStack, 900, 909, false)) { // into hotbar
					if (!this.moveItemStackTo(slotStack, 873, 900, false)) { // into inv
						if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
							return ItemStack.EMPTY;
						}
					}
				}
			}
		}
		return stack;
	}

}
