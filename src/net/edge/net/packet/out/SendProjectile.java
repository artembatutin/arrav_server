package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.locale.Position;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.actor.player.Player;

public final class SendProjectile implements OutgoingPacket {
	
	private final Position position, offset;
	private final int speed, gfxMoving, startHeight, endHeight, lockon, time;
	
	public SendProjectile(Position position, Position offset, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time) {
		this.position = position;
		this.offset = offset;
		this.speed = speed;
		this.gfxMoving = gfxMoving;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.lockon = lockon;
		this.time = time;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		new SendCoordinates(position).write(player, msg);
		msg.message(117);
		msg.put(0);
		msg.put(offset.getX());
		msg.put(offset.getY());
		msg.putShort(lockon);
		msg.putShort(gfxMoving);
		msg.put(startHeight);
		msg.put(endHeight);
		msg.putShort(time);
		msg.putShort(speed);
		msg.put(16);
		msg.put(64);
		return msg.getBuffer();
	}
}
