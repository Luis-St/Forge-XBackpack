package net.luis.xbackpack.world.backpack.config;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.extension.BackpackExtensionState;
import net.luis.xbackpack.world.extension.BackpackExtensions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;

/**
 *
 * @author Luis-st
 *
 */

public class BackpackExtensionConfig {
	
	private final Map<BackpackExtension, Data> states = Maps.newHashMap();
	
	public BackpackExtensionConfig() {
		for (BackpackExtension extension : BackpackExtensions.REGISTRY.get().getValues()) {
			this.states.put(extension, new Data(BackpackExtensionState.LOCKED, 0));
		}
	}
	
	private Data getData(BackpackExtension extension) {
		return this.states.getOrDefault(extension, new Data(BackpackExtensionState.LOCKED, 0));
	}
	
	public BackpackExtensionState getState(BackpackExtension extension) {
		return this.getData(extension).state();
	}
	
	public List<BackpackExtension> getWithState(BackpackExtensionState state) {
		return this.states.entrySet().stream().filter((entry) -> {
			return entry.getValue().state() == state;
		}).map(Entry::getKey).collect(Collectors.toList());
	}
	
	public void setState(ServerPlayer player, BackpackExtension extension, BackpackExtensionState state) {
		this.states.put(extension, new Data(state, player.getStats().getValue(Stats.ITEM_CRAFTED, extension.getUnlockItem().getItem())));
	}
	
	public void update(ServerPlayer player) {
		for (BackpackExtension extension : BackpackExtensions.REGISTRY.get().getValues()) {
			Data data = this.getData(extension);
			BackpackExtensionState state = data.state();
			if (state != BackpackExtensionState.BLOCKED) {
				int count = player.getStats().getValue(Stats.ITEM_CRAFTED, extension.getUnlockItem().getItem());
				if (0 >= data.unlockCount() && count > 0) {
					this.setState(player, extension, BackpackExtensionState.UNLOCKED);
				} else if (data.unlockCount() > 0 && count > data.unlockCount()) {
					this.setState(player, extension, BackpackExtensionState.UNLOCKED);
				}
			}
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
			BackpackExtensionState state = BackpackExtensionState.fromString(stateTag.getString("value"), BackpackExtensionState.LOCKED);
			int unlockCount = stateTag.getInt("unlock_count");
			if (extension != null) {
				this.states.put(extension, new Data(state, unlockCount));
			}
		}
	}
	
	private static record Data(BackpackExtensionState state, int unlockCount) {
		
	}
	
}
