package net.arrav.net.packet.out;


import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendClearText implements OutgoingPacket {
	
	private final int start, count;
	
	public SendClearText(int start, int count) {
		this.start = start;
		this.count = count;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		for(int i = start; i < start + count; i++) {
			player.interfaceTexts.remove(i);
		}
		out.message(127, GamePacketType.FIXED);
		out.putShort(start, ByteTransform.A);
		out.putShort(count, ByteTransform.A);
		return out;
	}
}
