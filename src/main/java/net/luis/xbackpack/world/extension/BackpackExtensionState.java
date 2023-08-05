package net.luis.xbackpack.world.extension;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 *
 * @author Luis-St
 *
 */

public enum BackpackExtensionState {
	
	BLOCKED("blocked"),
	UNLOCKED("unlocked"),
	LOCKED("locked");
	
	private final String name;
	
	BackpackExtensionState(String name) {
		this.name = name;
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
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
