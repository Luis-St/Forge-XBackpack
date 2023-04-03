package net.luis.xbackpack.world.inventory.modifier;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *
 * @author Luis-st
 *
 */

public interface ItemModifier {
	
	String getName();
	
	int getId();
	
	ItemModifierType getType();
	
	boolean isSelectable();
	
	default List<Component> getTooltip(@NotNull TooltipFlag tooltipFlag) {
		if (tooltipFlag.isAdvanced()) {
			List<MutableComponent> info = this.getInfo();
			if (!info.isEmpty()) {
				List<Component> tooltip = Lists.newArrayList();
				tooltip.add(this.getDisplayName().withStyle(ChatFormatting.WHITE));
				tooltip.addAll(info.stream().map((component) -> component.withStyle(ChatFormatting.GRAY)).toList());
				return tooltip;
			}
		}
		return Lists.newArrayList(this.getDisplayName().withStyle(ChatFormatting.WHITE));
	}
	
	MutableComponent getDisplayName();
	
	List<MutableComponent> getInfo();
	
}
