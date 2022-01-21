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

/**
 * 
 * @author Luis-st
 *
 */

public class XBackpackNetworkHandler {
	
	/**
	 * {@link SimpleChannel} version
	 */
	protected static final String VERSION = "1";
	
	/**
	 * network packet registration id
	 */
	protected static int id = 0;
	
	/**
	 * {@link SimpleChannel}
	 */
	protected static SimpleChannel simpleChannel;

	/**
	 * initialized the {@link SimpleChannel}
	 * and all network packets
	 */
	public static void init() {
		simpleChannel = NetworkRegistry.newSimpleChannel(new ResourceLocation(XBackpack.MOD_ID, "simple_chnanel"), () -> VERSION, VERSION::equals, VERSION::equals);
		simpleChannel.registerMessage(id++, BackpackOpen.class, BackpackOpen::encode, BackpackOpen::decode, BackpackOpen::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		simpleChannel.registerMessage(id++, BackpackNextTool.class, BackpackNextTool::encode, BackpackNextTool::decode, BackpackNextTool::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		simpleChannel.registerMessage(id++, BackpackToolTop.class, BackpackToolTop::encode, BackpackToolTop::decode, BackpackToolTop::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		simpleChannel.registerMessage(id++, BackpackToolDown.class, BackpackToolDown::encode, BackpackToolDown::decode, BackpackToolDown::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		simpleChannel.registerMessage(id++, BackpackNextToolDown.class, BackpackNextToolDown::encode, BackpackNextToolDown::decode, BackpackNextToolDown::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		simpleChannel.registerMessage(id++, BackpackNextToolTop.class, BackpackNextToolTop::encode, BackpackNextToolTop::decode, BackpackNextToolTop::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
	}
	
	/**
	 * @return the {@link XBackpack} {@link SimpleChannel}
	 */
	public static SimpleChannel getChannel() {
		return simpleChannel;
	}

}
