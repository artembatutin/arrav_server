package com.rageps.net.packet.in;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;

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
		
		if(World.get().getEnvironment().isDebug()) {
			player.message("item = " + itemId + ", spell = " + spellId + ", x = " + x + ", y = " + y + ".");
		}
	}
	
}
