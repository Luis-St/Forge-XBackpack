package net.luis.xbackpack.world.backpack.config.client;

/**
 *
 * @author Luis-st
 *
 */

public class BackpackClientConfig {
	
	private boolean showModifierInfo = false;
	
	public BackpackClientConfig() {
		
	}
	
	public boolean shouldShowModifierInfo() {
		return this.showModifierInfo;
	}
	
	public void setShowModifierInfo(boolean showModifierInfo) {
		this.showModifierInfo = showModifierInfo;
	}
	
}
