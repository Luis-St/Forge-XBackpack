package net.luis.xbackpack.world.inventory.handler;

import com.google.common.collect.Lists;
import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.inventory.slot.SlotWrapper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

/**
 *
 * @author Luis-st
 *
 */

public class ModifiableHandler implements IItemHandlerModifiable {
	
	private final ItemStackHandler mainHandler;
	private final List<SlotWrapper> slotWrappers;
	
	public ModifiableHandler(int size) {
		this(new ItemStackHandler(size));
	}
	
	public ModifiableHandler(ItemStackHandler mainHandler) {
		this.mainHandler = mainHandler;
		this.slotWrappers = Lists.newArrayList();
		this.initSlotWrappers(mainHandler.getSlots(), SlotWrapper::ofUnwrapped);
	}
	
	private void validateSlotIndex(int slot) {
		if (slot >= this.mainHandler.getSlots() || 0 > slot) {
			throw new RuntimeException("Slot " + slot + " is not in valid range - [0," + this.mainHandler.getSlots() + ")");
		}
	}
	
	private boolean isCurrentlyModified() {
		for (SlotWrapper slotWrapper : this.slotWrappers) {
			if (slotWrapper.getMainSlot() != slotWrapper.getSlot()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getSlots() {
		return this.mainHandler.getSlots();
	}
	
	@Override
	public @NotNull ItemStack getStackInSlot(int slot) {
		int wrappedSlot = this.getWrappedSlot(slot);
		if (wrappedSlot != -1) {
			return this.mainHandler.getStackInSlot(wrappedSlot);
		}
		return ItemStack.EMPTY;
	}
	
	public ItemStack insertItem(ItemStack stack, boolean simulate) {
		for (int i = 0; i < this.getSlots() && !stack.isEmpty(); i++) {
			stack = this.insertItem(i, stack, simulate);
		}
		return stack.isEmpty() ? ItemStack.EMPTY : stack;
	}
	
	@Override
	public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
		int wrappedSlot = this.getWrappedSlot(slot);
		if (wrappedSlot != -1) {
			return this.mainHandler.insertItem(wrappedSlot, stack, simulate);
		}
		return stack;
	}
	
	@Override
	public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
		int wrappedSlot = this.getWrappedSlot(slot);
		if (wrappedSlot != -1) {
			return this.mainHandler.extractItem(wrappedSlot, amount, simulate);
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public int getSlotLimit(int slot) {
		int wrappedSlot = this.getWrappedSlot(slot);
		if (wrappedSlot != -1) {
			return this.mainHandler.getSlotLimit(wrappedSlot);
		}
		return 0;
	}
	
	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack) {
		int wrappedSlot = this.getWrappedSlot(slot);
		if (wrappedSlot != -1) {
			if (this.mainHandler.getStackInSlot(wrappedSlot).isEmpty() && (wrappedSlot != slot || this.isCurrentlyModified())) {
				return false;
			}
			return this.mainHandler.isItemValid(wrappedSlot, stack);
		}
		return false;
	}
	
	@Override
	public void setStackInSlot(int slot, @NotNull ItemStack stack) {
		int wrappedSlot = this.getWrappedSlot(slot);
		if (wrappedSlot != -1) {
			this.mainHandler.setStackInSlot(wrappedSlot, stack);
		}
	}
	
	private void initSlotWrappers(int size, Function<Integer, SlotWrapper> function) {
		for (int i = 0; i < size; i++) {
			this.slotWrappers.add(function.apply(i));
		}
	}
	
	private SlotWrapper getSlotWrapper(int mainSlot) {
		this.validateSlotIndex(mainSlot);
		SlotWrapper wrapper = this.slotWrappers.get(mainSlot);
		if (wrapper.getMainSlot() == mainSlot) {
			return wrapper;
		}
		return this.slotWrappers.stream().filter((slotWrapper) -> slotWrapper.getMainSlot() == mainSlot).findFirst().orElseThrow();
	}
	
	public int getWrappedSlot(int mainSlot) {
		return this.getSlotWrapper(mainSlot).getSlot();
	}
	
	public void setWrappedSlot(int mainSlot, int slot) {
		this.validateSlotIndex(mainSlot);
		this.validateSlotIndex(slot);
		this.getSlotWrapper(mainSlot).setSlot(slot);
	}
	
	public void resetWrappedSlots() {
		this.resetWrappedSlots(SlotWrapper::ofUnwrapped);
	}
	
	public void resetWrappedSlots(Function<Integer, SlotWrapper> function) {
		int size = this.slotWrappers.size();
		this.slotWrappers.clear();
		this.initSlotWrappers(size, function);
	}
	
	public List<ItemStack> createModifiableList() {
		List<ItemStack> stacks = Lists.newArrayList();
		for (int i = 0; i < this.mainHandler.getSlots(); i++) {
			ItemStack stack = this.mainHandler.getStackInSlot(i).copy();
			if (!stack.isEmpty()) {
				stack.getOrCreateTagElement(XBackpack.MOD_NAME + "ItemModifierInformation").putInt("SlotIndex", i);
				stacks.add(stack);
			}
		}
		return stacks;
	}
	
	public void applyModifications(List<ItemStack> stacks) {
		this.resetWrappedSlots(SlotWrapper::ofDisabled);
		for (int i = 0; i < stacks.size(); i++) {
			CompoundTag tag = stacks.get(i).getTagElement(XBackpack.MOD_NAME + "ItemModifierInformation");
			if (tag != null && tag.contains("SlotIndex", Tag.TAG_INT)) {
				this.setWrappedSlot(i, tag.getInt("SlotIndex"));
			}
		}
	}
	
}
