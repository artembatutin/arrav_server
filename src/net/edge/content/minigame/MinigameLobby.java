package net.edge.content.minigame;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.task.Task;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * Holds functionality for minigame lobby's.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class MinigameLobby {
	
	/**
	 * The list of players in this lobby.
	 */
	private final ObjectList<Player> players = new ObjectArrayList<>();
	
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
	 * @param player the player entering.
	 */
	public abstract void onEnter(Player player);
	
	/**
	 * The functionality which should occur as soon as the player leaves the lobby.
	 * @param player the player leaving.
	 */
	public abstract void onLeave(Player player);
	
	/**
	 * The functionality which should occur as soon as the lobby's
	 * countdown timer has hit zero.
	 */
	public abstract void onStart();
	
	/**
	 * The flag which identifies if this player can enter the minigame lobby.
	 * @param player the player to check for.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public abstract boolean canEnter(Player player);
	
	/**
	 * The flag which identifies if this minigame can start as soon as the
	 * timer hits zero.
	 * @return <true> if the minigame can start, <false> otherwise.
	 */
	public abstract boolean canStart();
	
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
	 * @param current the current timing.
	 * @param t       the task running for this lobby.
	 */
	public void onCountdown(int current, Task t) {
	
	}
	
	/**
	 * Gets the players waiting in the lobby.
	 * @return players waiting.
	 */
	public ObjectList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Submits this minigame lobby.
	 */
	public final void submit(Player player) {
		if(!canEnter(player)) {
			return;
		}
		onEnter(player);
		if(!task.isPresent()) {
			task = Optional.of(new MinigameLobbyTask(this));
			World.get().submit(task.get());
		}
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
		 * Constructs a new {@link MinigameLobbyTask}.
		 * @param minigame {@link #minigame}.
		 */
		public MinigameLobbyTask(MinigameLobby minigame) {
			super(1, false);
			this.minigame = minigame;
		}
		
		/**
		 * The current ticks this task is at.
		 */
		private int current;
		
		/**
		 * Whether this lobby was executed or not.
		 */
		private boolean executed = false;
		
		@Override
		public void onSubmit() {
			this.current = minigame.restartTimer();
		}
		
		@Override
		public void execute() {
			minigame.onCountdown(current, this);
			if(--current < 1) {
				if(!minigame.canStart()) {
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
				minigame.onStart();
				minigame.task = Optional.empty();
				return;
			}
			minigame.onDestruct();
		}
		
	}
}
