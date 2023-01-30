package net.luis.xbackpack.world.extension;

import net.luis.xbackpack.XBackpack;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 *
 * @author Luis-st
 *
 */

public class BackpackExtensions {
	
	public static final ResourceKey<Registry<BackpackExtension>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(XBackpack.MOD_ID, "backpack_extension"));
	public static final DeferredRegister<BackpackExtension> BACKPACK_EXTENSIONS = DeferredRegister.create(REGISTRY_KEY, XBackpack.MOD_ID);
	public static final Supplier<IForgeRegistry<BackpackExtension>> REGISTRY = BACKPACK_EXTENSIONS.makeRegistry(RegistryBuilder::new);
	
	public static final RegistryObject<BackpackExtension> NO = BACKPACK_EXTENSIONS.register("no", () -> {
		return new BackpackExtension(ItemStack.EMPTY, 0, 0, 0, 0);
	});
	public static final RegistryObject<BackpackExtension> CRAFTING_TABLE = BACKPACK_EXTENSIONS.register("crafting_table", () -> {
		return new BackpackExtension(new ItemStack(Items.CRAFTING_TABLE), 20, 22, 68, 135);
	});
	public static final RegistryObject<BackpackExtension> FURNACE = BACKPACK_EXTENSIONS.register("furnace", () -> {
		return new BackpackExtension(new ItemStack(Items.FURNACE), 20, 22, 86, 126);
	});
	public static final RegistryObject<BackpackExtension> ANVIL = BACKPACK_EXTENSIONS.register("anvil", () -> {
		return new BackpackExtension(new ItemStack(Items.ANVIL), 20, 22, 111, 46);
	});
	public static final RegistryObject<BackpackExtension> ENCHANTMENT_TABLE = BACKPACK_EXTENSIONS.register("enchantment_table", () -> {
		return new BackpackExtension(new ItemStack(Items.ENCHANTING_TABLE), 20, 22, 136, 88);
	});
	public static final RegistryObject<BackpackExtension> STONECUTTER = BACKPACK_EXTENSIONS.register("stonecutter", () -> {
		return new BackpackExtension(new ItemStack(Items.STONECUTTER), 20, 22, 95, 136);
	});
	public static final RegistryObject<BackpackExtension> BREWING_STAND = BACKPACK_EXTENSIONS.register("brewing_stand", () -> {
		return new BackpackExtension(new ItemStack(Items.BREWING_STAND), 20, 22, 106, 88);
	});
	public static final RegistryObject<BackpackExtension> GRINDSTONE = BACKPACK_EXTENSIONS.register("grindstone", () -> {
		return new BackpackExtension(new ItemStack(Items.GRINDSTONE), 20, 22, 112, 84);
	});
	public static final RegistryObject<BackpackExtension> SMITHING_TABLE = BACKPACK_EXTENSIONS.register("smithing_table", () -> {
		return new BackpackExtension(new ItemStack(Items.SMITHING_TABLE), 20, 22, 111, 46);
	});
	
}
