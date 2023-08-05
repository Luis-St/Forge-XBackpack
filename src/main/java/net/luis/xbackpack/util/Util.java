package net.luis.xbackpack.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Luis-St
 *
 */

public class Util {
	
	public static int tryParseInteger(String value, int fallback) {
		int number = fallback;
		try {
			char firstChar = value.charAt(0);
			char lastChar = value.charAt(value.length() - 1);
			if (firstChar == '=' || firstChar == '<' || firstChar == '>') {
				value = value.substring(1);
			} else if (lastChar == '<' || lastChar == '>') {
				value = value.substring(0, value.length() - 1);
			}
			number = Integer.parseInt(value);
		} catch (Exception ignored) {
			
		}
		return number;
	}
	
	public static <T extends Enum<T>> T tryParseEnum(String value, T[] enumValues) {
		return tryParseEnum(value, enumValues, null);
	}
	
	public static <T extends Enum<T>> T tryParseEnum(String value, T @NotNull [] enumValues, @Nullable T fallback) {
		for (T enumValue : enumValues) {
			if (enumValue.name().toLowerCase().equals(value)) {
				return enumValue;
			}
		}
		return fallback;
	}
}
