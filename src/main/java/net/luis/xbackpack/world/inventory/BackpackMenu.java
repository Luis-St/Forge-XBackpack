package net.luis.xbackpack.world.inventory;

import java.util.List;

import com.google.common.collect.Lists;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.world.capability.BackpackProvider;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.extension.AnvilExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.BrewingStandExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.CraftingExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.EnchantmentTableExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.FurnaceExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.GrindstoneExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.SmithingTableExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.StonecutterExtensionMenu;
import net.luis.xbackpack.world.inventory.handler.ModifiableHandler;
import net.luis.xbackpack.world.inventory.modifier.filter.ItemFilters;
import net.luis.xbackpack.world.inventory.modifier.sorter.ItemSorters;
import net.luis.xbackpack.world.inventory.slot.BackpackArmorSlot;
import net.luis.xbackpack.world.inventory.slot.BackpackOffhandSlot;
import net.luis.xbackpack.world.inventory.slot.BackpackSlot;
import net.luis.xbackpack.world.inventory.slot.BackpackToolSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackMenu extends AbstractModifiableContainerMenu {
	
	private final ModifiableHandler handler;
	
	public BackpackMenu(int id, Inventory inventory, FriendlyByteBuf byteBuf) {
		this(id, inventory);
	}
	
	public BackpackMenu(int id, Inventory inventory) {
		super(XBMenuTypes.BACKPACK_MENU.get(), id, inventory);
		Player player = inventory.player;
		IBackpack backpack = BackpackProvider.get(player);
		this.handler = new ModifiableHandler(backpack.getBackpackHandler());
		for (int i = 0; i < this.handler.getSlots() / 9; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new BackpackSlot(this.handler, j + i * 9, 30 + j * 18, 18 + i * 18));
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
		this.addExtensionMenu(BackpackExtensions.CRAFTING_TABLE.get(), player, CraftingExtensionMenu::new);
		this.addExtensionMenu(BackpackExtensions.FURNACE.get(), player, FurnaceExtensionMenu::new);
		this.addExtensionMenu(BackpackExtensions.ANVIL.get(), player, AnvilExtensionMenu::new);
		this.addExtensionMenu(BackpackExtensions.ENCHANTMENT_TABLE.get(), player, EnchantmentTableExtensionMenu::new);
		this.addExtensionMenu(BackpackExtensions.STONECUTTER.get(), player, StonecutterExtensionMenu::new);
		this.addExtensionMenu(BackpackExtensions.BREWING_STAND.get(), player, BrewingStandExtensionMenu::new);
		this.addExtensionMenu(BackpackExtensions.GRINDSTONE.get(), player, GrindstoneExtensionMenu::new);
		this.addExtensionMenu(BackpackExtensions.SMITHING_TABLE.get(), player, SmithingTableExtensionMenu::new);
	}
	
	public ModifiableHandler getHandler() {
		return this.handler;
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
				if (!this.moveSpecial(slotStack)) {
					if (!this.moveExtension(slotStack, index)) { // into extension
						if (!this.moveInventory(slotStack)) { // into inventory
							return ItemStack.EMPTY;
						}
					}
				}
			} else if (908 >= index && index >= 873) { // from inventory
				stack = slotStack.copy();
				if (!this.moveSpecial(slotStack)) {
					if (!this.moveExtension(slotStack, index)) { // into extension
						if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
							return ItemStack.EMPTY;
						}
					}
				}
			} else if (916 >= index && index >= 909) { // from tool, armor or offhand slot
				stack = slotStack.copy();
				if (!this.moveExtension(slotStack, index)) { // into extension
					if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
						if (!this.moveInventory(slotStack)) { // into inventory
							return ItemStack.EMPTY;
						}
					}
				}
			} else if (956 >= index && index >= 917) { // from extensions
				stack = slotStack.copy();
				if (!this.moveExtension(slotStack, index)) { // into extension
					if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
						if (!this.moveInventory(slotStack)) { // into inventory
							return ItemStack.EMPTY;
						}
					}
				}
			}
			if (slotStack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			slot.onTake(player, slotStack);
		}
		return stack;
	}
	
	private boolean moveInventory(ItemStack slotStack) {
		if (!this.moveItemStackTo(slotStack, 900, 909, false)) { // into hotbar
			if (!this.moveItemStackTo(slotStack, 873, 900, false)) { // into inventory
				return false;
			}
		}
		return true;
	}
	
	private boolean moveSpecial(ItemStack slotStack) {
		if (BackpackConstans.VALID_TOOL_SLOT_ITEMS.contains(slotStack.getItem())) {
			if (this.moveItemStackTo(slotStack, 909, 912, false)) { // into tool slot
				return true;
			}
		} else if (BackpackConstans.SHIFTABLE_OFFHAND_SLOT_ITEMS.contains(slotStack.getItem())) {
			if (this.moveItemStackTo(slotStack, 916, 917, false)) { // into offhand slot
				return true;
			}
		} else if (BackpackConstans.VALID_ARMOR_SLOT_ITEMS.contains(slotStack.getItem())) {
			if (this.moveItemStackTo(slotStack, 912, 916, false)) { // into armor slot
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean clickMenuButton(Player player, int button) {
		if (this.getExtensionMenu().isEmpty()) {
			if (player instanceof ServerPlayer serverPlayer) {
				if (button == 0) {
					this.updateFilter(null, UpdateType.CYCLE, CycleDirection.FORWARDS);
					return true;
				} else if (button == 1) {
					this.updateFilter(null, UpdateType.CYCLE, CycleDirection.BACKWARDS);
					return true;
				} else if (button == 2) {
					this.updateSorter(null, UpdateType.CYCLE, CycleDirection.FORWARDS);
					return true;
				} else if (button == 3) {
					this.updateSorter(null, UpdateType.CYCLE, CycleDirection.BACKWARDS);
					return true;
				} else if (button == 4) {
					this.mergeInventory(serverPlayer);
					return true;
				}
			}
		}
		return super.clickMenuButton(player, button);
	}
	
	private void mergeInventory(ServerPlayer player) {
		List<ItemStack> failedStacks = Lists.newArrayList();
		ModifiableHandler handler = new ModifiableHandler(this.handler.getSlots());
		this.handler.resetWrappedSlots();
		for (int i = 0; i < this.handler.getSlots(); i++) {
			ItemStack stack = this.handler.getStackInSlot(i);
			if (stack.isEmpty()) {
				continue;
			} else {
				stack = handler.insertItem(stack, false);
				if (!stack.isEmpty()) {
					failedStacks.add(stack);
				}
			}
		}
		for (int i = 0; i < handler.getSlots(); i++) {
			if (this.handler.getWrappedSlot(i) != i) {
				this.handler.setWrappedSlot(i, i);
			}
			this.handler.setStackInSlot(i, handler.getStackInSlot(i));
		}
		failedStacks.removeIf(ItemStack::isEmpty);
		if (!failedStacks.isEmpty()) {
			failedStacks.forEach((stack) -> {
				player.drop(stack, false);
			});
			failedStacks.clear();
		}
		this.broadcastChanges();
	}
	
	@Override
	protected void onItemModifiersChanged(ServerPlayer player) {
		if (this.getFilter() == ItemFilters.NONE && this.getSorter() == ItemSorters.NONE) {
			this.handler.resetWrappedSlots();
		} else {
			List<ItemStack> stacks = this.handler.createModifiableList();
			if (!this.getSearchTerm().isEmpty()) {
				if (this.getSorter() == ItemSorters.NAME_SEARCH) {
					stacks.removeIf((stack) -> {
						return !ItemFilters.NAME_SEARCH.canKeepItem(stack, this.getSearchTerm());
					});
				} else if (this.getSorter() == ItemSorters.NAMESPACE_SEARCH) {
					stacks.removeIf((stack) -> {
						return !ItemFilters.NAMESPACE_SEARCH.canKeepItem(stack, this.getSearchTerm());
					});
				} else if (this.getSorter() == ItemSorters.TAG_SEARCH) {
					stacks.removeIf((stack) -> {
						return !ItemFilters.TAG_SEARCH.canKeepItem(stack, this.getSearchTerm());
					});
				} else if (this.getSorter() == ItemSorters.COUNT_SEARCH) {
					stacks.removeIf((stack) -> {
						return !ItemFilters.COUNT_SEARCH.canKeepItem(stack, this.getSearchTerm());
					});
				}
			}
			stacks.removeIf((stack) -> {
				return !this.getFilter().canKeepItem(stack, this.getSearchTerm());
			});
			this.handler.applyModifications(this.getSorter().sort(stacks, this.getSearchTerm()));
		}
		this.broadcastChanges();
	}
	
}
