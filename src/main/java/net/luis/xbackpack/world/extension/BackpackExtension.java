package net.luis.xbackpack.world.extension;

import java.util.function.Supplier;

import net.luis.xbackpack.XBackpack;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public record BackpackExtension(Component title, Component tooltip, ItemStack icon, int iconWidth, int iconHeight, int imageWidth, int imageHeight) {
	
	public static final ResourceKey<Registry<BackpackExtension>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(XBackpack.MOD_ID, "backpack_extension"));
	public static final DeferredRegister<BackpackExtension> BACKPACK_EXTENSIONS = DeferredRegister.create(REGISTRY_KEY, XBackpack.MOD_ID);
	public static final Supplier<IForgeRegistry<BackpackExtension>> REGISTRY = BACKPACK_EXTENSIONS.makeRegistry(RegistryBuilder::new);
	
	public static final RegistryObject<BackpackExtension> NO = BACKPACK_EXTENSIONS.register("no", () -> {
		return new BackpackExtension(Component.empty(), Component.empty(), ItemStack.EMPTY, 0, 0, 0, 0);
	});
	public static final RegistryObject<BackpackExtension> CRAFTING_TABLE = BACKPACK_EXTENSIONS.register("crafting_table", () -> {
		String component = "xbackpack.backpack_extension.crafting_table.";
		return new BackpackExtension(Component.translatable(component + "title"), Component.translatable(component + "tooltip"), new ItemStack(Items.CRAFTING_TABLE), 20, 22, 68, 135);
	});
	public static final RegistryObject<BackpackExtension> FURNACE = BACKPACK_EXTENSIONS.register("furnace", () -> {
		String component = "xbackpack.backpack_extension.furnace.";
		return new BackpackExtension(Component.translatable(component + "title"), Component.translatable(component + "tooltip"), new ItemStack(Items.FURNACE), 20, 22, 86, 126);
	});
	public static final RegistryObject<BackpackExtension> ANVIL = BACKPACK_EXTENSIONS.register("anvil", () -> {
		String component = "xbackpack.backpack_extension.anvil.";
		return new BackpackExtension(Component.translatable(component + "title"), Component.translatable(component + "tooltip"), new ItemStack(Items.ANVIL), 20, 22, 111, 46);
	});
	public static final RegistryObject<BackpackExtension> ENCHANTING_TABLE = BACKPACK_EXTENSIONS.register("enchanting_table", () -> {
		String component = "xbackpack.backpack_extension.enchanting_table.";
		return new BackpackExtension(Component.translatable(component + "title"), Component.translatable(component + "tooltip"), new ItemStack(Items.ENCHANTING_TABLE), 20, 22, 136, 88);
	});
	public static final RegistryObject<BackpackExtension> STONECUTTER = BACKPACK_EXTENSIONS.register("stonecutter", () -> {
		String component = "xbackpack.backpack_extension.stonecutter.";
		return new BackpackExtension(Component.translatable(component + "title"), Component.translatable(component + "tooltip"), new ItemStack(Items.STONECUTTER), 20, 22, 95, 136);
	});
	public static final RegistryObject<BackpackExtension> BREWING_STAND = BACKPACK_EXTENSIONS.register("brewing_stand", () -> {
		String component = "xbackpack.backpack_extension.brewing_stand.";
		return new BackpackExtension(Component.translatable(component + "title"), Component.translatable(component + "tooltip"), new ItemStack(Items.BREWING_STAND), 20, 22, 106, 88);
	});
	public static final RegistryObject<BackpackExtension> SMITHING_TABLE = BACKPACK_EXTENSIONS.register("smithing_table", () -> {
		String component = "xbackpack.backpack_extension.smithing_table.";
		return new BackpackExtension(Component.translatable(component + "title"), Component.translatable(component + "tooltip"), new ItemStack(Items.SMITHING_TABLE), 20, 22, 111, 46);
	});
	public static final RegistryObject<BackpackExtension> GRINDSTONE = BACKPACK_EXTENSIONS.register("grindstone", () -> {
		String component = "xbackpack.backpack_extension.grindstone.";
		return new BackpackExtension(Component.translatable(component + "title"), Component.translatable(component + "tooltip"), new ItemStack(Items.GRINDSTONE), 20, 22, 112, 84);
	});
	
}
