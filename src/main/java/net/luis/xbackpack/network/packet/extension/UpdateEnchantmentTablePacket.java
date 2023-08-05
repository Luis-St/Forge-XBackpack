package net.luis.xbackpack.network.packet.extension;

import net.luis.xbackpack.client.XBClientPacketHandler;
import net.luis.xbackpack.network.NetworkPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 *
 * @author Luis-St
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
	
	public UpdateEnchantmentTablePacket(@NotNull FriendlyByteBuf buffer) {
		this.enchantments = new ResourceLocation[buffer.readInt()];
		for (int i = 0; i < this.enchantments.length; i++) {
			this.enchantments[i] = buffer.readResourceLocation();
		}
		this.enchantmentLevels = buffer.readVarIntArray();
		this.enchantingCosts = buffer.readVarIntArray();
		this.enchantmentSeed = buffer.readInt();
	}
	
	@Override
	public void encode(@NotNull FriendlyByteBuf buffer) {
		buffer.writeInt(this.enchantments.length);
		for (ResourceLocation enchantment : this.enchantments) {
			buffer.writeResourceLocation(enchantment);
		}
		buffer.writeVarIntArray(this.enchantmentLevels);
		buffer.writeVarIntArray(this.enchantingCosts);
		buffer.writeInt(this.enchantmentSeed);
	}
	
	@Override
	public void handle(@NotNull Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				XBClientPacketHandler.updateEnchantmentTableExtension(this.enchantments, this.enchantmentLevels, this.enchantingCosts, this.enchantmentSeed);
			});
		});
	}
}
