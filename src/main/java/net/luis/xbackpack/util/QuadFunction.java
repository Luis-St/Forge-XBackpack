package net.luis.xbackpack.util;

import java.util.Objects;
import java.util.function.Function;

/**
 *
 * @author Luis-st
 *
 */

@FunctionalInterface
public interface QuadFunction<T, U, V, W, R> {
	
	R apply(T t, U u, V v, W w);
	
	default <S> QuadFunction<T, U, V, W, S> andThen(Function<? super R, ? extends S> after) {
		Objects.requireNonNull(after);
		return (T t, U u, V v, W w) -> after.apply(this.apply(t, u, v, w));
	}
}
