package com.rageps.net.packet.out;

import io.netty.buffer.ByteBuf;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.ObjectDirection;
import com.rageps.world.entity.object.ObjectType;
import com.rageps.world.locale.Position;

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
	public OutgoingPacket coordinatePacket() {
		return new SendCoordinates(position);
	}
}
