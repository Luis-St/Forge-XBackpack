package net.luis.xbackpack.world.inventory;

import com.mojang.datafixers.util.Pair;

import net.luis.xbackpack.BackpackConstans;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackArmorSlot extends Slot {
	
	/**
	 * the related {@link Player}
	 */
	private final Player player;
	
	/**
	 * the related {@link EquipmentSlot}
	 */
	private final EquipmentSlot equipmentSlot;
	
	/**
	 * constructor for a {@link BackpackArmorSlot}
	 */
	public BackpackArmorSlot(Inventory inventory, EquipmentSlot equipmentSlot, int index, int xPosition, int yPosition) {
		super(inventory, index, xPosition, yPosition);
		this.player = inventory.player;
		this.equipmentSlot = equipmentSlot;
	}
	
	/**
	 * @return the max stack size for {@link ArmorItem}s
	 */
	@Override
	public int getMaxStackSize() {
		return 1;
	}
	
	/**
	 * @return the max stack size for {@link ArmorItem}s
	 */
	@Override
	public int getMaxStackSize(ItemStack stack) {
		return 1;
	}

	/**
	 * only {@link ArmorItem}s or {@link Item}s which can be used as a armor<br>
	 * are valid for this Slot.<br> 
	 * at this point we ignore the {@link BackpackConstans#VALID_ARMOR_SLOT_ITEMS}
	 */
	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.canEquip(this.equipmentSlot, this.player);
	}
	
	/**
	 * copy the logic from the armor solt of the player inventory 
	 */
	@Override
	public boolean mayPickup(Player player) {
		ItemStack stack = this.getItem();
		return !stack.isEmpty() && !player.isCreative() && EnchantmentHelper.hasBindingCurse(stack) ? false : super.mayPickup(player);
	}
	
	/**
	 * @return correct background for the Slot based on the {@link BackpackArmorSlot#equipmentSlot}
	 */
	@Override
	public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
		switch (this.equipmentSlot) {
		case HEAD: return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET);
		case CHEST: return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE);
		case LEGS: return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS);
		case FEET: return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS);
		default: break;
		}
		return super.getNoItemIcon();
	}
	
}
