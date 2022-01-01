package net.luis.xbackpack.init;

import net.luis.xbackpack.XBackpack;
import net.minecraft.client.KeyMapping;

/**
 * 
 * @author Luis-st
 *
 */

//Registration class of the KeyMappings
public class XBackpackKeyMappings {
	
	public static final String BACKPACK_CATEGORY = XBackpack.MOD_ID + ".key.categories.backpack";
	public static final KeyMapping BACKPACK_OPEN = new KeyMapping(XBackpack.MOD_ID + ".key.backpack_open", 66, BACKPACK_CATEGORY);
	public static final KeyMapping BACKPACK_NEXT = new KeyMapping(XBackpack.MOD_ID + ".key.backpack_next", 71, BACKPACK_CATEGORY);
	public static final KeyMapping BACKPACK_SLOT_TOP = new KeyMapping(XBackpack.MOD_ID + ".key.backpack_slot_top", 86, BACKPACK_CATEGORY);
	public static final KeyMapping BACKPACK_SLOT_DOWN = new KeyMapping(XBackpack.MOD_ID + ".key.backpack_slot_down", 78, BACKPACK_CATEGORY);
	
}
