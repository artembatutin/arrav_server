package net.arrav.net.packet.out;


import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendPrivateMessage implements OutgoingPacket {
	
	private final long name;
	private final int rights, size;
	private final byte[] message;
	
	public SendPrivateMessage(long name, int rights, byte[] message, int size) {
		this.name = name;
		this.rights = rights;
		this.message = message;
		this.size = size;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(196, GamePacketType.VARIABLE_BYTE);
		out.putLong(name);
		out.putInt(player.getPrivateMessage().getLastMessage().getAndIncrement());
		out.put(rights);
		out.putBytes(message, size);
		out.endVarSize();
		return out;
	}
}
