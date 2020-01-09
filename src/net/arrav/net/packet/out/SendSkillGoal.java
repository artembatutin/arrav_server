package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendSkillGoal implements OutgoingPacket {
	
	private final int id, goal;
	
	public SendSkillGoal(int id, int goal) {
		this.id = id;
		this.goal = goal;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(135);
		out.put(id);
		out.put(goal);
		return out;
	}
}
