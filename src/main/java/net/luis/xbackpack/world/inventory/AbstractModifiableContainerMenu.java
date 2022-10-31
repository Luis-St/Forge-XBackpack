package net.luis.xbackpack.world.inventory;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.network.packet.modifier.UpdateBackpackItemModifiers;
import net.luis.xbackpack.util.Util;
import net.luis.xbackpack.world.inventory.modifier.ModifiableMenu;
import net.luis.xbackpack.world.inventory.modifier.filter.ItemFilter;
import net.luis.xbackpack.world.inventory.modifier.filter.ItemFilters;
import net.luis.xbackpack.world.inventory.modifier.sorter.ItemSorter;
import net.luis.xbackpack.world.inventory.modifier.sorter.ItemSorters;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

/**
 *
 * @author Luis-st
 *
 */

public abstract class AbstractModifiableContainerMenu extends AbstractExtensionContainerMenu implements ModifiableMenu {
	
	private final Player player;
	private ItemFilter filter = ItemFilters.NONE;
	private ItemSorter sorter = ItemSorters.NONE;
	private String searchTerm = "";
	
	protected AbstractModifiableContainerMenu(MenuType<?> menuType, int id, Inventory inventory) {
		super(menuType, id, inventory);
		this.player = inventory.player;
	}
	
	@Override
	public String getSearchTerm() {
		return StringUtils.trimToEmpty(this.searchTerm).toLowerCase();
	}
	
	@Override
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = StringUtils.trimToEmpty(searchTerm).toLowerCase();
		if (this.searchTerm.isEmpty() && !this.sorter.isSelectable()) {
			this.sorter = ItemSorters.NONE;
		} else if (!this.searchTerm.isEmpty()) {
			if (this.isNumber(this.searchTerm) && this.sorter != ItemSorters.COUNT_SEARCH) {
				this.sorter = ItemSorters.COUNT_SEARCH;
			} else if (!this.isNumber(this.searchTerm) && this.sorter != ItemSorters.NAME_SEARCH) {
				this.sorter = ItemSorters.NAME_SEARCH;
			}
		}
		this.updateItemModifiers();
	}
	
	private boolean isNumber(String searchTerm) {
		return Util.tryParseInteger(searchTerm, -1) >= 0;
	}
	
	@Override
	public ItemFilter getFilter() {
		return this.filter;
	}
	
	@Override
	public void updateFilter(@Nullable ItemFilter filter, UpdateType type, @Nullable CycleDirection direction) {
		boolean changed = false;
		if (type.shouldSet()) {
			if (this.filter != filter) {
				this.filter = filter;
				changed = true;
			}
		} else if (type.shouldReset()) {
			if (this.filter != ItemFilters.NONE) {
				this.filter = ItemFilters.NONE;
				changed = true;
			}
		} else if (type.sholdCycle()) {
			List<ItemFilter> filters = Lists.newArrayList(ItemFilters.values()).stream().filter(ItemFilter::isSelectable).collect(Collectors.toList());
			if (this.filter.isSelectable()) {
				this.filter = direction.cycle(filters, this.filter);
				changed = true;
			}
		}
		if (type.shouldUpdate() && changed) {
			this.updateItemModifiers();
		}
	}
	
	@Override
	public ItemSorter getSorter() {
		return this.sorter;
	}
	
	@Override
	public void updateSorter(@Nullable ItemSorter sorter, UpdateType type, @Nullable CycleDirection direction) {
		boolean changed = false;
		if (type.shouldSet()) {
			if (this.sorter != sorter) {
				this.sorter = sorter;
				changed = true;
			}
		} else if (type.shouldReset()) {
			if (this.sorter != ItemSorters.NONE) {
				this.sorter = ItemSorters.NONE;
				changed = true;
			}
		} else if (type.sholdCycle()) {
			List<ItemSorter> sorters = Lists.newArrayList(ItemSorters.values()).stream().filter(ItemSorter::isSelectable).collect(Collectors.toList());
			if (this.sorter.isSelectable()) {
				this.sorter = direction.cycle(sorters, this.sorter);
				changed = true;
			}
		}
		if (type.shouldUpdate() && changed) {
			this.updateItemModifiers();
		}
	}
	
	private void updateItemModifiers() {
		if (this.player instanceof ServerPlayer player) {
			XBNetworkHandler.sendToPlayer(player, new UpdateBackpackItemModifiers(this.filter, this.sorter));
			this.onItemModifiersChanged(player);
		}
	}
	
	protected abstract void onItemModifiersChanged(ServerPlayer player);
	
}
