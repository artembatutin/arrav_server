package net.edge.net.packet.impl;

import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.PacketReader;
import net.edge.world.node.entity.player.Player;

public final class DefaultPacket implements PacketReader {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
	
	}
}
