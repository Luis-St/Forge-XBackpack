package net.luis.xbackpack.client;

import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.luis.xbackpack.client.gui.screens.extension.AbstractExtensionScreen;
import net.luis.xbackpack.client.gui.screens.extension.FurnaceExtensionScreen;
import net.luis.xbackpack.world.extension.BackpackExtension;
import net.minecraft.client.Minecraft;

public class XBackpackClientPacketHandler {
	
	public static void handleUpdateFurnaceExtension(int cookingProgress, int fuelProgress) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.screen instanceof BackpackScreen screen) {
			AbstractExtensionScreen extensionScreen = screen.getExtensionScreen(BackpackExtension.FURNACE.get());
			if (extensionScreen instanceof FurnaceExtensionScreen furnaceExtension) {
				furnaceExtension.setCookingProgress(cookingProgress);
				furnaceExtension.setFuelProgress(fuelProgress);
			}
		}
	}
	
}
