package net.luis.xbackpack.world.inventory;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.capability.XBackpackCapabilities;
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
	
	/**
	 * client constructor for the {@link BackpackMenu}
	 */
	public BackpackMenu(int id, Inventory inventory, FriendlyByteBuf byteBuf) {
		this(id, inventory);
	}
	
	/**
	 * constructor for the {@link BackpackMenu},
	 * add all Slots:
	 * <ul>
	 * 	<li>main backpack Slots</li>
	 * 	<li>{@link BackpackToolSlot}s</li>
	 * 	<li>player Inventory Slots</li>
	 * 	<li>player Hotbar Slots</li>
	 * 	<li>{@link BackpackArmorSlot}s</li>
	 * 	<li>{@link BackpackOffhandSlot}s</li>
	 * </ul>
	 */
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

	/**
	 * @return {@code true} that the {@link Player} can always open the backpack
	 */
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
