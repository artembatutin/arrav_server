package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.locale.Position;
import net.arrav.world.object.DynamicObject;
import net.arrav.world.object.GameObject;
import net.arrav.world.object.ObjectDirection;
import net.arrav.world.object.ObjectType;

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
	public ByteBuf write(Player player, ByteBuf buf) {
		new SendCoordinates(object.getGlobalPos()).write(player, buf);
		buf.message(151);
		buf.put(0, ByteTransform.S);
		buf.putInt(object.getId());
		buf.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteTransform.S);
		return buf;
	}
}
