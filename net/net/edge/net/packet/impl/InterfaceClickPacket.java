package net.edge.net.packet.impl;

import net.edge.content.minigame.MinigameHandler;
import net.edge.net.codec.ByteMessage;
import net.edge.net.packet.PacketReader;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;

/**
 * The message sent from the client when a player clicks certain options on an
 * interface.
 * @author lare96 <http://github.com/lare96>
 */
public final class InterfaceClickPacket implements PacketReader {
	
	@Override
	public void handle(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.INTERFACE_CLICK))
			return;
		
		if(World.getExchangeSessionManager().reset(player)) {
			return;
		}
		
		MinigameHandler.executeVoid(player, t -> t.onInterfaceClick(player));
		player.getMessages().sendCloseWindows();
		player.getActivityManager().execute(ActivityManager.ActivityType.INTERFACE_CLICK);
	}
}
