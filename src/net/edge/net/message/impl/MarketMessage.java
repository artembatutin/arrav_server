package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.message.InputMessageListener;
import net.edge.utils.TextUtils;
import net.edge.world.content.market.MarketShop;
import net.edge.world.node.entity.player.Player;

/**
 * The message that is sent from the client when the player searches an item in the market.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MarketMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		String search = TextUtils.hashToName(payload.getLong());
		new MarketShop(player, search);
	}
}
