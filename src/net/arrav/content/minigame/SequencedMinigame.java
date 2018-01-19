package net.arrav.content.minigame;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.task.Task;
import net.arrav.util.MutableNumber;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * The class that provides all of the functionality needed for minigames cannot
 * usually be ran on their own meaning they are dependent on some sort of
 * sequencer or task.
 * @author lare96 <http://github.com/lare96>
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class SequencedMinigame extends Minigame {
	
	/**
	 * The list of all players in this minigame.
	 */
	private final ObjectList<Player> players = new ObjectArrayList<>();
	
	/**
	 * The counter that conceals the tick amount.
	 */
	private final MutableNumber counter = new MutableNumber();
	
	/**
	 * The backing task chained to this sequenced minigame.
	 */
	protected Optional<SequencedMinigameTask> task = Optional.empty();
	
	/**
	 * Creates a new {@link SequencedMinigame}.
	 * @param minigame {@link #getMinigame()}.
	 * @param safety   {@link #getSafety()}.
	 */
	public SequencedMinigame(String minigame, MinigameSafety safety) {
		super(minigame, safety, MinigameType.SEQUENCED);
	}
	
	@Override
	public final void onEnter(Player player) {
		players.add(player);
		if(!task.isPresent()) {
			task = Optional.of(new SequencedMinigameTask(this));
			World.get().submit(task.get());
		}
		player.setMinigame(Optional.of(this));
		enter(player);
	}
	
	@Override
	public final void onLogin(Player player) {
		if(!task.isPresent()) {
			task = Optional.of(new SequencedMinigameTask(this));
			World.get().submit(task.get());
		}
		this.login(player);
	}
	
	@Override
	public final void onLogout(Player player) {
		this.logout(player);
	}
	
	/**
	 * Destructs the backing task for this sequenced minigame.
	 */
	public final void destruct() {
		if(!task.isPresent()) {
			return;
		}
		task.get().cancel();
	}
	
	/**
	 * The method executed when this minigame is sequenced.
	 */
	public abstract void onSequence();
	
	/**
	 * The method executed when the player enters the minigame.
	 * @param player the player whom entered the minigame.
	 */
	public abstract void enter(Player player);
	
	/**
	 * The delay interval for the sequencing of this minigame.
	 * @return the delay interval.
	 */
	public abstract int delay();
	
	/**
	 * The functionality handled when the player logs into this minigame.
	 * @param player the player this functionality is being handled for.
	 */
	public abstract void login(Player player);
	
	/**
	 * The functionality handled when the player logs out this minigame.
	 * @param player the player this functionality is being handled for.
	 */
	public abstract void logout(Player player);
	
	/**
	 * Gets the counter that conceals the tick amount.
	 * @return the tick amount.
	 */
	public final MutableNumber getCounter() {
		return counter;
	}
	
	/**
	 * Gets the players participating in this minigame.
	 * @return players in this minigame.
	 */
	public ObjectList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * The backing task chained to the {@link SequencedMinigame}.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class SequencedMinigameTask extends Task {
		
		/**
		 * The sequencer this task is running for.
		 */
		private final SequencedMinigame sequencer;
		
		/**
		 * Constructs a new {@link SequencedMinigame}.
		 * @param sequencer {@link #sequencer}.
		 */
		public SequencedMinigameTask(SequencedMinigame sequencer) {
			super(1, false);
			this.sequencer = sequencer;
		}
		
		@Override
		public void execute() {
			if(sequencer.getCounter().incrementAndGet() == sequencer.delay()) {
				sequencer.onSequence();
				sequencer.getCounter().set(0);
			}
		}
		
		@Override
		public void onCancel() {
			this.sequencer.task = Optional.empty();
		}
	}
}
