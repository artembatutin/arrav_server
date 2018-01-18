package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectType;

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
