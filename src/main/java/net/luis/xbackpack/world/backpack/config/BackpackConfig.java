package net.luis.xbackpack.world.backpack.config;

import net.luis.xbackpack.XBackpack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 *
 * @author Luis-st
 *
 */

public class BackpackConfig {
	
	private final Player player;
	private final BackpackExtensionConfig extensionConfig;
	private boolean showModifierInfo = true;
	
	public BackpackConfig(Player player) {
		this.player = player;
		this.extensionConfig = new BackpackExtensionConfig();
	}
	
	public BackpackExtensionConfig getExtensionConfig() {
		return this.extensionConfig;
	}
	
	public boolean shouldShowModifierInfo() {
		return this.showModifierInfo;
	}
	
	public void setShowModifierInfo(boolean showModifierInfo) {
		this.showModifierInfo = showModifierInfo;
	}
	
	public void updateServer() {
		if (this.player instanceof ServerPlayer player) {
			this.extensionConfig.update(player);
		} else {
			XBackpack.LOGGER.warn("The backpack config cannot be updated from the client");
		}
	}
	
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.put("extension_config", this.extensionConfig.serialize());
		tag.putBoolean("show_modifier_info", this.showModifierInfo);
		return tag;
	}
	
	public void deserialize(CompoundTag tag) {
		this.extensionConfig.deserialize(tag.getCompound("extension_config"));
		this.showModifierInfo = tag.contains("show_modifier_info") ? tag.getBoolean("show_modifier_info") : true;
	}
	
}
