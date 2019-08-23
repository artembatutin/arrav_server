package net.arrav.net.packet.in;

import net.arrav.net.codec.game.GamePacket;
import net.arrav.Arrav;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.entity.actor.player.Player;

/**
 * The message sent from the client when a player uses magic on a ground item.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class MagicOnGroundItemPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		int x = buf.getShort(false, ByteOrder.LITTLE);
		int y = buf.getShort(false, ByteOrder.LITTLE);
		int itemId = buf.getShort(true);
		int spellId = buf.getShort(false, ByteTransform.A);
		
		if(Arrav.DEBUG) {
			player.message("item = " + itemId + ", spell = " + spellId + ", x = " + x + ", y = " + y + ".");
		}
	}
	
}
