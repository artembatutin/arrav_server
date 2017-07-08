package net.edge.net.packet.out;

import com.google.common.base.Preconditions;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendCameraReset implements OutgoingPacket {
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(107);
	}
}
