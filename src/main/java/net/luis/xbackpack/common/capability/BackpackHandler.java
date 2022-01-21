package net.luis.xbackpack.common.capability;

import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackHandler extends ItemStackHandler implements IBackpack {

	/**
	 * constructor is package private,<br> 
	 * because a new instance should only be created in {@link BackpackProvider}
	 */
	BackpackHandler(int size) {
		super(size);
	}

}
