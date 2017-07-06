package net.edge.net.session;

import net.edge.world.World;
import net.edge.world.node.entity.player.Player;

/**
 * A {@link Session} implementation that mimics networking for a bot, aka not human {@link Player}.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class BotSession extends GameSession {
	
	/**
	 * The player assigned to this {@code GameSession}.
	 */
	private final Player player;
	
	/**
	 * Creates a new {@link BotSession}.
	 */
	public BotSession(Player player) {
		super(player, null, null, null);
		this.player = player;
	}
	
	@Override
	public void dispose() {
		onDispose();
	}
	
	@Override
	public void onDispose() {
		World.get().queueLogout(player);
	}
	
	@Override
	public void handleUpstreamMessage(Object msg) {
	
	}
	
	@Override
	public void flushQueue() {

	}
}