package net.edge.net.packet.in;

import net.edge.content.dialogue.impl.StatementDialogue;
import net.edge.content.market.MarketShop;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.util.TextUtils;
import net.edge.world.entity.actor.player.Player;

/**
 * The message that is sent from the client when the player searches an item in the market.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MarketPacket implements IncomingPacket {

	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		String search = TextUtils.hashToName(payload.getLong());
		if(player.isIronMan() && !player.isIronMaxed()) {
			player.getDialogueBuilder().append(new StatementDialogue("You are in iron man mode.", "Therefore you can't search the global market.", "Once you max-out you will be able to."));
			return;
		}
		new MarketShop(player, search);
	}
}
