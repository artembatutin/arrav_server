package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;
import net.arrav.world.entity.object.ObjectDirection;
import net.arrav.world.entity.object.ObjectType;

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
	public ByteBuf write(Player player, ByteBuf buf) {
		new SendCoordinates(position).write(player, buf);
		buf.message(160);
		buf.put(((0 & 7) << 4) + (0 & 7), ByteTransform.S);
		buf.put((type << 2) + (direction & 3), ByteTransform.S);
		buf.putShort(animation, ByteTransform.A);
		return buf;
	}
}
