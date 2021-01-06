package com.rageps.net.packet.out;

import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.object.DynamicObject;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.entity.object.ObjectDirection;
import com.rageps.world.entity.object.ObjectType;
import com.rageps.world.locale.Position;
import io.netty.buffer.ByteBuf;

import java.util.Optional;

public final class SendObject implements OutgoingPacket {
	
	private final GameObject object;
	
	public SendObject(GameObject object) {
		this.object = object;
	}
	
	public static void construction(Player player, int objectX, int objectY, int objectId, int face, int objectType, int height) {
		Optional<ObjectDirection> dir = ObjectDirection.valueOf(face);
		Optional<ObjectType> type = ObjectType.valueOf(objectType);
		if(!dir.isPresent()) {
			if(player.getRights() == Rights.ADMINISTRATOR)
				player.message("Couldn't find direction, " + face);
			return;
		}
		if(!type.isPresent()) {
			if(player.getRights() == Rights.ADMINISTRATOR)
				player.message("Couldn't find type, " + objectType);
			return;
		}
		player.send(new Object(new DynamicObject(objectId, new Position(objectX, objectY, height), dir.get(), type.get(), false, 0, player.getInstance())));
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(151);
		out.put(0, ByteTransform.S);
		out.putInt(object.getId());
		out.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteTransform.S);
		return out;
	}
	
	@Override
	public OutgoingPacket coordinatePacket() {
		return new SendCoordinates(object.getPosition());
	}
}
