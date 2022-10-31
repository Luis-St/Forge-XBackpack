package net.luis.xbackpack.world.inventory.modifier;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.TooltipFlag;

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
	
	default List<Component> getTooltip(TooltipFlag tooltipFlag) {
		if (tooltipFlag.isAdvanced()) {
			List<MutableComponent> info = this.getInfo();
			if (!info.isEmpty()) {
				List<Component> tooltip = Lists.newArrayList();
				tooltip.add(this.getDisplayName().withStyle(ChatFormatting.WHITE));
				tooltip.addAll(info.stream().map((component) -> {
					return component.withStyle(ChatFormatting.GRAY);
				}).collect(Collectors.toList()));
				return tooltip;
			}
		}
		return Lists.newArrayList(this.getDisplayName().withStyle(ChatFormatting.WHITE));
	}
	
	MutableComponent getDisplayName();
	
	List<MutableComponent> getInfo();
	
}
