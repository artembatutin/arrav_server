package net.arrav.net.packet.out;


import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendConfig implements OutgoingPacket {
	
	private final int id, state;
	
	public SendConfig(int id, int state) {
		this.id = id;
		this.state = state;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		if(state < Byte.MIN_VALUE || state > Byte.MAX_VALUE) {
			out.message(87);
			out.putShort(id, ByteOrder.LITTLE);
			out.putInt(state, ByteOrder.MIDDLE);
			return out;
		}
		out.message(36);
		out.putShort(id, ByteOrder.LITTLE);
		out.put(state);
		return out;
	}
}
