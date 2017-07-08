package net.edge.net.packet.out;

import net.edge.locale.Position;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectNode;
import net.edge.world.object.ObjectType;

import java.util.Optional;

public final class SendObject implements OutgoingPacket {
	
	private final ObjectNode object;
	
	public SendObject(ObjectNode object) {
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
	public void write(Player player) {
		player.write(new SendCoordinates(object.getGlobalPos()));
		GameBuffer msg = player.getSession().getStream();
		msg.message(151);
		msg.put(0, ByteTransform.S);
		msg.putInt(object.getId());
		msg.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteTransform.S);
	}
}
