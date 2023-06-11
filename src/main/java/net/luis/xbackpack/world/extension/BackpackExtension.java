package net.luis.xbackpack.world.extension;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

/**
 *
 * @author Luis-st
 *
 */

public final class BackpackExtension {
	
	private final ItemStack unlockItem;
	private final ItemStack icon;
	private final int iconWidth;
	private final int iconHeight;
	private final int imageWidth;
	private final int imageHeight;
	private final boolean disabled;
	
	public BackpackExtension(ItemStack stack, int iconWidth, int iconHeight, int imageWidth, int imageHeight) {
		this(stack, stack, iconWidth, iconHeight, imageWidth, imageHeight, false);
	}
	
	public BackpackExtension(ItemStack unlockItem, ItemStack icon, int iconWidth, int iconHeight, int imageWidth, int imageHeight) {
		this(unlockItem, icon, iconWidth, iconHeight, imageWidth, imageHeight, false);
	}
	
	public BackpackExtension(ItemStack stack, int iconWidth, int iconHeight, int imageWidth, int imageHeight, boolean disabled) {
		this(stack, stack, iconWidth, iconHeight, imageWidth, imageHeight, disabled);
	}
	
	public BackpackExtension(ItemStack unlockItem, ItemStack icon, int iconWidth, int iconHeight, int imageWidth, int imageHeight, boolean disabled) {
		this.unlockItem = unlockItem;
		this.icon = icon;
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.disabled = disabled;
	}
	
	public ItemStack getUnlockItem() {
		return this.unlockItem;
	}
	
	public ItemStack getIcon() {
		return this.icon;
	}
	
	public Component getTitle() {
		ResourceLocation location = BackpackExtensions.REGISTRY.get().getKey(this);
		assert location != null;
		return Component.translatable(location.getNamespace() + ".backpack_extension." + location.getPath() + ".title");
	}
	
	public Component getTooltip() {
		ResourceLocation location = BackpackExtensions.REGISTRY.get().getKey(this);
		assert location != null;
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
	
	public boolean isDisabled() {
		return this.disabled;
	}
	
	@Override
	public String toString() {
		return Objects.requireNonNull(BackpackExtensions.REGISTRY.get().getKey(this)).toString();
	}
}
