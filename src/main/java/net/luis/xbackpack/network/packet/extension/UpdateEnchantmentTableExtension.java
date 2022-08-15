package net.luis.xbackpack.network.packet.extension;

import java.util.function.Supplier;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

/**
 * 
 * @author Luis-st
 *
 */

public class UpdateEnchantmentTableExtension {
	
	private final ResourceLocation[] enchantments;
	private final int[] enchantmentLevels;
	private final int[] enchantingCosts;
	private final int enchantmentSeed;
	
	public UpdateEnchantmentTableExtension(ResourceLocation[] enchantments, int[] enchantmentLevels, int[] enchantingCosts, int enchantmentSeed) {
		this.enchantments = enchantments;
		this.enchantmentLevels = enchantmentLevels;
		this.enchantingCosts = enchantingCosts;
		this.enchantmentSeed = enchantmentSeed;
	}
	
	public UpdateEnchantmentTableExtension(FriendlyByteBuf buffer) {
		this.enchantments = new ResourceLocation[buffer.readInt()];
		for (int i = 0; i < this.enchantments.length; i++) {
			this.enchantments[i] = buffer.readResourceLocation();
		}
		this.enchantmentLevels = buffer.readVarIntArray();
		this.enchantingCosts = buffer.readVarIntArray();
		this.enchantmentSeed = buffer.readInt();
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.enchantments.length);
		for (int i = 0; i < this.enchantments.length; i++) {
			buffer.writeResourceLocation(this.enchantments[i]);
		}
		buffer.writeVarIntArray(this.enchantmentLevels);
		buffer.writeVarIntArray(this.enchantingCosts);
		buffer.writeInt(this.enchantmentSeed);
	}
	
	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateEnchantmentTableExtension(this.enchantments, this.enchantmentLevels, this.enchantingCosts, this.enchantmentSeed);
			});
		});
	}
	
}
