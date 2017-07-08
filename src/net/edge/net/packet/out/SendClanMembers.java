package net.edge.net.packet.out;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.clanchat.ClanMember;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendClanMembers implements OutgoingPacket {
	
	private final ObjectList<ClanMember> members;
	
	public SendClanMembers(ObjectList<ClanMember> members) {
		this.members = members;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(51, MessageType.VARIABLE);
		msg.putShort(members != null ? members.size() : 0);
		if(members != null) {
			for(ClanMember m : members) {
				msg.putCString(m.getPlayer().getCredentials().getUsername());
				msg.put(m.isMuted() ? 1 : 0);
				msg.put(m.getRank().toIcon(player, m.getPlayer()));
			}
		}
		msg.endVarSize();
	}
}
