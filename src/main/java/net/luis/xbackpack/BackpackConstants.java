package net.luis.xbackpack;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Luis-st
 *
 */

public class BackpackConstants {
	
	/**
	 * Add recipe types to this list in {@link FMLCommonSetupEvent},<br>
	 * if you want to allow the recipe type in the {@link BackpackExtensions#FURNACE Furnace Extension}
	 */
	public static final List<RecipeType<? extends AbstractCookingRecipe>> FURNACE_RECIPE_TYPES = Lists.newArrayList(RecipeType.SMELTING, RecipeType.BLASTING, RecipeType.SMOKING);
	private static final List<Item> ITEM = ImmutableList.copyOf(ForgeRegistries.ITEMS.getValues());
	
	/**
	 * Add items to this list in {@link FMLCommonSetupEvent},<br>
	 * if you want to add valid items for the tool slot
	 */
	public static final List<Item> VALID_TOOL_SLOT_ITEMS = ITEM.stream().filter(item -> {
		return item instanceof TieredItem || item instanceof ShearsItem || item instanceof FlintAndSteelItem || item instanceof BowItem || item instanceof CrossbowItem;
	}).collect(Collectors.toList());
	
	/**
	 * Add items to this list in {@link FMLCommonSetupEvent},<br>
	 * if you want to add valid items for the armor slot<br>
	 * <br>
	 * note: the item can only be place in the Slot if {@link ItemStack#canEquip(EquipmentSlot, Entity)} returns {@code true}
	 */
	public static final List<Item> VALID_ARMOR_SLOT_ITEMS = ITEM.stream().filter(item -> {
		return item instanceof ArmorItem || item instanceof ElytraItem || item == Items.CARVED_PUMPKIN;
	}).collect(Collectors.toList());
	
	/**
	 * Add items to this list in {@link FMLCommonSetupEvent},<br>
	 * if you want to quick move the item via shift into the offhand slot
	 */
	public static final List<Item> SHIFTABLE_OFFHAND_SLOT_ITEMS = ITEM.stream().filter(item -> {
		return item instanceof ShieldItem || item == Items.TORCH || item == Items.SOUL_TORCH || item.isEdible();
	}).collect(Collectors.toList());
}
