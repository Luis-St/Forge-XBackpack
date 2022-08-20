package net.luis.xbackpack.world.extension;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-st
 *
 */

public enum ExtensionState {
	
	BLOCKED("blocked"),
	UNLOCKED("unlocked"),
	LOCKED("locked");
	
	private final String name;

	private ExtensionState(String name) {
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
	public static ExtensionState fromString(String string) {
		for (ExtensionState state : values()) {
			if (state.getName().equals(string)) {
				return state;
			}
		}
		return null;
	}
	
	@NotNull
	public static ExtensionState fromString(String string, ExtensionState fallbackState) {
		ExtensionState state = fromString(string);
		return state != null ? state : fallbackState; 
	}
	
}
