package net.arrav.net.packet.in;

import io.netty.buffer.ByteBuf;
import net.arrav.content.dialogue.impl.StatementDialogue;
import net.arrav.content.market.MarketShop;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.util.TextUtils;
import net.arrav.world.entity.actor.player.Player;

/**
 * The message that is sent from the client when the player searches an item in the market.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MarketPacket implements IncomingPacket {

	@Override
	public void handle(Player player, int opcode, int size, ByteBuf buf) {
		String search = TextUtils.hashToName(buf.getLong());
		if(player.isIronMan() && !player.isIronMaxed()) {
			player.getDialogueBuilder().append(new StatementDialogue("You are in iron man mode.", "Therefore you can't search the global market.", "Once you max-out you will be able to."));
			return;
		}
		new MarketShop(player, search);
	}
}
