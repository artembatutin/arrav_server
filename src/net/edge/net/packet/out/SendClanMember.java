package net.edge.net.packet.out;

import net.edge.content.clanchat.ClanMember;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendClanMember implements OutgoingPacket {
	
	private final int index;
	private final ClanMember member;
	
	public SendClanMember(int index, ClanMember member) {
		this.index = index;
		this.member = member;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(41, MessageType.VARIABLE);
		msg.putShort(index);
		msg.putCString(member.getPlayer().getCredentials().getUsername());
		msg.put(member.isMuted() ? 1 : 0);
		msg.put(member.getRank().toIcon(player, member.getPlayer()));
		msg.endVarSize();
	}
}
