package net.luis.xbackpack.world.inventory;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.luis.xbackpack.BackpackConstans;
import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.capability.IBackpack;
import net.luis.xbackpack.world.capability.XBCapabilities;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.extension.AbstractExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.AnvilExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.BrewingStandExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.CraftingExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.EnchantmentTableExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.ExtensionMenuHolder;
import net.luis.xbackpack.world.inventory.extension.FurnaceExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.GrindstoneExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.SmithingTableExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.StonecutterExtensionMenu;
import net.luis.xbackpack.world.inventory.handler.BrewingHandler;
import net.luis.xbackpack.world.inventory.handler.EnchantingHandler;
import net.luis.xbackpack.world.inventory.slot.BackpackArmorSlot;
import net.luis.xbackpack.world.inventory.slot.BackpackOffhandSlot;
import net.luis.xbackpack.world.inventory.slot.BackpackSlot;
import net.luis.xbackpack.world.inventory.slot.BackpackToolSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
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
		super(XBMenuTypes.BACKPACK_MENU.get(), id);
		Player player = inventory.player;
		IBackpack backpack = player.getCapability(XBCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new);
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
		this.extensionMenus.add(new BrewingStandExtensionMenu(this, player));
		this.extensionMenus.add(new GrindstoneExtensionMenu(this, player));
		this.extensionMenus.add(new SmithingTableExtensionMenu(this, player));
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
				if (this.extension == BackpackExtension.CRAFTING_TABLE.get()) {
					if (!this.moveItemStackTo(slotStack, 917, 927, false)) { // into crafting table
						if (!this.moveInventory(slotStack)) { // into inventory
							return ItemStack.EMPTY;
						}
					}
				} else if (this.extension == BackpackExtension.FURNACE.get()) {
					if (FurnaceExtensionMenu.canSmelt(player, slotStack)) {
						if (!this.moveItemStackTo(slotStack, 931, 932, false)) { // into furnace input
							if (!this.moveItemStackTo(slotStack, 927, 931, false)) { // into furnace input storage
								if (!this.moveInventory(slotStack)) { // into inventory
									return ItemStack.EMPTY;
								}
							}
						}
					} else if (FurnaceExtensionMenu.isFuel(slotStack) || slotStack.is(Items.BUCKET)) {
						if (!this.moveItemStackTo(slotStack, 932, 933, false)) { // into furnace fuel
							if (!this.moveInventory(slotStack)) { // into inventory
								return ItemStack.EMPTY;
							}
						}
					}
				} else if (this.extension == BackpackExtension.ANVIL.get()) {
					if (!this.moveItemStackTo(slotStack, 938, 940, false)) { // into anvil input
						if (!this.moveInventory(slotStack)) { // into inventory
							return ItemStack.EMPTY;
						}
					}
				} else if (this.extension == BackpackExtension.ENCHANTMENT_TABLE.get()) {
					if (slotStack.is(Tags.Items.BOOKSHELVES) || (this.canQuickMoveBook(player) && slotStack.getItem() instanceof BookItem)) {
						if (!this.moveItemStackTo(slotStack, 941, 942, false)) { // into enchantment table power
							if (!this.moveInventory(slotStack)) { // into inventory
								return ItemStack.EMPTY;
							}
						}
					} else if (slotStack.isEnchantable() || slotStack.getItem() instanceof BookItem) {
						if (!this.moveItemStackTo(slotStack, 942, 943, false)) { // into enchantment table input
							if (!this.moveInventory(slotStack)) { // into inventory
								return ItemStack.EMPTY;
							}
						}
					} else if (slotStack.is(Tags.Items.ENCHANTING_FUELS)) {
						if (!this.moveItemStackTo(slotStack, 943, 944, false)) { // into enchantment table fuel
							if (!this.moveInventory(slotStack)) { // into inventory
								return ItemStack.EMPTY;
							}
						}
					}
				} else if (this.extension == BackpackExtension.STONECUTTER.get()) {
					if (player.level.getRecipeManager().getRecipeFor(RecipeType.STONECUTTING, new SimpleContainer(slotStack), player.level).isPresent()) {
						if (!this.moveItemStackTo(slotStack, 944, 945, false)) { // into stonecutter input
							if (!this.moveInventory(slotStack)) { // into inventory
								return ItemStack.EMPTY;
							}
						}
					}
				} else if (this.extension == BackpackExtension.BREWING_STAND.get()) {
					XBackpack.LOGGER.info("extension is brewing stand");
					if (slotStack.is(Items.BLAZE_POWDER) && this.canQuickMovePowder(player)) {
						if (!this.moveItemStackTo(slotStack, 947, 948, false)) { // into brewing stand fuel
							if (!this.moveInventory(slotStack)) { // into inventory
								return ItemStack.EMPTY;
							}
						}
					} else if (BrewingRecipeRegistry.isValidIngredient(slotStack)) {
						if (!this.moveItemStackTo(slotStack, 946, 947, false)) { // into brewing stand input
							if (!this.moveInventory(slotStack)) { // into inventory
								return ItemStack.EMPTY;
							}
						}
					} else if (BrewingRecipeRegistry.isValidInput(slotStack)) {
						XBackpack.LOGGER.info("isValidInput");
						if (!this.moveItemStackTo(slotStack, 948, 451, false)) { // into brewing stand result
							if (!this.moveInventory(slotStack)) { // into inventoryentory
								return ItemStack.EMPTY;
							}
						}
					}
				} else if (this.extension == BackpackExtension.GRINDSTONE.get()) {
					if (slotStack.isDamageableItem() || slotStack.is(Items.ENCHANTED_BOOK) || slotStack.isEnchanted()) {
						if (!this.moveItemStackTo(slotStack, 951, 953, false)) { // into grindstone input
							if (!this.moveInventory(slotStack)) { // into inventory
								return ItemStack.EMPTY;
							}
						}
					}
				} else if (this.extension == BackpackExtension.SMITHING_TABLE.get()) {
					if (this.canQuickMoveIngredient(player, slotStack)) {
						if (!this.moveItemStackTo(slotStack, 955, 956, false)) { // into smithing table addition
							if (!this.moveInventory(slotStack)) { // into inventory
								return ItemStack.EMPTY;
							}
						}
					} else if (!this.moveItemStackTo(slotStack, 954, 955, false)) { // into smithing table input
						if (!this.moveInventory(slotStack)) { // into inventory
							return ItemStack.EMPTY;
						}
					}
				} else {
					if (!this.moveInventory(slotStack)) { // into inventory
						return ItemStack.EMPTY;
					}
				}
			} else if (908 >= index && index >= 873) { // from inventory
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
				} else if (this.extension == BackpackExtension.CRAFTING_TABLE.get()) {
					if (!this.moveItemStackTo(slotStack, 917, 927, false)) { // into crafting table
						if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
							return ItemStack.EMPTY;
						}
					}
				} else if (this.extension == BackpackExtension.FURNACE.get()) {
					if (FurnaceExtensionMenu.canSmelt(player, slotStack)) {
						if (!this.moveItemStackTo(slotStack, 931, 932, false)) { // into furnace input
							if (!this.moveItemStackTo(slotStack, 927, 931, false)) { // into furnace input storage
								if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
									return ItemStack.EMPTY;
								}
							}
						}
					} else if (FurnaceExtensionMenu.isFuel(slotStack) || slotStack.is(Items.BUCKET)) {
						if (!this.moveItemStackTo(slotStack, 932, 933, false)) { // into furnace fuel
							if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
								return ItemStack.EMPTY;
							}
						}
					}
				} else if (this.extension == BackpackExtension.ANVIL.get()) {
					if (!this.moveItemStackTo(slotStack, 938, 940, false)) { // into anvil input
						if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
							return ItemStack.EMPTY;
						}
					}
				} else if (this.extension == BackpackExtension.ENCHANTMENT_TABLE.get()) {
					if (slotStack.is(Tags.Items.BOOKSHELVES) || (this.canQuickMoveBook(player) && slotStack.getItem() instanceof BookItem)) {
						if (!this.moveItemStackTo(slotStack, 941, 942, false)) { // into enchantment table power
							if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
								return ItemStack.EMPTY;
							}
						}
					} else if (slotStack.isEnchantable() || slotStack.getItem() instanceof BookItem) {
						if (!this.moveItemStackTo(slotStack, 942, 943, false)) { // into enchantment table input
							if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
								return ItemStack.EMPTY;
							}
						}
					} else if (slotStack.is(Tags.Items.ENCHANTING_FUELS)) {
						if (!this.moveItemStackTo(slotStack, 943, 944, false)) { // into enchantment table fuel
							if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
								return ItemStack.EMPTY;
							}
						}
					}
				} else if (this.extension == BackpackExtension.STONECUTTER.get()) {
					if (player.level.getRecipeManager().getRecipeFor(RecipeType.STONECUTTING, new SimpleContainer(slotStack), player.level).isPresent()) {
						if (!this.moveItemStackTo(slotStack, 944, 945, false)) { // into stonecutter input
							if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
								return ItemStack.EMPTY;
							}
						}
					}
				} else if (this.extension == BackpackExtension.BREWING_STAND.get()) {
					XBackpack.LOGGER.info("extension is brewing stand");
					if (slotStack.is(Items.BLAZE_POWDER) && this.canQuickMovePowder(player)) {
						if (!this.moveItemStackTo(slotStack, 947, 948, false)) { // into brewing stand fuel
							if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
								return ItemStack.EMPTY;
							}
						}
					} else if (BrewingRecipeRegistry.isValidIngredient(slotStack)) {
						if (!this.moveItemStackTo(slotStack, 946, 947, false)) { // into brewing stand input
							if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
								return ItemStack.EMPTY;
							}
						}
					} else if (BrewingRecipeRegistry.isValidInput(slotStack)) {
						XBackpack.LOGGER.info("isValidInput");
						if (!this.moveItemStackTo(slotStack, 948, 451, false)) { // into brewing stand result
							if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
								return ItemStack.EMPTY;
							}
						}
					}
				} else if (this.extension == BackpackExtension.GRINDSTONE.get()) {
					if (slotStack.isDamageableItem() || slotStack.is(Items.ENCHANTED_BOOK) || slotStack.isEnchanted()) {
						if (!this.moveItemStackTo(slotStack, 951, 953, false)) { // into grindstone input
							if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
								return ItemStack.EMPTY;
							}
						}
					}
				} else if (this.extension == BackpackExtension.SMITHING_TABLE.get()) {
					if (this.canQuickMoveIngredient(player, slotStack)) {
						if (!this.moveItemStackTo(slotStack, 955, 956, false)) { // into smithing table addition
							if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
								return ItemStack.EMPTY;
							}
						}
					} else if (!this.moveItemStackTo(slotStack, 954, 955, false)) { // into smithing table input
						if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
							return ItemStack.EMPTY;
						}
					}
				} else {
					if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
						return ItemStack.EMPTY;
					}
				}
			} else if (916 >= index && index >= 909) { // from tool, armor or offhand slot
				stack = slotStack.copy();
				if (!this.moveItemStackTo(slotStack, 900, 909, false)) { // into hotbar
					if (!this.moveItemStackTo(slotStack, 873, 900, false)) { // into inventory
						if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
							return ItemStack.EMPTY;
						}
					}
				}
			} else if (index == 940) { // anvil result quick move disabled -> delayed
				return ItemStack.EMPTY;
			} else if (956 >= index && index >= 917) { // from extensions
				stack = slotStack.copy();
				if (!this.moveItemStackTo(slotStack, 0, 873, false)) { // into menu
					if (!this.moveItemStackTo(slotStack, 873, 900, false)) { // into inventory
						if (!this.moveItemStackTo(slotStack, 900, 909, false)) { // into hotbar
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
	
	private boolean canQuickMoveBook(Player player) {
		EnchantingHandler handler = player.getCapability(XBCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new).getEnchantingHandler();
		if (!handler.getInputHandler().getStackInSlot(0).isEmpty()) {
			ItemStack stack = handler.getPowerHandler().getStackInSlot(0);
			return stack.isEmpty() || (stack.is(Items.BOOK) && stack.getMaxStackSize() > stack.getCount());
		}
		return false;
	}
	
	private boolean canQuickMovePowder(Player player) {
		BrewingHandler handler = player.getCapability(XBCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new).getBrewingHandler();
		ItemStack stack = handler.getFuelHandler().getStackInSlot(0);
		return stack.isEmpty() || (stack.is(Items.BLAZE_POWDER) && stack.getMaxStackSize() > stack.getCount());
	}
	
	private boolean canQuickMoveIngredient(Player player, ItemStack stack) {
		return player.level.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING).stream().anyMatch((recipe) -> {
			return recipe.isAdditionIngredient(stack);
		});
	}
	
	public boolean requiresTickUpdate() {
		return this.extensionMenus.stream().filter(AbstractExtensionMenu::requiresTickUpdate).findAny().isPresent();
	}
	
	public void tick() {
		this.extensionMenus.stream().filter(AbstractExtensionMenu::requiresTickUpdate).forEach(AbstractExtensionMenu::slotsChanged);
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
