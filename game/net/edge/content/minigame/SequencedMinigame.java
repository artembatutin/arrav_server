package net.edge.content.minigame;

import net.edge.task.Task;
import net.edge.util.MutableNumber;
import net.edge.World;
import net.edge.world.node.entity.player.Player;

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
		if(task.isPresent()) {
			throw new IllegalStateException("Sequenced Minigame Task is already started.");
		}
		task = Optional.of(new SequencedMinigameTask(player, this));
		World.get().submit(task.get());
		player.setMinigame(Optional.of(this));
		enter(player);
	}
	
	@Override
	public final void onLogin(Player player) {
		if(task.isPresent()) {
			throw new IllegalStateException("Sequenced Minigame Task is already started.");
		}
		task = Optional.of(new SequencedMinigameTask(player, this));
		World.get().submit(task.get());
		this.login(player);
	}
	
	@Override
	public final void onLogout(Player player) {
		this.logout(player);
		this.destruct(player);
	}
	
	/**
	 * Destructs the backing task for this sequenced minigame.
	 * @param player the player to destruct for.
	 */
	public final void destruct(Player player) {
		if(!task.isPresent()) {
			return;
		}
		task.get().cancel();
	}
	
	/**
	 * The method executed when this minigame is sequenced.
	 * @param player the player whom this sequencer is running for.
	 */
	public abstract void onSequence(Player player);
	
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
	 * The backing task chained to the {@link SequencedMinigame}.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class SequencedMinigameTask extends Task {
		
		/**
		 * The player this task is running for.
		 */
		private final Player player;
		
		/**
		 * The sequencer this task is running for.
		 */
		private final SequencedMinigame sequencer;
		
		/**
		 * Constructs a new {@link SequencedMinigame}.
		 * @param sequencer {@link #sequencer}.
		 */
		public SequencedMinigameTask(Player player, SequencedMinigame sequencer) {
			super(1, false);
			this.player = player;
			this.sequencer = sequencer;
		}
		
		@Override
		public void execute() {
			if(sequencer.getCounter().incrementAndGet() == sequencer.delay()) {
				sequencer.onSequence(player);
				sequencer.getCounter().set(0);
			}
		}
		
		@Override
		public void onCancel() {
			this.sequencer.task = Optional.empty();
		}
	}
}
