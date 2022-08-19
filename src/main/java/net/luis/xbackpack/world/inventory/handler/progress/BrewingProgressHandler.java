package net.luis.xbackpack.world.inventory.handler.progress;

import net.luis.xbackpack.network.XBNetworkHandler;
import net.luis.xbackpack.network.packet.extension.UpdateBrewingStandExtension;
import net.luis.xbackpack.world.inventory.handler.BrewingHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 
 * @author Luis-st
 *
 */

public class BrewingProgressHandler implements ProgressHandler {
	
	private final Player player;
	private final BrewingHandler handler;
	private Item input;
	private int fuel;
	private int brewTime;
	
	public BrewingProgressHandler(Player player, BrewingHandler handler) {
		this.player = player;
		this.handler = handler;
	}
	
	@Override
	public void tick() {
		ItemStack fuelStack = this.getFuelItem();
		if (0 >= this.fuel && fuelStack.is(Items.BLAZE_POWDER)) {
			this.fuel = 20;
			this.handler.getFuelHandler().extractItem(0, 1, false);
			this.broadcastChanges();
		}
		ItemStack inputStack = this.getInputItem();
		if (this.brewTime > 0) {
			--this.brewTime;
			if (this.brewTime == 0 && this.isBrewable()) {
				this.brewPotion();
			} else if (!this.isBrewable() || !inputStack.is(this.input)) {
				this.brewTime = 0;
			}
			this.broadcastChanges();
		} else if (this.isBrewable() && this.fuel > 0) {
			--this.fuel;
			this.brewTime = 400;
			this.input = inputStack.getItem();
			this.broadcastChanges();
		}
	}
	
	private boolean isBrewable() {
		ItemStack inputStack = this.getInputItem();
		if (!inputStack.isEmpty()) {
			for (int i = 0; i < 3; i++) {
				if (BrewingRecipeRegistry.hasOutput(this.getResultHandler().getStackInSlot(i), inputStack)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void brewPotion() {
		if (!this.onPotionAttemptBrew()) {
			ItemStack inputStack = this.getInputItem();
			for (int i = 0; i < 3; i++) {
				this.getResultHandler().setStackInSlot(i, BrewingRecipeRegistry.getOutput(this.getResultHandler().getStackInSlot(i), inputStack));
			}
			this.onPotionBrewed(this.asList());
			if (this.player instanceof ServerPlayer player) {
				this.playSound(player, player.getLevel());
			}
			if (inputStack.hasCraftingRemainingItem()) {
				ItemStack remainingStack = inputStack.getCraftingRemainingItem();
				inputStack.shrink(1);
				if (inputStack.isEmpty()) {
					inputStack = remainingStack;
				} else {
					Containers.dropItemStack(this.player.level, this.player.getX(), this.player.getY(), this.player.getZ(), remainingStack);
				}
			} else {
				inputStack.shrink(1);
			}
			this.handler.getInputHandler().setStackInSlot(0, inputStack);
		}
	}
	
	private ItemStack getInputItem() {
		return this.handler.getInputHandler().getStackInSlot(0);
	}
	
	private NonNullList<ItemStack> getInputList() {
		NonNullList<ItemStack> stacks = NonNullList.withSize(this.handler.getInputHandler().getSlots(), ItemStack.EMPTY);
		for (int i = 0; i < stacks.size(); i++) {
			stacks.set(i, this.handler.getInputHandler().getStackInSlot(i));
		}
		return stacks;
	}
	
	private ItemStack getFuelItem() {
		return this.handler.getFuelHandler().getStackInSlot(0);
	}
	
	private ItemStackHandler getResultHandler() {
		return this.handler.getResultHandler();
	}
	
	private void playSound(ServerPlayer player, ServerLevel level) {
		player.connection.send(new ClientboundSoundPacket(SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, player.getX(), player.getY(), player.getZ(), 1.0F, level.random.nextFloat() * 0.1F + 0.9F, level.random.nextLong()));
	}
	
	private NonNullList<ItemStack> asList() {
		NonNullList<ItemStack> stacks = NonNullList.withSize(5, ItemStack.EMPTY);
		for (int i = 0; i < 3; i++) {
			stacks.set(i, this.getResultHandler().getStackInSlot(i));
		}
		stacks.set(3, this.getInputItem());
		stacks.set(4, this.getFuelItem());
		return stacks;
	}
	
	private boolean onPotionAttemptBrew() {
		NonNullList<ItemStack> inputStacks = this.getInputList();
		NonNullList<ItemStack> eventStacks = this.getInputList();
		PotionBrewEvent.Pre event = new PotionBrewEvent.Pre(eventStacks);
		if (MinecraftForge.EVENT_BUS.post(event)) {
			boolean changed = false;
			for (int x = 0; x < inputStacks.size(); x++) {
				changed |= ItemStack.matches(eventStacks.get(x), inputStacks.get(x));
				inputStacks.set(x, event.getItem(x));
			}
			if (changed) {
				this.onPotionBrewed(inputStacks);
			}
			return true;
		}
		return false;
	}
	
	private void onPotionBrewed(NonNullList<ItemStack> stacks) {
		ForgeEventFactory.onPotionBrewed(stacks);
	}
	
	@Override
	public void broadcastChanges() {
		if (this.player instanceof ServerPlayer player) {
			XBNetworkHandler.sendToPlayer(player, new UpdateBrewingStandExtension(this.fuel, this.brewTime));
		}
	}
	
	@Override
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.putString("input", ForgeRegistries.ITEMS.getKey(this.input).toString());
		tag.putInt("fuel", this.fuel);
		tag.putInt("brew_time", this.brewTime);
		return tag;
	}

	@Override
	public void deserialize(CompoundTag tag) {
		this.input = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(tag.getString("input")));
		this.fuel = tag.getInt("fuel");
		this.brewTime = tag.getInt("brew_time");
	}

}
