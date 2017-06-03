package net.edge.net.packet.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.packet.PacketReader;
import net.edge.world.node.entity.player.Player;

/**
 * The decoder used to handle useless messages sent from the client.
 * @author lare96 <http://github.com/lare96>
 */
public final class DefaultMessage implements PacketReader {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		
	}
}
