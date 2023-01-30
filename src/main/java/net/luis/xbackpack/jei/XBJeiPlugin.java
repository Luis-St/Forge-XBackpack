package net.luis.xbackpack.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.client.gui.screens.BackpackScreen;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Luis-st
 *
 */

@JeiPlugin
public class XBJeiPlugin implements IModPlugin {
	
	private final ResourceLocation pluginId;
	
	public XBJeiPlugin() {
		this.pluginId = new ResourceLocation(XBackpack.MOD_ID, "jei");
	}
	
	@Override
	public @NotNull ResourceLocation getPluginUid() {
		return this.pluginId;
	}
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGuiContainerHandler(BackpackScreen.class, new BackpackContainerHandler());
	}
	
}
