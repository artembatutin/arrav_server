package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendClanBanned implements OutgoingPacket {
	
	private final ObjectList<String> bans;
	
	public SendClanBanned(ObjectList<String> bans) {
		this.bans = bans;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(52, GamePacketType.VARIABLE_BYTE);
		buf.putShort(bans.size());
		for(String s : bans) {
			buf.putCString(s);
		}
		buf.endVarSize();
		buf.endVarSize();
		return buf;
	}
}
