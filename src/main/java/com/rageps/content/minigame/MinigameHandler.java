package com.rageps.content.minigame;

import com.google.common.collect.ImmutableSet;
import com.rageps.content.minigame.barrows.BarrowsMinigame;
import com.rageps.content.minigame.warriorsguild.impl.AnimationRoom;
import com.rageps.content.minigame.warriorsguild.impl.CyclopsRoom;
import com.rageps.world.entity.actor.player.Player;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The class that contains methods to handle the functionality of minigames.
 * @author lare96 <http://github.com/lare96>
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class MinigameHandler {
	
	/**
	 * Minigames that should be saved should be stored in this collection.
	 */
	public static final ImmutableSet<Minigame> STATIC_MINIGAMES = ImmutableSet.of(new AnimationRoom(), new CyclopsRoom(), new BarrowsMinigame());
	
	/**
	 * The method that executes {@code action} for {@code player}.
	 * @param player the player to execute the action for.
	 * @param action the backed minigame action to execute.
	 */
	public static void executeVoid(Player player, Consumer<Minigame> action) {
		Optional<Minigame> minigame = getMinigame(player);
		minigame.ifPresent(action::accept);
	}
	
	/**
	 * The method that executes {@code function} for {@code player} that returns
	 * a result.
	 * @param player the player to execute the function for.
	 * @param defaultValue the default value to return if the player isn't in a minigame.
	 * @param function the function to execute that returns a result.
	 */
	public static boolean execute(Player player, Function<Minigame, Boolean> function) {
		Optional<Minigame> minigame = getMinigame(player);
		if(!minigame.isPresent()) {
			return true;
		}
		return function.apply(minigame.get());
	}
	
	/**
	 * Determines if {@code player} is in any minigame.
	 * @param player the player to determine this for.
	 * @return <true> if the player is in a minigame, <false> otherwise.
	 */
	public static boolean contains(Player player) {
		return getMinigame(player).isPresent();
	}
	
	/**
	 * Retrieves the minigame that {@code player} is currently in.
	 * @param player the player to determine the minigame for.
	 * @return the minigame that the player is currently in.
	 */
	public static Optional<Minigame> getMinigame(Player player) {
		Optional<Minigame> minigame = player.getMinigame();
		
		if(minigame.isPresent() && minigame.get().contains(player)) {
			return minigame;
		}
		
		if(minigame.isPresent() && !minigame.get().contains(player)) {
			minigame.get().onDestruct(player);
			return Optional.empty();
		}
		
		Optional<Minigame> staticMinigame = STATIC_MINIGAMES.stream().filter(m -> m.contains(player)).findAny();
		
		if(staticMinigame.isPresent() && staticMinigame.get().contains(player)) {
			return staticMinigame;
		}
		
		if(staticMinigame.isPresent() && !staticMinigame.get().contains(player)) {
			staticMinigame.get().onDestruct(player);
			return Optional.empty();
		}
		
		return Optional.empty();
	}
}
