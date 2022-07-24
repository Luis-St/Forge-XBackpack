package net.luis.xbackpack.world.inventory.extension;

import java.util.function.Consumer;

import net.luis.xbackpack.world.capability.XBackpackCapabilities;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.luis.xbackpack.world.inventory.BackpackMenu;
import net.luis.xbackpack.world.inventory.extension.slot.FurnaceExtensionFuelSlot;
import net.luis.xbackpack.world.inventory.extension.slot.FurnaceExtensionInputSlot;
import net.luis.xbackpack.world.inventory.extension.slot.FurnaceExtensionResultSlot;
import net.luis.xbackpack.world.inventory.extension.slot.FurnaceExtensionResultStorageSlot;
import net.luis.xbackpack.world.inventory.handler.FurnaceCraftingHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

/**
 * 
 * @author Luis-st
 *
 */

public class FurnaceExtensionMenu extends AbstractExtensionMenu {
	
	private final FurnaceCraftingHandler handler;
	
	public FurnaceExtensionMenu(BackpackMenu menu, Player player) {
		super(menu, player, BackpackExtension.FURNACE.get());
		this.handler = this.player.getCapability(XBackpackCapabilities.BACKPACK, null).orElseThrow(NullPointerException::new).getFurnaceHandler();
	}

	@Override
	public void addSlots(Consumer<Slot> consumer) {
		for (int i = 0; i < 4; i++) {
			consumer.accept(new FurnaceExtensionInputSlot(this, this.player, this.handler.getInputStorageHandler(), i, 225 + i * 18, 49));
		}
		consumer.accept(new FurnaceExtensionInputSlot(this, this.player, this.handler.getInputHandler(), 0, 225, 71));
		consumer.accept(new FurnaceExtensionFuelSlot(this, this.handler.getFuelHandler(), 0, 225, 107));
		consumer.accept(new FurnaceExtensionResultSlot(this, this.player, this.handler.getResultHandler(), 0, 275, 89));
		for (int i = 0; i < 4; i++) {
			consumer.accept(new FurnaceExtensionResultStorageSlot(this, this.handler.getResultStorageHandler(), i, 225 + i * 18, 129));
		}
	}

}
