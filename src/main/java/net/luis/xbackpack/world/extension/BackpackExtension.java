package net.luis.xbackpack.world.extension;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackExtension {
	
	private final ItemStack unlockItem;
	private final ItemStack icon;
	private final int iconWidth;
	private final int iconHeight;
	private final int imageWidth;
	private final int imageHeight;
	
	public BackpackExtension(ItemStack stack, int iconWidth, int iconHeight, int imageWidth, int imageHeight) {
		this(stack, stack, iconWidth, iconHeight, imageWidth, imageHeight);
	}
	
	public BackpackExtension(ItemStack unlockItem, ItemStack icon, int iconWidth, int iconHeight, int imageWidth, int imageHeight) {
		this.unlockItem = unlockItem;
		this.icon = icon;
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
	}

	public ItemStack getUnlockItem() {
		return this.unlockItem;
	}

	public ItemStack getIcon() {
		return this.icon;
	}

	public Component getTitle() {
		ResourceLocation location = BackpackExtensions.REGISTRY.get().getKey(this);
		return Component.translatable(location.getNamespace() + ".backpack_extension." + location.getPath() + ".title");
	}

	public Component getTooltip() {
		ResourceLocation location = BackpackExtensions.REGISTRY.get().getKey(this);
		return Component.translatable(location.getNamespace() + ".backpack_extension." + location.getPath() + ".tooltip");
	}

	public int getIconWidth() {
		return this.iconWidth;
	}

	public int getIconHeight() {
		return this.iconHeight;
	}

	public int getImageWidth() {
		return this.imageWidth;
	}

	public int getImageHeight() {
		return this.imageHeight;
	}
	
	@Override
	public String toString() {
		return BackpackExtensions.REGISTRY.get().getKey(this).toString();
	}
	
}
