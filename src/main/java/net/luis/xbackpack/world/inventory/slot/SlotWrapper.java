package net.luis.xbackpack.world.inventory.slot;

import net.minecraft.network.FriendlyByteBuf;

/**
 *
 * @author Luis-st
 *
 */

public class SlotWrapper {
	
	private final int mainSlot;
	private int slot;
	
	private SlotWrapper(int mainSlot, int slot) {
		this.mainSlot = mainSlot;
		this.slot = slot;
	}
	
	public static SlotWrapper of(int mainSlot, int slot) {
		return new SlotWrapper(mainSlot, slot);
	}
	
	public static SlotWrapper of(FriendlyByteBuf buffer) {
		int mainSlot = buffer.readInt();
		int slot = buffer.readInt();
		return new SlotWrapper(mainSlot, slot);
	}
	
	public static SlotWrapper ofUnwrapped(int mainSlot) {
		return of(mainSlot, mainSlot);
	}
	
	public static SlotWrapper ofDisabled(int mainSlot) {
		return of(mainSlot, -1);
	}
	
	public int getMainSlot() {
		return this.mainSlot;
	}
	
	public int getSlot() {
		return this.slot;
	}
	
	public void setSlot(int slot) {
		this.slot = slot;
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.mainSlot);
		buffer.writeInt(this.slot);
	}
	
}
