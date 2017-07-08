package net.edge.net.packet.out;

import net.edge.content.TabInterface;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

/**
 * Created by artembatutin on 7/7/17.
 */
public final class SendForceTab implements OutgoingPacket {
	private final TabInterface tab;
	
	public SendForceTab(TabInterface tab) {
		this.tab = tab;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(106);
		msg.put(tab.getOld());
		msg.put(tab.getNew());
	}
}
