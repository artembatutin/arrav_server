package com.rageps.net.packet.in;

import com.rageps.world.entity.item.container.session.test.impl.TradeSession;
import com.rageps.GameConstants;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.container.session.ExchangeSession;
import com.rageps.world.entity.item.container.session.ExchangeSessionManager;
import com.rageps.world.entity.item.container.session.impl.DuelSession;

/**
 * The message sent from the client when a player sends some sort of request to
 * another player.
 * @author lare96 <http://github.com/lare96>
 */
public final class RequestPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.REQUEST_MESSAGE))
			return;
		
		switch(opcode) {
			case 139:
				tradeRequest(player, buf);
				break;
			case 128:
				duelRequest(player, buf);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.REQUEST_MESSAGE);
	}
	
	/**
	 * Handles a trade request for {@code player}.
	 * @param player the player to handle this for.
	 * @param buf the payload buffer for reading the sent data.
	 */
	private void tradeRequest(Player player, GamePacket buf) {
		int index = buf.getShort(true, ByteOrder.LITTLE);
		Player other = World.get().getPlayers().get(index - 1);
		if(GameConstants.TRADE_DISABLED) {
			player.message("Trading has been temporarily disabled!");
		}
		if(other == null || !validate(player, other))
			return;
		if(!MinigameHandler.execute(player, m -> m.canTrade(player, other)))
			return;
		//ExchangeSessionManager.get().request(new TradeSession(player, other, ExchangeSession.REQUEST));
		player.exchange_manager.request(new TradeSession(player, other));
	}
	
	/**
	 * Handles a duel request for {@code player}.
	 * @param player the player to handle this for.
	 * @param buf the payload buffer for reading the sent data.
	 */
	private void duelRequest(Player player, GamePacket buf) {
		int index = buf.getShort(false);
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
	 * @param other the player being requested.
	 * @return {@code true} if the player can make a request, {@code false}
	 * otherwise.
	 */
	private boolean validate(Player player, Player other) {
		if(!other.getPosition().isViewableFrom(player.getPosition()) || other.same(player))
			return false;
		return true;
	}
}
