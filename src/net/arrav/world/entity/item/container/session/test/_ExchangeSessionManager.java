package net.arrav.world.entity.item.container.session.test;

import net.arrav.net.host.HostManager;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.item.container.session.ExchangeSessionActionType;

import java.util.Objects;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 8-2-2018.
 */
public final class _ExchangeSessionManager {

	public final Player player;

	public _ExchangeSessionManager(Player player) {
		this.player = player;
	}

	public _ExchangeSession session;

	public Player last_requested;

	public boolean request(_ExchangeSession session) {
		Player player = session.player;
		Player requested = session.other;

		if(requested == null) {
			player.message("Couldn't request the session.");
			return false;
		}

		if(Objects.equals(player, requested)) {
			player.message("You cannot start an exchange session with yourself.");
			return false;
		}

		if(player.getRights().less(Rights.SENIOR_MODERATOR) && requested.getRights().less(Rights.SENIOR_MODERATOR)) {
			if(HostManager.same(player, requested)) {
				player.message("You can't trade over the same network.");
				return false;
			}
			if(!(player.isIronMan() && requested.isIronMan())) {
				if(player.isIronMan() && !player.isIronMaxed()) {
					if(requested.isIronMan() && player.getRights().less(Rights.ADMINISTRATOR)) {
						player.message("You cannot start an exchange session because you're an iron man member.");
						return false;
					}
				}
				if(requested.isIronMan() && !requested.isIronMaxed()) {
					if(!player.isIronMan() && player.getRights().less(Rights.ADMINISTRATOR)) {
						player.message("This player is in iron man mode and can't start an exchange session.");
						return false;
					}
				}
			}
		}

		if(inAnySession(player)) {
			reset();
			return false;
		}

		if(inAnySession(requested)) {
			player.message("This player is currently in a session with another player.");
			return false;
		}
		if(player.getTeleportStage() > 1) {
			player.message("You cannot send a trade request while you're teleporting.");
			return false;
		}
		if(requested.getTeleportStage() > 1) {
			player.message("You cannot send a trade request while the other player is teleporting.");
			return false;
		}

		if(player.exchange_manager.last_requested == requested && requested.exchange_manager.last_requested == player) {
			session.forEach(p -> {
				p.exchange_manager.session = session;
				p.exchange_manager.session.accept(p, -1);
				p.exchange_manager.last_requested = null;
			});
			return true;
		}

		player.message("Sending duel request...");
		requested.message(player.getFormatUsername() + ":duelreq:");

		player.exchange_manager.last_requested = requested;
		session.onRequest(player, requested);
		return true;
	}

	public boolean inAnySession(Player other) {
		return session != null;
	}

	public void reset() {
		Player player = session.player;
		Player other = session.other;

		session.finalize(ExchangeSessionActionType.RESTORE_ITEMS);
		session.forEach(Player::closeWidget);
	}
}
