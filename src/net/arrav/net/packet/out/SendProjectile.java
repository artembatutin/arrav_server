package net.arrav.net.packet.out;


import net.arrav.net.codec.game.GamePacket;
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
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(117);
		out.put(0);
		out.put(offset.getX());
		out.put(offset.getY());
		out.putShort(lockon);
		out.putShort(gfxMoving);
		out.put(startHeight);
		out.put(endHeight);
		out.putShort(time);
		out.putShort(speed);
		out.put(16);
		out.put(64);
		return out;
	}
	
	@Override
	public GamePacket coordinatePacket(Player player) {
		return new SendCoordinates(position).write(player);
	}
}
