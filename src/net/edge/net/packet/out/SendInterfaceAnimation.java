package net.edge.net.packet.out;

import net.edge.locale.Position;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendInterfaceAnimation implements OutgoingPacket {
	
	private final int id, animation;
	
	public SendInterfaceAnimation(int id, int animation) {
		this.id = id;
		this.animation = animation;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(200);
		msg.putShort(id);
		msg.putShort(animation);
	}
}
