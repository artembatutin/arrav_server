package net.edge.net.packet.out;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.clanchat.ClanMember;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendClanBanned implements OutgoingPacket {
	
	private final ObjectList<String> bans;
	
	public SendClanBanned(ObjectList<String> bans) {
		this.bans = bans;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(52, MessageType.VARIABLE);
		msg.putShort(bans.size());
		for(String s : bans) {
			msg.putCString(s);
		}
		msg.endVarSize();
	}
}
