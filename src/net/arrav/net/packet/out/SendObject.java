package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.object.DynamicObject;
import net.arrav.world.entity.object.GameObject;
import net.arrav.world.entity.object.ObjectDirection;
import net.arrav.world.entity.object.ObjectType;
import net.arrav.world.locale.Position;

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
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(151);
		out.put(0, ByteTransform.S);
		out.putInt(object.getId());
		out.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteTransform.S);
		return out;
	}
	
	@Override
	public OutgoingPacket coordinatePacket(Player player) {
		return new SendCoordinates(object.getPosition());
	}
}
