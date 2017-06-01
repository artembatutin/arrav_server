package net.edge.content.minigame;

import net.edge.task.Task;
import net.edge.util.MutableNumber;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Holds functionality for minigame lobby's.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class MinigameLobby {
	
	/**
	 * The list of players in this lobby.
	 */
	private final List<Player> players = new ArrayList<Player>();
	
	/**
	 * The timer for this minigame lobby.
	 */
	private final int timer;
	
	/**
	 * Constructs a new {@link MinigameLobby}.
	 * @param timer the timer chained to this minigame lobby.
	 */
	public MinigameLobby(int timer) {
		this.timer = timer;
	}
	
	/**
	 * The minigame lobby task chained to this minigame.
	 */
	private Optional<MinigameLobbyTask> task = Optional.empty();
	
	/**
	 * The functionality which should occur as soon as the player enters
	 * the lobby.
	 */
	public abstract void onEnter(Player player);
	
	/**
	 * The functionality which should occur as soon as the lobby's
	 * countdown timer has hit zero.
	 * @param player the player to handle the functionality for.
	 */
	public abstract void onStart(List<Player> player);
	
	/**
	 * The flag which identifies if this player can enter the minigame lobby.
	 * @param player the player to check for.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public abstract boolean canEnter(Player player);
	
	/**
	 * The flag which identifies if this minigame can start as soon as the
	 * timer hits zero.
	 * @param players the players in the lobby.
	 * @return <true> if the minigame can start, <false> otherwise.
	 */
	public abstract boolean canStart(List<Player> players);
	
	/**
	 * The restart timer if this lobby can't start.
	 * @return the numerical value for the restart timer.
	 */
	public abstract int restartTimer();
	
	/**
	 * The functionality which should be handled as soon as the lobby
	 * is abandoned.
	 */
	public abstract void onDestruct();
	
	/**
	 * Any functionality that should occur while the task is running.
	 * @param player the player in the lobby.
	 * @param t      the task running for this lobby.
	 */
	public void onCountdown(List<Player> player, Task t) {
		
	}
	
	/**
	 * Submits this minigame lobby.
	 */
	public final void submit(Player player) {
		if(task.isPresent()) {
			throw new IllegalStateException("Minigame lobby has already started.");
		}
		
		if(!canEnter(player)) {
			return;
		}
		
		task = Optional.of(new MinigameLobbyTask(this));
		World.submit(task.get());
	}
	
	/**
	 * The backing task which is running for this minigame lobby.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class MinigameLobbyTask extends Task {
		
		/**
		 * The minigame this lobby is active for.
		 */
		private final MinigameLobby minigame;
		
		/**
		 * The players this task is running for.
		 */
		private final List<Player> players;
		
		/**
		 * Constructs a new {@link MinigameLobbyTask}.
		 * @param minigame {@link #minigame}.
		 */
		public MinigameLobbyTask(MinigameLobby minigame) {
			super(1, false);
			this.minigame = minigame;
			this.players = minigame.players;
		}
		
		/**
		 * The current ticks this task is at.
		 */
		private MutableNumber current;
		
		/**
		 * Whether this lobby was executed or not.
		 */
		private boolean executed = false;
		
		@Override
		public void onSubmit() {
			this.current.set(minigame.timer);
		}
		
		@Override
		public void execute() {
			if(current.decrementAndGet() < 1) {
				if(!minigame.canStart(players)) {
					this.setDelay(minigame.restartTimer());
					return;
				}
				executed = true;
				this.cancel();
				return;
			}
			
			if(minigame.players.isEmpty()) {
				this.cancel();
				return;
			}
		}
		
		@Override
		public void onCancel() {
			if(executed) {
				minigame.onStart(players);
				minigame.task = Optional.empty();
				return;
			}
			minigame.onDestruct();
		}
		
	}
}
