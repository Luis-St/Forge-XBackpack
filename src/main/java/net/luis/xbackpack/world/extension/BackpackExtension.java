/*
 * XBackpack
 * Copyright (C) 2024 Luis Staudt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.luis.xbackpack.world.extension;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

/**
 *
 * @author Luis-St
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
