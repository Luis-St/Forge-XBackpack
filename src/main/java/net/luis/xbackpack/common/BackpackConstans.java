package net.luis.xbackpack.common;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.core.Registry;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.common.extensions.IForgeItem;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackConstans {
	
	/**
	 * a List of all registered {@link Item}s
	 */
	@SuppressWarnings("deprecation")
	private static final List<Item> ITEM = Registry.ITEM.stream().collect(Collectors.toList());
	
	/**
	 * add Items to this List in {@link FMLCommonSetupEvent},<br>
	 * if you want to add valid Items for the {@link ToolSlot}
	 */
	public static final List<Item> VALID_TOOL_SLOT_ITEMS = ITEM.stream().filter(item -> {
		return item instanceof TieredItem || item instanceof ShearsItem || item instanceof FlintAndSteelItem || item instanceof BowItem || item instanceof CrossbowItem;
	}).collect(Collectors.toList());
	
	/**
	 * add Items to this List in {@link FMLCommonSetupEvent},<br>
	 * if you want to add valid Items for the {@link ArmorSlot}<br>
	 * <br>
	 * note: the {@link Item} can only be place in the Slot if {@link IForgeItem#canEquip()} returns {@code true}
	 */
	public static final List<Item> VALID_ARMOR_SLOT_ITEMS = ITEM.stream().filter(item -> {
		return item instanceof ArmorItem || item instanceof ElytraItem || item == Items.CARVED_PUMPKIN;
	}).collect(Collectors.toList());
	
	/**
	 * add Items to this List in {@link FMLCommonSetupEvent},<br> 
	 * if you want to quick move the {@link Item} via shift into the {@link OffhandSlot}
	 */
	public static final List<Item> SHIFTABLE_OFFHAND_SLOT_ITEMS = ITEM.stream().filter(item -> {
		return item instanceof ShieldItem || item == Items.TORCH || item == Items.SOUL_TORCH || item.isEdible();
	}).collect(Collectors.toList());
	
}
