package net.luis.xbackpack.world.inventory;

import com.google.common.collect.Lists;
import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.inventory.extension.AbstractExtensionMenu;
import net.luis.xbackpack.world.inventory.extension.ExtensionMenuFactory;
import net.luis.xbackpack.world.inventory.extension.ExtensionMenuRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Luis-st
 *
 */

public abstract class AbstractExtensionContainerMenu extends AbstractContainerMenu {
	
	private final List<AbstractExtensionMenu> extensionMenus = Lists.newArrayList();
	private BackpackExtension extension = BackpackExtensions.NO.get();
	
	protected AbstractExtensionContainerMenu(MenuType<?> menuType, int id, Inventory inventory) {
		super(menuType, id);
	}
	
	@NotNull
	public BackpackExtension getExtension() {
		return this.extension;
	}
	
	public void setExtension(@NotNull BackpackExtension extension) {
		if (this.extension != extension) {
			this.getExtensionMenu().ifPresent(AbstractExtensionMenu::close);
		}
		this.extension = extension;
		this.getExtensionMenu().ifPresent(AbstractExtensionMenu::open);
	}
	
	public boolean moveItemStackTo(@NotNull ItemStack stack, int startIndex, int endIndex) {
		if (!stack.isStackable()) {
			for (int i = startIndex; i < endIndex; i++) {
				Slot slot = this.getSlot(i);
				if (!slot.mayPlace(stack)) {
					continue;
				}
				if (!slot.hasItem()) {
					slot.setByPlayer(stack.copy());
					stack.setCount(0);
					slot.setChanged();
					return true;
				}
			}
			return false;
		}
		for (int i = startIndex; i < endIndex; i++) {
			Slot slot = this.getSlot(i);
			if (!slot.mayPlace(stack)) {
				continue;
			}
			if (!slot.hasItem()) {
				slot.setByPlayer(stack.copy());
				stack.setCount(0);
				slot.setChanged();
				return true;
			}
			ItemStack slotStack = slot.getItem();
			if (slotStack.getCount() >= slotStack.getMaxStackSize() || !ItemStack.isSameItemSameTags(stack, slotStack)) {
				continue;
			}
			int count = slotStack.getCount() + stack.getCount();
			int maxSize = Math.min(stack.getMaxStackSize(), slot.getMaxStackSize());
			if (maxSize >= count) {
				slotStack.setCount(count);
				stack.setCount(0);
				slot.setChanged();
				return true;
			} else {
				slotStack.setCount(maxSize);
				stack.setCount(count - maxSize);
				slot.setChanged();
			}
		}
		return stack.isEmpty();
	}
	
	protected boolean moveExtension(ItemStack slotStack, int index) {
		AbstractExtensionMenu extensionMenu = this.getExtensionMenu(this.extension);
		return extensionMenu != null && extensionMenu.quickMoveStack(slotStack, index);
	}
	
	public void tick() {
		this.extensionMenus.stream().filter(AbstractExtensionMenu::requiresTickUpdate).forEach(AbstractExtensionMenu::slotsChanged);
	}
	
	@Override
	public void slotsChanged(@NotNull Container container) {
		super.slotsChanged(container);
		this.extensionMenus.forEach((extensionMenu) -> {
			extensionMenu.slotsChanged(container);
		});
	}
	
	@Override
	public boolean clickMenuButton(@NotNull Player player, int button) {
		AbstractExtensionMenu extensionMenu = this.getExtensionMenu(this.extension);
		if (extensionMenu != null) {
			return extensionMenu.clickMenuButton(player, button);
		}
		return super.clickMenuButton(player, button);
	}
	
	@NotNull
	public List<AbstractExtensionMenu> getExtensionMenus() {
		return this.extensionMenus;
	}
	
	protected void addExtensionMenu(BackpackExtension extension, Player player, ExtensionMenuFactory menuFactory) {
		AbstractExtensionMenu extensionMenu = ExtensionMenuRegistry.getExtensionMenu(extension, this, player, menuFactory);
		extensionMenu.addSlots(this::addSlot);
		this.extensionMenus.add(extensionMenu);
	}
	
	@Nullable
	public AbstractExtensionMenu getExtensionMenu(BackpackExtension extension) {
		return this.extensionMenus.stream().filter((extensionMenu) -> extensionMenu.getExtension() == extension).findAny().orElse(null);
	}
	
	@NotNull
	public Optional<AbstractExtensionMenu> getExtensionMenu() {
		return Optional.ofNullable(this.getExtensionMenu(this.getExtension()));
	}
	
}
