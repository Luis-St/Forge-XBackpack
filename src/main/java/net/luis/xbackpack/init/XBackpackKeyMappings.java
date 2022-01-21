package net.luis.xbackpack.init;

import org.lwjgl.glfw.GLFW;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.common.inventory.slot.BackpackToolSlot;
import net.minecraft.client.KeyMapping;

/**
 * registration class, for the {@link XBackpack} {@link KeyMapping}s
 * 
 * @author Luis-st
 */

public class XBackpackKeyMappings {
	
	/**
	 * {@link XBackpack} key category
	 */
	public static final String BACKPACK_CATEGORY = XBackpack.MOD_ID + ".key.categories.backpack";
	
	/**
	 * {@link KeyMapping} for opening the backpack
	 * @default {@link GLFW#GLFW_KEY_B}
	 */
	public static final KeyMapping BACKPACK_OPEN = new KeyMapping(XBackpack.MOD_ID + ".key.backpack_open", 66, BACKPACK_CATEGORY);
	
	/**
	 * {@link KeyMapping} for cycling through the {@link BackpackToolSlot}
	 * @default {@link GLFW#GLFW_KEY_G}
	 */
	public static final KeyMapping BACKPACK_NEXT = new KeyMapping(XBackpack.MOD_ID + ".key.backpack_next", 71, BACKPACK_CATEGORY);
	
	/**
	 * {@link KeyMapping} for getting the tool from the upper {@link BackpackToolSlot}
	 * @default {@link GLFW#GLFW_KEY_V}
	 */
	public static final KeyMapping BACKPACK_SLOT_TOP = new KeyMapping(XBackpack.MOD_ID + ".key.backpack_slot_top", 86, BACKPACK_CATEGORY);
	
	/**
	 * {@link KeyMapping} for getting the tool from the lower {@link BackpackToolSlot}
	 * @default {@link GLFW#GLFW_KEY_N}
	 */
	public static final KeyMapping BACKPACK_SLOT_DOWN = new KeyMapping(XBackpack.MOD_ID + ".key.backpack_slot_down", 78, BACKPACK_CATEGORY);
	
}
