package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.actor.player.Player;

public final class SendSkillGoal implements OutgoingPacket {
	
	private final int id, goal;
	
	public SendSkillGoal(int id, int goal) {
		this.id = id;
		this.goal = goal;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(135);
		msg.put(id);
		msg.put(goal);
		return msg.getBuffer();
	}
}
