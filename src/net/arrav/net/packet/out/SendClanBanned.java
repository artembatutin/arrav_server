package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendClanBanned implements OutgoingPacket {
	
	private final ObjectList<String> bans;
	
	public SendClanBanned(ObjectList<String> bans) {
		this.bans = bans;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(52, GamePacketType.VARIABLE_BYTE);
		out.putShort(bans.size());
		for(String s : bans) {
			out.putCString(s);
		}
		out.endVarSize();
		out.endVarSize();
		return out;
	}
}
