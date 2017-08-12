package net.edge.net.packet.in;

import net.edge.GameConstants;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.entity.item.container.session.ExchangeSession;
import net.edge.world.entity.item.container.session.ExchangeSessionManager;
import net.edge.world.entity.item.container.session.impl.DuelSession;
import net.edge.world.entity.item.container.session.impl.TradeSession;
import net.edge.content.minigame.MinigameHandler;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteOrder;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;

/**
 * The message sent from the client when a player sends some sort of request to
 * another player.
 * @author lare96 <http://github.com/lare96>
 */
public final class RequestPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.REQUEST_MESSAGE))
			return;
		
		switch(opcode) {
			case 139:
				tradeRequest(player, payload);
				break;
			case 128:
				duelRequest(player, payload);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.REQUEST_MESSAGE);
	}
	
	/**
	 * Handles a trade request for {@code player}.
	 * @param player  the player to handle this for.
	 * @param payload the payloadfer for reading the sent data.
	 */
	private void tradeRequest(Player player, IncomingMsg payload) {
		int index = payload.getShort(true, ByteOrder.LITTLE);
		Player other = World.get().getPlayers().get(index - 1);
		if(GameConstants.TRADE_DISABLED) {
			player.message("Trading has been temporarily disabled!");
		}
		if(other == null || !validate(player, other))
			return;
		if(!MinigameHandler.execute(player, m -> m.canTrade(player, other)))
			return;
		ExchangeSessionManager.get().request(new TradeSession(player, other, ExchangeSession.REQUEST));
	}
	
	/**
	 * Handles a duel request for {@code player}.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void duelRequest(Player player, IncomingMsg payload) {
		int index = payload.getShort(false);
		Player other = World.get().getPlayers().get(index - 1);
		if(GameConstants.DUEL_DISABLED) {
			player.message("Duelling has been temporarily disabled!");
		}
		if(other == null || !validate(player, other))
			return;
		ExchangeSessionManager.get().request(new DuelSession(player, other, ExchangeSession.REQUEST));
	}
	
	/**
	 * Determines if {@code player} can be a valid request to {@code other}.
	 * @param player the player making the request.
	 * @param other  the player being requested.
	 * @return {@code true} if the player can make a request, {@code false}
	 * otherwise.
	 */
	private boolean validate(Player player, Player other) {
		if(!other.getPosition().isViewableFrom(player.getPosition()) || other.same(player))
			return false;
		return true;
	}
}
