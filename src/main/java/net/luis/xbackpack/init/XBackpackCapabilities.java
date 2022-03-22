package net.luis.xbackpack.init;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.common.capability.IBackpack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/**
 * registration class, for the {@link XBackpack} Capability
 * 
 * @author Luis-st
 */

public class XBackpackCapabilities {
	
	/**
	 * {@link IBackpack} Capability
	 */
	public static final Capability<IBackpack> BACKPACK = CapabilityManager.get(new CapabilityToken<IBackpack>() {});
	
}
