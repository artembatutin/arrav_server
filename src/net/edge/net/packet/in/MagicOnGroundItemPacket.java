package net.edge.net.packet.in;

import net.edge.GameServer;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.entity.actor.player.Player;

/**
 * The message sent from the client when a player uses magic on a ground item.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class MagicOnGroundItemPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		int x = payload.getShort(false, ByteOrder.LITTLE);
		int y = payload.getShort(false, ByteOrder.LITTLE);
		int itemId = payload.getShort(true);
		int spellId = payload.getShort(false, ByteTransform.A);
		
		if(GameServer.DEBUG) {
			player.message("item = " + itemId + ", spell = " + spellId + ", x = " + x + ", y = " + y + ".");
		}
	}
	
}
