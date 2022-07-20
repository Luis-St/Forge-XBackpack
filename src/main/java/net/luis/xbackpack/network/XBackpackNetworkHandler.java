package net.luis.xbackpack.network;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.network.packet.BackpackNextTool;
import net.luis.xbackpack.network.packet.BackpackNextToolDown;
import net.luis.xbackpack.network.packet.BackpackNextToolTop;
import net.luis.xbackpack.network.packet.BackpackOpen;
import net.luis.xbackpack.network.packet.BackpackToolDown;
import net.luis.xbackpack.network.packet.BackpackToolMid;
import net.luis.xbackpack.network.packet.BackpackToolTop;
import net.luis.xbackpack.network.packet.UpdateBackpackExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * 
 * @author Luis-st
 *
 */

public class XBackpackNetworkHandler {
	
	private static final String VERSION = "2";
	
	private static int id = 0;
	
	private static SimpleChannel simpleChannel;
	
	public static void init() {
		simpleChannel = NetworkRegistry.newSimpleChannel(new ResourceLocation(XBackpack.MOD_ID, "simple_chnanel"), () -> VERSION, VERSION::equals, VERSION::equals);
		simpleChannel.messageBuilder(BackpackOpen.class, id++, NetworkDirection.PLAY_TO_SERVER).encoder(BackpackOpen::encode).decoder(BackpackOpen::new).consumerMainThread(BackpackOpen::handle).add();
		simpleChannel.messageBuilder(BackpackNextTool.class, id++, NetworkDirection.PLAY_TO_SERVER).encoder(BackpackNextTool::encode).decoder(BackpackNextTool::new).consumerMainThread(BackpackNextTool::handle).add();
		simpleChannel.messageBuilder(BackpackToolTop.class, id++, NetworkDirection.PLAY_TO_SERVER).encoder(BackpackToolTop::encode).decoder(BackpackToolTop::new).consumerMainThread(BackpackToolTop::handle).add();
		simpleChannel.messageBuilder(BackpackToolMid.class, id++, NetworkDirection.PLAY_TO_SERVER).encoder(BackpackToolMid::encode).decoder(BackpackToolMid::new).consumerMainThread(BackpackToolMid::handle).add();
		simpleChannel.messageBuilder(BackpackToolDown.class, id++, NetworkDirection.PLAY_TO_SERVER).encoder(BackpackToolDown::encode).decoder(BackpackToolDown::new).consumerMainThread(BackpackToolDown::handle).add();
		simpleChannel.messageBuilder(BackpackNextToolTop.class, id++, NetworkDirection.PLAY_TO_SERVER).encoder(BackpackNextToolTop::encode).decoder(BackpackNextToolTop::new).consumerMainThread(BackpackNextToolTop::handle).add();
		simpleChannel.messageBuilder(BackpackNextToolDown.class, id++, NetworkDirection.PLAY_TO_SERVER).encoder(BackpackNextToolDown::encode).decoder(BackpackNextToolDown::new).consumerMainThread(BackpackNextToolDown::handle).add();
		simpleChannel.messageBuilder(UpdateBackpackExtension.class, id++, NetworkDirection.PLAY_TO_SERVER).encoder(UpdateBackpackExtension::encode).decoder(UpdateBackpackExtension::new).consumerMainThread(UpdateBackpackExtension::handle).add();
	}
	
	public static SimpleChannel getChannel() {
		return simpleChannel;
	}

}
