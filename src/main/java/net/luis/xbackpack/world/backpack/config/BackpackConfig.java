package net.luis.xbackpack.world.backpack.config;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.backpack.config.client.BackpackClientConfig;
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
	private final boolean isClientSide;
	private final BackpackClientConfig clientConfig;
	private final BackpackExtensionConfig extensionConfig;
	
	public BackpackConfig(Player player) {
		this.player = player;
		this.isClientSide = this.player.level().isClientSide;
		this.clientConfig = new BackpackClientConfig();
		this.extensionConfig = new BackpackExtensionConfig();
	}
	
	public BackpackClientConfig getClientConfig() {
		if (!this.isClientSide) {
			XBackpack.LOGGER.warn("The backpack client config is not accessible from the server");
			return null;
		}
		return this.clientConfig;
	}
	
	public BackpackExtensionConfig getExtensionConfig() {
		return this.extensionConfig;
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
		return tag;
	}
	
	public void deserialize(CompoundTag tag) {
		this.extensionConfig.deserialize(tag.getCompound("extension_config"));
	}
}
