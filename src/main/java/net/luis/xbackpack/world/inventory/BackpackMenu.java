package net.luis.xbackpack.world.inventory;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.capability.XBackpackCapabilities;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.extension.AbstractExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.AnvilExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.CraftingExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.EnchantmentTableExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.ExtensionMenuHolder;
import net.luis.xbackpack.world.inventory.extension.FurnaceExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.StonecutterExtensionMenu;
import net.luis.xbackpack.world.inventory.slot.BackpackArmorSlot;
import net.luis.xbackpack.world.inventory.slot.BackpackOffhandSlot;
import net.luis.xbackpack.world.inventory.slot.BackpackSlot;
import net.luis.xbackpack.world.inventory.slot.BackpackToolSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackMenu extends AbstractContainerMenu implements ExtensionMenuHolder {
	
	private final List<AbstractExtensionMenu> extensionMenus = Lists.newArrayList();
	private BackpackExtension extension = BackpackExtension.NO.get();
	
	public BackpackMenu(int id, Inventory inventory, FriendlyByteBuf byteBuf) {
		this(id, inventory);
	}
	
	public BackpackMenu(int id, Inventory inventory) {
		super(XBackpackMenuTypes.BACKPACK_MENU.get(), id);
		Player player = inventory.player;
		IBackpack backpack = player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new);
		ItemStackHandler handler = backpack.getBackpackHandler();
		for (int i = 0; i < handler.getSlots() / 9; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new BackpackSlot(handler, j + i * 9, 30 + j * 18, 18 + i * 18));
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
		this.addSlot(new BackpackToolSlot(backpack.getToolHandler(), 0, 196, 138)); // top
		this.addSlot(new BackpackToolSlot(backpack.getToolHandler(), 1, 196, 156)); // mid
		this.addSlot(new BackpackToolSlot(backpack.getToolHandler(), 2, 196, 174)); // down
		this.addSlot(new BackpackArmorSlot(inventory, EquipmentSlot.HEAD, 39, 8, 18));
		this.addSlot(new BackpackArmorSlot(inventory, EquipmentSlot.CHEST, 38, 8, 36));
		this.addSlot(new BackpackArmorSlot(inventory, EquipmentSlot.LEGS, 37, 8, 54));
		this.addSlot(new BackpackArmorSlot(inventory, EquipmentSlot.FEET, 36, 8, 72));
		this.addSlot(new BackpackOffhandSlot(inventory, 40, 8, 196));
		this.extensionMenus.add(new CraftingExtensionMenu(this, player));
		this.extensionMenus.add(new FurnaceExtensionMenu(this, player));
		this.extensionMenus.add(new AnvilExtensionMenu(this, player));
		this.extensionMenus.add(new EnchantmentTableExtensionMenu(this, player));
		this.extensionMenus.add(new StonecutterExtensionMenu(this, player));
		this.extensionMenus.forEach((extensionMenu) -> {
			extensionMenu.addSlots(this::addSlot);
		});
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
		// TODO: add extensions quick move
		return stack;
	}
	
	@Override
	public void slotsChanged(Container container) {
		super.slotsChanged(container);
		this.extensionMenus.forEach((extensionMenu) -> {
			extensionMenu.slotsChanged(container);
		});
	}
	
	@Override
	public boolean clickMenuButton(Player player, int button) {
		AbstractExtensionMenu extensionMenu = this.getExtensionMenu(this.extension);
		if (extensionMenu != null) {
			return extensionMenu.clickMenuButton(player, button);
		}
		return super.clickMenuButton(player, button);
	}
	
	public BackpackExtension getExtension() {
		return this.extension;
	}
	
	public void setExtension(BackpackExtension extension) {
		if (this.extension != extension) {
			AbstractExtensionMenu extensionMenu = this.getExtensionMenu(this.extension);
			if (extensionMenu != null) {
				extensionMenu.close();
			}
		}
		this.extension = extension;
		AbstractExtensionMenu extensionMenu = this.getExtensionMenu(this.extension);
		if (extensionMenu != null) {
			extensionMenu.open();
		}
	}

	@Override
	public List<AbstractExtensionMenu> getExtensionMenus() {
		return ImmutableList.copyOf(this.extensionMenus);
	}

	@Override
	public AbstractExtensionMenu getExtensionMenu(BackpackExtension extension) {
		for (AbstractExtensionMenu extensionMenu : this.extensionMenus) {
			if (extensionMenu.getExtension() == extension) {
				return extensionMenu;
			}
		}
		return null;
	}
	
}
