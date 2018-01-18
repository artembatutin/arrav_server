package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendSkillGoal implements OutgoingPacket {
	
	private final int id, goal;
	
	public SendSkillGoal(int id, int goal) {
		this.id = id;
		this.goal = goal;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(135);
		buf.put(id);
		buf.put(goal);
		return buf;
	}
}
