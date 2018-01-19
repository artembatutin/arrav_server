package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

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
	public ByteBuf write(Player player, ByteBuf buf) {
		new SendCoordinates(position).write(player, buf);
		buf.message(117);
		buf.put(0);
		buf.put(offset.getX());
		buf.put(offset.getY());
		buf.putShort(lockon);
		buf.putShort(gfxMoving);
		buf.put(startHeight);
		buf.put(endHeight);
		buf.putShort(time);
		buf.putShort(speed);
		buf.put(16);
		buf.put(64);
		return buf;
	}
}
