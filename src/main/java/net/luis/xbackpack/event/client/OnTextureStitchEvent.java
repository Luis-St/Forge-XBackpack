package net.luis.xbackpack.event.client;

import net.luis.xbackpack.XBackpack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = XBackpack.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OnTextureStitchEvent {

	@SubscribeEvent
	public static void TextureStitch(TextureStitchEvent.Pre event) {
		event.addSprite(new ResourceLocation(XBackpack.MOD_ID, "item/empty_tool_slot"));
	}

}