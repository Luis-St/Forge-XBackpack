package net.luis.xbackpack.common.capability;

import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class BackpackHandler extends ItemStackHandler implements IBackpack {

	// package private, because a new instance should only be created in BackpackProvider
	BackpackHandler(int size) {
		super(size);
	}

}
