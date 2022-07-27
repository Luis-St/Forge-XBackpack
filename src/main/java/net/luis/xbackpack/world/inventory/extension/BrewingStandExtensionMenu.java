package net.luis.xbackpack.world.inventory.extension;

import java.util.function.Consumer;

import net.luis.xbackpack.world.capability.XBackpackCapabilities;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.extension.slot.BrewingStandExtensionFuelSlot;
import net.luis.xbackpack.world.inventory.extension.slot.BrewingStandExtensionInputSlot;
import net.luis.xbackpack.world.inventory.extension.slot.BrewingStandExtensionResultSlot;
import net.luis.xbackpack.world.inventory.handler.BrewingStandCraftingHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

/**
 * 
 * @author Luis-st
 *
 */

public class BrewingStandExtensionMenu extends AbstractExtensionMenu {
	
	private final BrewingStandCraftingHandler handler;
	
	public BrewingStandExtensionMenu(BackpackMenu menu, Player player) {
		super(menu, player, BackpackExtension.BREWING_STAND.get());
		this.handler = this.player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new).getBrewingHandler();
	}

	@Override
	public void addSlots(Consumer<Slot> consumer) {
		consumer.accept(new BrewingStandExtensionInputSlot(this, this.handler.getInputHandler(), 0, 277, 146));
		consumer.accept(new BrewingStandExtensionFuelSlot(this, this.handler.getFuelHandler(), 0, 225, 146));
		consumer.accept(new BrewingStandExtensionResultSlot(this, this.handler.getResultHandler(), 0, 254, 180));
		consumer.accept(new BrewingStandExtensionResultSlot(this, this.handler.getResultHandler(), 1, 277, 187));
		consumer.accept(new BrewingStandExtensionResultSlot(this, this.handler.getResultHandler(), 2, 300, 180));
	}

}
