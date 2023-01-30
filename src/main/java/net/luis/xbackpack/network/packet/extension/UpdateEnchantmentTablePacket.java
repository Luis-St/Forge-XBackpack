package net.luis.xbackpack.network.packet.extension;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.luis.xbackpack.network.NetworkPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

/**
 *
 * @author Luis-st
 *
 */

public class UpdateEnchantmentTablePacket implements NetworkPacket {
	
	private final ResourceLocation[] enchantments;
	private final int[] enchantmentLevels;
	private final int[] enchantingCosts;
	private final int enchantmentSeed;
	
	public UpdateEnchantmentTablePacket(ResourceLocation[] enchantments, int[] enchantmentLevels, int[] enchantingCosts, int enchantmentSeed) {
		this.enchantments = enchantments;
		this.enchantmentLevels = enchantmentLevels;
		this.enchantingCosts = enchantingCosts;
		this.enchantmentSeed = enchantmentSeed;
	}
	
	public UpdateEnchantmentTablePacket(FriendlyByteBuf buffer) {
		this.enchantments = new ResourceLocation[buffer.readInt()];
		for (int i = 0; i < this.enchantments.length; i++) {
			this.enchantments[i] = buffer.readResourceLocation();
		}
		this.enchantmentLevels = buffer.readVarIntArray();
		this.enchantingCosts = buffer.readVarIntArray();
		this.enchantmentSeed = buffer.readInt();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.enchantments.length);
		for (int i = 0; i < this.enchantments.length; i++) {
			buffer.writeResourceLocation(this.enchantments[i]);
		}
		buffer.writeVarIntArray(this.enchantmentLevels);
		buffer.writeVarIntArray(this.enchantingCosts);
		buffer.writeInt(this.enchantmentSeed);
	}
	
	@Override
	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateEnchantmentTableExtension(this.enchantments, this.enchantmentLevels, this.enchantingCosts, this.enchantmentSeed);
			});
		});
	}
	
}
