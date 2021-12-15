package net.luis.xbackpack.common.inventory.menu;

import net.luis.xbackpack.common.BackpackConstans;
import net.luis.xbackpack.common.capability.IBackpack;
import net.luis.xbackpack.common.inventory.slot.BackpackArmorSlot;
import net.luis.xbackpack.common.inventory.slot.BackpackOffhandSlot;
import net.luis.xbackpack.common.inventory.slot.BackpackToolSlot;
import net.luis.xbackpack.init.XBackpackCapabilities;
import net.luis.xbackpack.init.XBackpackMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

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
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new SlotItemHandler(backpack, j + i * 9, 8 + j * 18, (i * 18) + 18));
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(inventory, i, 8 + i * 18, 160));
		}
		this.addSlot(new BackpackToolSlot(backpack, 36, 174, 54));
		this.addSlot(new BackpackToolSlot(backpack, 37, 174, 72));
		this.addSlot(new BackpackArmorSlot(inventory, EquipmentSlot.HEAD, 39, -14, 18));
		this.addSlot(new BackpackArmorSlot(inventory, EquipmentSlot.CHEST, 38, -14, 36));
		this.addSlot(new BackpackArmorSlot(inventory, EquipmentSlot.LEGS, 37, -14, 54));
		this.addSlot(new BackpackArmorSlot(inventory, EquipmentSlot.FEET, 36, -14, 72));
		this.addSlot(new BackpackOffhandSlot(inventory, 40, -14, 160));
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.getSlot(index);
		if (slot != null && slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			stack = slotStack.copy();
			if (35 >= index && index >= 0) {
				if (!this.moveItemStackTo(slotStack, 63, 72, false)) {
					if (!this.moveItemStackTo(slotStack, 36, 63, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (71 >= index && index >= 36) {
				if (BackpackConstans.VALID_TOOL_SLOT_ITEMS.contains(slotStack.getItem())) {
					if (!this.moveItemStackTo(slotStack, 72, 74, false)) {
						if (!this.moveItemStackTo(slotStack, 0, 36, false)) {
							return ItemStack.EMPTY;
						}
					}
				} else if (BackpackConstans.SHIFTABLE_OFFHAND_SLOT_ITEMS.contains(slotStack.getItem())) {
					if (!this.moveItemStackTo(slotStack, 78, 79, false)) {
						if (!this.moveItemStackTo(slotStack, 0, 36, false)) {
							return ItemStack.EMPTY;
						}
					}
				} else if (BackpackConstans.VALID_ARMOR_SLOT_ITEMS.contains(slotStack.getItem())) {
					if (!this.moveItemStackTo(slotStack, 74, 78, false)) {
						if (!this.moveItemStackTo(slotStack, 0, 36, false)) {
							return ItemStack.EMPTY;
						}
					}
				} else if (!this.moveItemStackTo(slotStack, 0, 36, false)) {
					return ItemStack.EMPTY;
				}
			} else if (78 >= index && index >= 72) {
				if (!this.moveItemStackTo(slotStack, 63, 72, false)) {
					if (!this.moveItemStackTo(slotStack, 36, 63, false)) {
						if (!this.moveItemStackTo(slotStack, 0, 36, false)) {
							return ItemStack.EMPTY;
						}
					}
				}
			}
		}
		return stack;
	}

}
