package net.edge.net.packet.out;

import net.edge.locale.Position;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

import java.util.Objects;

import static net.edge.world.node.NodeState.INACTIVE;

public final class SendGraphic implements OutgoingPacket {
	
	private final int id, level;
	private final Position position;
	
	public static void local(Player player, int id, Position position, int level) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		for(Player p : player.getLocalPlayers()) {
			if(p == null)
				continue;
			p.out(new SendGraphic(id, position, level));
		}
	}
	
	public SendGraphic(int id, Position position, int level) {
		this.id = id;
		this.position = position;
		this.level = level;
	}
	
	@Override
	public void write(Player player) {
		player.write(new SendCoordinates(position));
		GameBuffer msg = player.getSession().getStream();
		msg.message(4);
		msg.put(0);
		msg.putShort(id);
		msg.put(level);
		msg.putShort(0);
	}
}
