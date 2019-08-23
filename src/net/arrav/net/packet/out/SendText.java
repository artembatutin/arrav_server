package net.arrav.net.packet.out;

import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendText implements OutgoingPacket {
	
	private final int id;
	private final String text;
	
	public SendText(int id, String text) {
		this.id = id;
		this.text = text;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(126, GamePacketType.VARIABLE_SHORT);
		out.putCString(text);
		out.putShort(id, ByteTransform.A);
		out.endVarSize();
		out.endVarSize();
		return out;
	}
}
