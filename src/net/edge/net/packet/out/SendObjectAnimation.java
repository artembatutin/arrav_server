package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.locale.Position;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectType;

public final class SendObjectAnimation implements OutgoingPacket {
	
	private final Position position;
	private final ObjectType type;
	private final ObjectDirection direction;
	private final int animation;
	
	public SendObjectAnimation(Position position, int animation, ObjectType type, ObjectDirection direction) {
		this.position = position;
		this.animation = animation;
		this.type = type;
		this.direction = direction;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		new SendCoordinates(position).write(player, msg);
		msg.message(160);
		msg.put(((0 & 7) << 4) + (0 & 7), ByteTransform.S);
		msg.put((type.getId() << 2) + (direction.getId() & 3), ByteTransform.S);
		msg.putShort(animation, ByteTransform.A);
		return msg.getBuffer();
	}
}
