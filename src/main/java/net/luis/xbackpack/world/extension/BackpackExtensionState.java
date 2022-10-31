package net.luis.xbackpack.world.extension;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-st
 *
 */

public enum BackpackExtensionState {
	
	BLOCKED("blocked"),
	UNLOCKED("unlocked"),
	LOCKED("locked");
	
	private final String name;

	private BackpackExtensionState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Nullable
	public static BackpackExtensionState fromString(String string) {
		for (BackpackExtensionState state : values()) {
			if (state.getName().equals(string)) {
				return state;
			}
		}
		return null;
	}
	
	@NotNull
	public static BackpackExtensionState fromString(String string, BackpackExtensionState fallbackState) {
		BackpackExtensionState state = fromString(string);
		return state != null ? state : fallbackState; 
	}
	
}
