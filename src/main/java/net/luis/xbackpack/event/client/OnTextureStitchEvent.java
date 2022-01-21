package net.luis.xbackpack.event.client;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.common.inventory.slot.BackpackToolSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 
 * @author Luis-st
 *
 */

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OnTextureStitchEvent {

	/**
	 * stitch the {@link BackpackToolSlot#EMPTY_TOOL_SLOT} to the {@link InventoryMenu#BLOCK_ATLAS}
	 */
	@SubscribeEvent
	public static void textureStitch(TextureStitchEvent.Pre event) {
		if (event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {
			event.addSprite(new ResourceLocation(XBackpack.MOD_ID, "item/empty_tool_slot"));
		}
	}

}