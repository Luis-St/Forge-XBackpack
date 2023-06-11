package net.luis.xbackpack.util;

import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Luis-st
 *
 */

public class Util {
	
	public static int tryParseInteger(String value) {
		return tryParseInteger(value, 0);
	}
	
	public static int tryParseInteger(String value, int fallback) {
		int number = fallback;
		try {
			number = Integer.parseInt(value);
		} catch (Exception ignored) {
			
		}
		return number;
	}
	
	public static <T extends Enum<T>> T tryParseEnum(String value, T[] enumValues) {
		return tryParseEnum(value, enumValues, null);
	}
	
	public static <T extends Enum<T>> T tryParseEnum(String value, T[] enumValues, @Nullable T fallback) {
		for (T enumValue : enumValues) {
			if (enumValue.name().toLowerCase().equals(value)) {
				return enumValue;
			}
		}
		return fallback;
	}
}
