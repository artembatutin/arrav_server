package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.locale.Position;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.GameObject;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectType;

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
		player.out(new SendObject(new DynamicObject(objectId, new Position(objectX, objectY, height), dir.get(), type.get(), false, 0, player.getInstance())));
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		new SendCoordinates(object.getGlobalPos()).write(player, msg);
		msg.message(151);
		msg.put(0, ByteTransform.S);
		msg.putInt(object.getId());
		msg.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteTransform.S);
		return msg.getBuffer();
	}
}
