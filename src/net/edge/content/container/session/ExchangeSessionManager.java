package net.edge.content.container.session;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The class which manages all the exchange sessions on the world.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ExchangeSessionManager {
	
	/**
	 * The collection of sessions.
	 */
	private static final ObjectList<ExchangeSession> SESSIONS = new ObjectArrayList<>();
	
	/**
	 * Adds a session to the collection.
	 * @param session the session to add.
	 */
	public void add(ExchangeSession session) {
		SESSIONS.add(session);
	}
	
	/**
	 * Removes a session from the collection.
	 * @param session the session to remove.
	 */
	public void remove(ExchangeSession session) {
		SESSIONS.remove(session);
	}
	
	/**
	 * Attempts to start an exchange session.
	 * @param session the exchange session to start.
	 * @return <true> if the request was successfull, <false> otherwise.
	 */
	public boolean request(ExchangeSession session) {
		Player player = session.getPlayers().get(0);
		Player requested = session.getOther(player);
		
		if(requested == null) {
			player.message("Couldn't request the session.");
			return false;
		}
		
		if(player.isIronMan() && !player.isIronMaxed()) {
			if(requested.isIronMan()) {
				player.message("You cannot start an exchange session because you're an iron man member.");
				return false;
			}
		}
		
		if(requested.isIronMan() && !requested.isIronMaxed()) {
			if(!player.isIronMan()) {
				player.message("This player is in iron man mode and can't start an exchange session.");
				return false;
			}
		}
		
		if(inAnySession(player)) {
			session.getPlayers().forEach(this::reset);
			return false;
		}
		if(inAnySession(requested)) {
			player.message("This player is currently is a session with another player.");
			return false;
		}
		if(Objects.equals(player, requested)) {
			player.message("You cannot start an exchange session with yourself.");
			return false;
		}
		if(isAvailable(requested, player, session.getType()).isPresent()) {
			player.message("You have already sent a request to this player.");
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
		if(Objects.isNull(requested)) {
			player.message("That player cannot be found, try again shortly.");
			return false;
		}
		session.onRequest(player, requested);
		return true;
	}
	
	/**
	 * Clicks a button on the interface and executes any function it requires.
	 * @param player the player clicking the button.
	 * @param button the button being clicked.
	 * @return <true> if the function was executed, <false> otherwise.
	 */
	public boolean buttonClickAction(Player player, int button) {
		Optional<ExchangeSession> session = getExchangeSession(player);
		
		if(!session.isPresent()) {
			return false;
		}
		
		if(World.getExchangeSessionManager().containsSessionInconsistancies(player)) {
			return false;
		}
		if(!World.getExchangeSessionManager().inSession(player, session.get().getType())) {
			return false;
		}
		
		session.get().onClickButton(player, button);
		return true;
	}
	
	/**
	 * Determines if the player is a session which matches the {@code type}.
	 * @param player the player we're checking for.
	 * @return <true> if the player is, <false> otherwise.
	 */
	public boolean inSession(Player player, ExchangeSessionType type) {
		return SESSIONS.stream().anyMatch(session -> session.getType() == type && session.getPlayers().contains(player) && session.getStage() > ExchangeSession.REQUEST);
	}
	
	/**
	 * Determines if the player is <b>any</b> session.
	 * @param player the player we're checking for.
	 * @return <true> if the player is, <false> otherwise.
	 */
	public boolean inAnySession(Player player) {
		return SESSIONS.stream().anyMatch(session -> session.getPlayers().contains(player) && session.getStage() > ExchangeSession.REQUEST);
	}
	
	/**
	 * Checks if this session has any inconsistancies.
	 * @param player the player to check for.
	 * @return <true> if it does, <false> otherwise.
	 */
	public boolean containsSessionInconsistancies(Player player) {
		List<ExchangeSession> exchangeSession = SESSIONS.stream().
				filter(session -> session.getStage() > ExchangeSession.REQUEST && session.getPlayers().contains(player)).collect(Collectors.toList());
		if(exchangeSession.size() > 1) {
			exchangeSession.forEach(session -> session.finalize(ExchangeSessionActionType.DISPOSE_ITEMS));
			player.message("Trade declined and items lost, you existed in one or more trades.");
			return true;
		}
		for(ExchangeSession session : SESSIONS) {
			if(session.getPlayers().size() > ExchangeSession.PLAYER_LIMIT) {
				session.finalize(ExchangeSessionActionType.DISPOSE_ITEMS);
				player.message("Trace declined, items lost, more than two players in this trade.");
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Resets the requests the player has retrieved.
	 * @param player the player to reset this for.
	 */
	public void resetRequests(Player player) {
		SESSIONS.removeAll(Arrays.asList(SESSIONS.stream().filter(session -> session.getPlayers().contains(player) && session.getStage() == ExchangeSession.REQUEST).toArray()));
	}
	
	/**
	 * Resets all the session for the player dependant on the {@code type}.
	 * @param player the player to reset the session for.
	 */
	public void reset(Player player, ExchangeSessionType type) {
		Optional<ExchangeSession> session = SESSIONS.stream().filter(sess -> sess.getType().equals(type)).filter(sess -> sess.getPlayers().contains(player)).findAny();
		
		if(!session.isPresent()) {
			return;
		}
		
		session.get().getPlayers().forEach(p -> {
			this.finalize(p, ExchangeSessionActionType.RESTORE_ITEMS);
			this.resetRequests(p);
			p.getMessages().sendCloseWindows();
		});
		Player other = session.get().getOther(player);
		if(other != null)
			other.message("The other player has declined the session.");
		remove(session.get());
	}
	
	/**
	 * Resets all the sessions the player is in regardless of the session state.
	 * @param player the player to reset the session for.
	 */
	public boolean reset(Player player) {
		Optional<ExchangeSession> session = getExchangeSession(player);
		
		if(!session.isPresent()) {
			return false;
		}
		
		reset(player, session.get().getType());
		return true;
	}
	
	/**
	 * Checks if the session is still available.
	 * @param requester the player who requested.
	 * @param requested the player who's being requested.
	 * @param type      the session type we're checking availability for.
	 * @return an exchange session wrapped in an optional if the session is still available,
	 * {@link Optional#empty()}. otherwise.
	 */
	public Optional<ExchangeSession> isAvailable(Player requester, Player requested, ExchangeSessionType type) {
		return SESSIONS.stream().filter(def -> def.getStage() == ExchangeSession.REQUEST).
				filter(def -> def.getType().equals(type)).
				filter(def -> def.getPlayers().containsAll(Arrays.asList(requester, requested))).
				filter(def -> def.getAttachment().equals(requested)).findAny();
	}
	
	/**
	 * Checks if the player is in any exchange session.
	 * @param player the player to check for.
	 * @return an exchange session wrapped in an optional, {@link Optional#empty()} otherwise.
	 */
	public Optional<ExchangeSession> getExchangeSession(Player player) {
		return SESSIONS.stream().filter(def -> this.inAnySession(player)).filter(def -> def.getPlayers().contains(player)).findAny();
	}
	
	/**
	 * Checks if the player is in the {@code type}.
	 * @param player the player to check for.
	 * @return an exchange session wrapped in an optional, {@link Optional#empty()} otherwise.
	 */
	public Optional<ExchangeSession> getExchangeSession(Player player, ExchangeSessionType type) {
		return SESSIONS.stream().filter(def -> def.getType().equals(type)).filter(def -> def.getPlayers().contains(player)).findAny();
	}
	
	/**
	 * Finalizes the exchange session procedure for the specified {@code player}
	 * in a session.
	 * @param player the player to finalize for.
	 * @param type   the type of finalization we're appending.
	 */
	public void finalize(Player player, ExchangeSessionActionType type) {
		Optional<ExchangeSession> session = SESSIONS.stream().filter(def -> def.getPlayers().contains(player)).findAny();
		
		if(!session.isPresent()) {
			return;
		}
		
		session.get().finalize(type);
	}
	
	/**
	 * Determines if the {@code session} matches any of the sessions in the collection.
	 * @param session the session to check for.
	 * @return <true> if the session is, <false> otherwise.
	 */
	public boolean contains(ExchangeSession session) {
		return SESSIONS.contains(session);
	}
}
