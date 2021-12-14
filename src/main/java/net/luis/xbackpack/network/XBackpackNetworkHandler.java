package net.luis.xbackpack.network;

import java.util.Optional;

import net.luis.xbackpack.XBackpack;
import net.luis.xbackpack.network.packet.BackpackNextTool;
import net.luis.xbackpack.network.packet.BackpackNextToolDown;
import net.luis.xbackpack.network.packet.BackpackNextToolTop;
import net.luis.xbackpack.network.packet.BackpackOpen;
import net.luis.xbackpack.network.packet.BackpackToolDown;
import net.luis.xbackpack.network.packet.BackpackToolTop;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class XBackpackNetworkHandler {
	
	private static final String version = "1";
	private static int id = 0;
	private static SimpleChannel simpleChannel;

	public static void init() {
		simpleChannel = NetworkRegistry.newSimpleChannel(new ResourceLocation(XBackpack.MOD_ID, "simple_chnanel"), () -> version, version::equals, version::equals);
		simpleChannel.registerMessage(id++, BackpackOpen.class, BackpackOpen::encode, BackpackOpen::decode, BackpackOpen::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		simpleChannel.registerMessage(id++, BackpackNextTool.class, BackpackNextTool::encode, BackpackNextTool::decode, BackpackNextTool::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		simpleChannel.registerMessage(id++, BackpackToolTop.class, BackpackToolTop::encode, BackpackToolTop::decode, BackpackToolTop::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		simpleChannel.registerMessage(id++, BackpackToolDown.class, BackpackToolDown::encode, BackpackToolDown::decode, BackpackToolDown::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		simpleChannel.registerMessage(id++, BackpackNextToolDown.class, BackpackNextToolDown::encode, BackpackNextToolDown::decode, BackpackNextToolDown::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		simpleChannel.registerMessage(id++, BackpackNextToolTop.class, BackpackNextToolTop::encode, BackpackNextToolTop::decode, BackpackNextToolTop::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
	}
	
	public static SimpleChannel getChannel() {
		return simpleChannel;
	}

}