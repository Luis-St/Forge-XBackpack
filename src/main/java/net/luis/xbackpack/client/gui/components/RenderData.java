package net.luis.xbackpack.client.gui.components;

import net.luis.xbackpack.util.QuadFunction;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 *
 * @author Luis-st
 *
 */

public record RenderData(boolean exists, int xPosition, int yPosition, int width, int height) {
	
	@Nullable
	public <T extends AbstractWidget> T addIfExists(QuadFunction<Integer, Integer, Integer, Integer, T> function, Consumer<T> action) {
		if (this.exists) {
			T renderable = function.apply(this.xPosition, this.yPosition, this.width, this.height);
			action.accept(renderable);
			return renderable;
		}
		return null;
	}
}
