package net.luis.xbackpack;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

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
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackConstans {
	
	private static final List<Item> ITEM = ImmutableList.copyOf(ForgeRegistries.ITEMS.getValues());
	
	/**
	 * add items to this list in {@link FMLCommonSetupEvent},<br>
	 * if you want to add valid items for the tool slot
	 */
	public static final List<Item> VALID_TOOL_SLOT_ITEMS = ITEM.stream().filter(item -> {
		return item instanceof TieredItem || item instanceof ShearsItem || item instanceof FlintAndSteelItem || item instanceof BowItem || item instanceof CrossbowItem;
	}).collect(Collectors.toList());
	
	/**
	 * add items to this list in {@link FMLCommonSetupEvent},<br>
	 * if you want to add valid items for the armor slot<br>
	 * <br>
	 * note: the item can only be place in the Slot if {@link IForgeItem#canEquip()} returns {@code true}
	 */
	public static final List<Item> VALID_ARMOR_SLOT_ITEMS = ITEM.stream().filter(item -> {
		return item instanceof ArmorItem || item instanceof ElytraItem || item == Items.CARVED_PUMPKIN;
	}).collect(Collectors.toList());
	
	/**
	 * add items to this list in {@link FMLCommonSetupEvent},<br> 
	 * if you want to quick move the item via shift into the offhand slot
	 */
	public static final List<Item> SHIFTABLE_OFFHAND_SLOT_ITEMS = ITEM.stream().filter(item -> {
		return item instanceof ShieldItem || item == Items.TORCH || item == Items.SOUL_TORCH || item.isEdible();
	}).collect(Collectors.toList());
	
}
