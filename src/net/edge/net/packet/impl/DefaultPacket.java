package net.edge.net.packet.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.packet.PacketReader;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager.ActivityType;

public final class DefaultPacket implements PacketReader {
	
	@Override
	public void handle(Player player, int opcode, int size, ByteMessage payload) {
	
	}
}
