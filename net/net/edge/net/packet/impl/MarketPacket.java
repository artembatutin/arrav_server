package net.edge.net.packet.impl;

import net.edge.content.market.MarketShop;
import net.edge.net.codec.ByteMessage;
import net.edge.net.packet.PacketReader;
import net.edge.util.TextUtils;
import net.edge.world.node.entity.player.Player;

/**
 * The message that is sent from the client when the player searches an item in the market.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MarketPacket implements PacketReader {
	
	@Override
	public void handle(Player player, int opcode, int size, ByteMessage payload) {
		String search = TextUtils.hashToName(payload.getLong());
		new MarketShop(player, search);
	}
}