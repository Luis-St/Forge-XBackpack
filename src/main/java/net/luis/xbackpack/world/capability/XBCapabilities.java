package net.luis.xbackpack.world.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/**
 * 
 * @author Luis-st
 * 
 */

public class XBCapabilities {
	
	public static final Capability<IBackpack> BACKPACK = CapabilityManager.get(new CapabilityToken<IBackpack>() {});
	
}
