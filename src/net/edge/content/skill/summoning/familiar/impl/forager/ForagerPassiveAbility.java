package net.edge.content.skill.summoning.familiar.impl.forager;

import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.content.skill.summoning.familiar.ability.Forager;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the passve forager ability.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class ForagerPassiveAbility extends Forager {
	
	/**
	 * The cached instance of the task to make sure it's not running twice.
	 */
	private Optional<ForagerTask> task = Optional.empty();
	
	/**
	 * Constructs a new {@link ForagerPassiveAbility}.
	 * @param collectables the collectables this forager can hold.
	 */
	public ForagerPassiveAbility(Item... collectables) {
		super(collectables);
	}
	
	/**
	 * Constructs a new {@link ForagerPassiveAbility}.
	 * @param collectables the collectables this forager can hold.
	 */
	public ForagerPassiveAbility(int... collectables) {
		super(collectables);
	}
	
	@Override
	public boolean canForage(Player player) {
		return true;
	}
	
	@Override
	public boolean canWithdraw(Player player, Item item) {
		return true;
	}
	
	@Override
	public void onWithdraw(Player player) {
		// can be overriden.
	}
	
	@Override
	public void initialise(Player player) {
		if(getTask().isPresent()) {
			return;
		}
		setTask(Optional.of(new ForagerTask(player, this)));
		World.get().submit(getTask().get());
	}
	
	/**
	 * The task chained to this ability type which will add items
	 * to the container.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class ForagerTask extends Task {
		
		/**
		 * The player this task is running for.
		 */
		private final Player player;
		
		/**
		 * The forager this task is running for.
		 */
		private final ForagerPassiveAbility forager;
		
		/**
		 * Constructs a new {@link ForagerTask}.
		 * @param player  {@link #player}.
		 * @param forager {@link #forager}.
		 */
		public ForagerTask(Player player, ForagerPassiveAbility forager) {
			super(1, false);
			
			this.player = player;
			this.forager = forager;
		}
		
		@Override
		public void onSequence() {
			if(!player.getFamiliar().isPresent()) {
				this.cancel();
				return;
			}
		}
		
		@Override
		public void execute() {
			if(!getForager().canForage(player)) {
				this.setDelay(RandomUtils.inclusive(10, 20));
				return;
			}
			if(getForager().getContainer().remaining() < 1) {
				player.message("Your familiar's inventory is currently full.");
				this.setDelay(RandomUtils.inclusive(10, 20));
				return;
			}
			getForager().setProduced(getForager().getCollectables()[RandomUtils.inclusive(getForager().getCollectables().length - 1)]);
			if(getForager().getContainer().canAdd(getForager().getProduced())) {
				getForager().getContainer().add(getForager().getProduced());
				this.getForager().onStore(player, getForager().getProduced());
				this.setDelay(RandomUtils.inclusive(100, 200));
			}
		}
		
		@Override
		public void onCancel() {
			getForager().setTask(Optional.empty());
		}
		
		/**
		 * @return the forager
		 */
		public ForagerPassiveAbility getForager() {
			return forager;
		}
	}
	
	/**
	 * @return the task
	 */
	public Optional<ForagerTask> getTask() {
		return task;
	}
	
	/**
	 * @param task the task to set
	 */
	public void setTask(Optional<ForagerTask> task) {
		this.task = task;
	}
}
