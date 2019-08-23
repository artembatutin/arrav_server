package net.arrav.net.packet.out;


import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendContextMenu implements OutgoingPacket {
	
	private final int slot;
	private final boolean top;
	private final String option;
	
	public SendContextMenu(int slot, boolean top, String option) {
		this.slot = slot;
		this.top = top;
		this.option = option;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(104, GamePacketType.VARIABLE_BYTE);
		out.put(slot, ByteTransform.C);
		out.put(top ? 1 : 0, ByteTransform.A);
		out.putCString(option);
		out.endVarSize();
		return out;
	}
}
