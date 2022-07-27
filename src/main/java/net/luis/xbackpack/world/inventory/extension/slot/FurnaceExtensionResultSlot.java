package net.luis.xbackpack.world.inventory.extension.slot;

import net.luis.xbackpack.world.inventory.extension.FurnaceExtensionMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.IItemHandler;

/**
 * 
 * @author Luis-st
 *
 */

public class FurnaceExtensionResultSlot extends ExtensionSlot {
	
	private final Player player;
	private int removeCount;
	
	public FurnaceExtensionResultSlot(FurnaceExtensionMenu extensionMenu, Player player, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(extensionMenu, itemHandler, index, xPosition, yPosition);
		this.player = player;
	}
	
	@Override
	public FurnaceExtensionMenu getMenu() {
		return (FurnaceExtensionMenu) super.getMenu();
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
	
	@Override
	public ItemStack remove(int amount) {
		if (this.hasItem()) {
			this.removeCount += Math.min(amount, this.getItem().getCount());
		}
		return super.remove(amount);
	}
	
	@Override
	public void onTake(Player player, ItemStack stack) {
		this.checkTakeAchievements(stack);
		super.onTake(player, stack);
	}
	
	@Override
	protected void onQuickCraft(ItemStack stack, int amount) {
		this.removeCount += amount;
		this.checkTakeAchievements(stack);
	}
	
	@Override
	protected void checkTakeAchievements(ItemStack stack) {
		stack.onCraftedBy(this.player.level, this.player, this.removeCount);
		this.removeCount = 0;
		ForgeEventFactory.firePlayerSmeltedEvent(this.player, stack);
	}

}
