package net.edge.net.packet.in;

import io.netty.buffer.ByteBuf;
import net.edge.Application;
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
	public void handle(Player player, int opcode, int size, ByteBuf buf) {
		int x = buf.getShort(false, ByteOrder.LITTLE);
		int y = buf.getShort(false, ByteOrder.LITTLE);
		int itemId = buf.getShort(true);
		int spellId = buf.getShort(false, ByteTransform.A);
		
		if(Application.DEBUG) {
			player.message("item = " + itemId + ", spell = " + spellId + ", x = " + x + ", y = " + y + ".");
		}
	}
	
}
