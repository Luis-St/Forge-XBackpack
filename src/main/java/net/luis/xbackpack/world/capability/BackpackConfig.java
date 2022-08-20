package net.luis.xbackpack.world.capability;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.luis.xbackpack.world.extension.ExtensionState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;

/**
 *
 * @author Luis-st
 *
 */

public class BackpackConfig {
	
	private final Player player;
	private final Map<BackpackExtension, Data> states = Maps.newHashMap();
	
	public BackpackConfig(Player player) {
		this.player = player;
		for (BackpackExtension extension : BackpackExtensions.REGISTRY.get().getValues()) {
			this.states.put(extension, new Data(ExtensionState.LOCKED, 0));
		}
	}
	
	private Data getData(BackpackExtension extension) {
		return this.states.getOrDefault(extension, new Data(ExtensionState.LOCKED, 0));
	}
	
	public ExtensionState getState(BackpackExtension extension) {
		return this.getData(extension).state();
	}
	
	public List<BackpackExtension> getWithState(ExtensionState state) {
		return this.states.entrySet().stream().filter((entry) -> {
			return entry.getValue().state() == state;
		}).map(Entry::getKey).collect(Collectors.toList());
	}
	
	public boolean setState(BackpackExtension extension, ExtensionState state) {
		if (this.player instanceof ServerPlayer player) {
			this.states.put(extension, new Data(state, player.getStats().getValue(Stats.ITEM_CRAFTED, extension.getUnlockItem().getItem())));
			return true;
		} else {
			XBackpack.LOGGER.warn("Can not set the state of a BackpackExtension from the client");
			return false;
		}
	}
	
	public void updateServer() {
		if (this.player instanceof ServerPlayer player) {
			for (BackpackExtension extension : BackpackExtensions.REGISTRY.get().getValues()) {
				Data data = this.getData(extension);
				ExtensionState state = data.state();
				if (state != ExtensionState.BLOCKED) {
					int count = player.getStats().getValue(Stats.ITEM_CRAFTED, extension.getUnlockItem().getItem());
					if (0 >= data.unlockCount() && count > 0) {
						this.setState(extension, ExtensionState.UNLOCKED);
					} else if (data.unlockCount() > 0 && count > data.unlockCount()) {
						this.setState(extension, ExtensionState.UNLOCKED);
					}
				}
			}
		} else {
			XBackpack.LOGGER.warn("Can not update the BackpackConfig from the client");
		}
	}
	
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		ListTag statesTag = new ListTag();
		for (Entry<BackpackExtension, Data> entry : this.states.entrySet()) {
			CompoundTag stateTag = new CompoundTag();
			stateTag.putString("key", BackpackExtensions.REGISTRY.get().getKey(entry.getKey()).toString());
			stateTag.putString("value", entry.getValue().state().getName());
			stateTag.putInt("unlock_count", entry.getValue().unlockCount());
			statesTag.add(stateTag);
		}
		tag.put("states", statesTag);
		return tag;
	}
	
	public void deserialize(CompoundTag tag) {
		ListTag statesTag = tag.getList("states", Tag.TAG_COMPOUND);
		for (int i = 0; i < statesTag.size(); i++) {
			CompoundTag stateTag = statesTag.getCompound(i);
			BackpackExtension extension = BackpackExtensions.REGISTRY.get().getValue(ResourceLocation.tryParse(stateTag.getString("key")));
			ExtensionState state = ExtensionState.fromString(stateTag.getString("value"), ExtensionState.LOCKED);
			int unlockCount = stateTag.getInt("unlock_count");
			if (extension != null) {
				this.states.put(extension, new Data(state, unlockCount));
			}
		}
	}
	
	public static record Data(ExtensionState state, int unlockCount) {
		
	}
	
}
