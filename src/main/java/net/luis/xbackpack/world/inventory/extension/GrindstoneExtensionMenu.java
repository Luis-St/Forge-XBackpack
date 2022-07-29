package net.luis.xbackpack.world.inventory.extension;

import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.world.capability.XBackpackCapabilities;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.extension.slot.ExtensionSlot;
import net.luis.xbackpack.world.inventory.handler.CraftingHandler;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 
 * @author Luis-st
 *
 */

public class GrindstoneExtensionMenu extends AbstractExtensionMenu {
	
	private final CraftingHandler handler;
	
	public GrindstoneExtensionMenu(BackpackMenu menu, Player player) {
		super(menu, player, BackpackExtension.GRINDSTONE.get());
		this.handler = this.player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new).getGrindstoneHandler();
	}
	
	@Override
	public void open() {
		this.createResult();
	}
	
	@Override
	public void addSlots(Consumer<Slot> consumer) {
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 0, 243, 172) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.isDamageableItem() || stack.is(Items.ENCHANTED_BOOK) || stack.isEnchanted();
			}
		});
		consumer.accept(new ExtensionSlot(this, this.handler.getInputHandler(), 1, 243, 193) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.isDamageableItem() || stack.is(Items.ENCHANTED_BOOK) || stack.isEnchanted();
			}
		});
		consumer.accept(new ExtensionSlot(this, this.handler.getResultHandler(), 0, 305, 187, false) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
			
			@Override
			public void onTake(Player player, ItemStack stack) {
				GrindstoneExtensionMenu.this.onTake(player, stack);
			}
		});
	}
	
	private void onTake(Player player, ItemStack stack) {
		if (player instanceof ServerPlayer serverPlayer) {
			player.giveExperiencePoints(this.getExperienceAmount(player.level));
			this.playSound(serverPlayer, serverPlayer.getLevel());
		}
		this.handler.getInputHandler().setStackInSlot(0, ItemStack.EMPTY);
		this.handler.getInputHandler().setStackInSlot(1, ItemStack.EMPTY);
	}

	private int getExperienceAmount(Level level) {
		int amount = 0;
		amount += this.getExperienceFromItem(this.handler.getInputHandler().getStackInSlot(0));
		amount += this.getExperienceFromItem(this.handler.getInputHandler().getStackInSlot(1));
		if (amount > 0) {
			int experience = (int) Math.ceil((double) amount / 2.0D);
			return experience + level.random.nextInt(experience);
		} else {
			return 0;
		}
	}

	private int getExperienceFromItem(ItemStack stack) {
		int experience = 0;
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
		for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			Enchantment enchantment = entry.getKey();
			if (!enchantment.isCurse()) {
				experience += enchantment.getMinCost(entry.getValue());
			}
		}
		return experience;
	}
	
	private void playSound(ServerPlayer player, ServerLevel level) {
		player.connection.send(new ClientboundSoundPacket(SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, player.getX(), player.getY(), player.getZ(), 1.0F, level.random.nextFloat() * 0.1F + 0.9F, level.random.nextLong()));
	}
	
	@Override
	public void slotsChanged() {
		this.createResult();
	}
	
	private void createResult() {
		ItemStack topStack = this.handler.getInputHandler().getStackInSlot(0);
		ItemStack bottomStack = this.handler.getInputHandler().getStackInSlot(1);
		boolean hasInput = !topStack.isEmpty() || !bottomStack.isEmpty();
		boolean hasInputs = !topStack.isEmpty() && !bottomStack.isEmpty();
		if (!hasInput) {
			XBackpack.LOGGER.debug("no input");
			this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
		} else {
			boolean hasEnchantedBook = !topStack.isEmpty() && !topStack.is(Items.ENCHANTED_BOOK) && !topStack.isEnchanted() || !bottomStack.isEmpty() && !bottomStack.is(Items.ENCHANTED_BOOK) && !bottomStack.isEnchanted();
			if (topStack.getCount() > 1 || bottomStack.getCount() > 1) {
				XBackpack.LOGGER.debug("count of stacks > 1");
				this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
				this.menu.broadcastChanges();
				return;
			} else if (!hasInputs && hasEnchantedBook) {
				XBackpack.LOGGER.debug("single input and enchanted book");
				this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
				this.menu.broadcastChanges();
				return;
			}
			int count = 1;
			int damageValue;
			ItemStack resultStack;
			if (hasInputs) {
				XBackpack.LOGGER.debug("has input stacks");
				if (!topStack.is(bottomStack.getItem())) {
					XBackpack.LOGGER.debug("top bottom not same item");
					this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
					this.menu.broadcastChanges();
					return;
				}
				int k = topStack.getMaxDamage() - topStack.getDamageValue();
				int l = topStack.getMaxDamage() - bottomStack.getDamageValue();
				int i1 = k + l + topStack.getMaxDamage() * 5 / 100;
				damageValue = Math.max(topStack.getMaxDamage() - i1, 0);
				resultStack = this.mergeEnchants(topStack, bottomStack);
				if (!resultStack.isRepairable()) {
					XBackpack.LOGGER.debug("not repairable");
					damageValue = topStack.getDamageValue();
				}
				if (!resultStack.isDamageableItem() || !resultStack.isRepairable()) {
					if (!ItemStack.matches(topStack, bottomStack)) {
						XBackpack.LOGGER.debug("top bottom not match");
						this.handler.getResultHandler().setStackInSlot(0, ItemStack.EMPTY);
						this.menu.broadcastChanges();
						return;
					}
					count = 2;
				}
			} else {
				XBackpack.LOGGER.debug("single input");
				boolean topEmpty = !topStack.isEmpty();
				damageValue = topEmpty ? topStack.getDamageValue() : bottomStack.getDamageValue();
				resultStack = topEmpty ? topStack : bottomStack;
			}
			this.handler.getResultHandler().setStackInSlot(0, this.removeNonCurses(resultStack, damageValue, count));
		}
		this.menu.broadcastChanges();
	}

	private ItemStack mergeEnchants(ItemStack firstStack, ItemStack secondStack) {
		ItemStack resultStack = firstStack.copy();
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(secondStack);
		for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			Enchantment enchantment = entry.getKey();
			if (!enchantment.isCurse() || EnchantmentHelper.getTagEnchantmentLevel(enchantment, resultStack) == 0) {
				resultStack.enchant(enchantment, entry.getValue());
			}
		}
		return resultStack;
	}

	private ItemStack removeNonCurses(ItemStack inputStack, int damageValue, int count) {
		ItemStack resultStack = inputStack.copy();
		resultStack.removeTagKey("Enchantments");
		resultStack.removeTagKey("StoredEnchantments");
		if (damageValue > 0) {
			resultStack.setDamageValue(damageValue);
		} else {
			resultStack.removeTagKey("Damage");
		}
		resultStack.setCount(count);
		
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(inputStack).entrySet().stream().filter((entry) -> {
			return entry.getKey().isCurse();
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		EnchantmentHelper.setEnchantments(enchantments, resultStack);
		XBackpack.LOGGER.debug("result enchanted {} -> {}", resultStack.isEnchanted(), EnchantmentHelper.getEnchantments(resultStack).keySet().stream().map(ForgeRegistries.ENCHANTMENTS::getKey).toList());
		resultStack.setRepairCost(0);
		if (resultStack.is(Items.ENCHANTED_BOOK) && enchantments.size() == 0) {
			resultStack = new ItemStack(Items.BOOK);
			if (inputStack.hasCustomHoverName()) {
				resultStack.setHoverName(inputStack.getHoverName());
			}
		}
		for (int i = 0; i < enchantments.size(); ++i) {
			resultStack.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(resultStack.getBaseRepairCost()));
		}
		return resultStack;
	}
	
}
