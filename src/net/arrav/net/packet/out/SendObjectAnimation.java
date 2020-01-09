package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.object.ObjectDirection;
import net.arrav.world.entity.object.ObjectType;
import net.arrav.world.locale.Position;

public final class SendObjectAnimation implements OutgoingPacket {
	
	private final Position position;
	private final int type;
	private final int direction;
	private final int animation;
	
	public SendObjectAnimation(Position position, int animation, ObjectType type, ObjectDirection direction) {
		this.position = position;
		this.animation = animation;
		this.type = type.getId();
		this.direction = direction.getId();
	}
	
	public SendObjectAnimation(Position position, int animation, ObjectType type, int direction) {
		this.position = position;
		this.animation = animation;
		this.type = type.getId();
		this.direction = direction;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(160);
		out.put(((0 & 7) << 4) + (0 & 7), ByteTransform.S);
		out.put((type << 2) + (direction & 3), ByteTransform.S);
		out.putShort(animation, ByteTransform.A);
		return out;
	}
	
	@Override
	public OutgoingPacket coordinatePacket(Player player) {
		return new SendCoordinates(position);
	}
}
