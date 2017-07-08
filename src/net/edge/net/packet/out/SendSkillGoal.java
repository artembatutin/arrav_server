package net.edge.net.packet.out;

import net.edge.locale.Position;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendSkillGoal implements OutgoingPacket {
	
	private final int id, goal;
	
	public SendSkillGoal(int id, int goal) {
		this.id = id;
		this.goal = goal;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(135);
		msg.put(id);
		msg.put(goal);
	}
}
