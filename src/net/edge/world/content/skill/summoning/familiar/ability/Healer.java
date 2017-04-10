package net.edge.world.content.skill.summoning.familiar.ability;

import net.edge.world.World;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.model.Visualize;
import net.edge.task.Task;

import java.util.Optional;

/**
 * Holds functionality for the healer familiar ability.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Healer extends FamiliarAbility {

	/**
	 * The amount of ticks it takes before healing this player again.
	 */
	private final int ticks;

	/**
	 * The amount this player should be healed.
	 */
	private final int amount;

	/**
	 * Constructs a new {@link Healer}.
	 * @param ticks  {@link #ticks}.
	 * @param amount {@link #amount}.
	 */
	public Healer(int ticks, int amount) {
		super(FamiliarAbilityType.HEALER);

		this.ticks = ticks;
		this.amount = amount;
	}

	/**
	 * The task running for this healer.
	 */
	private Optional<HealerTask> task;

	@Override
	public void initialise(Player player) {
		if(task.isPresent()) {
			return;
		}
		task = Optional.of(new HealerTask(player, this));
		World.submit(task.get());
	}

	/**
	 * Any visualisation the player shows each time the player
	 * gets healed should be handled here. <b> the method should be overriden
	 * to provide functionality</b>
	 * @return the visualisation wrapped in an optional.
	 */
	public Optional<Visualize> getPlayerVisualisation() {
		return Optional.empty();
	}

	/**
	 * Any visualisation the npc shows each time the player
	 * gets healed should be handled here. <b> the method should be overriden
	 * to provide functionality</b>
	 * @return the visualisation wrapped in an optional.
	 */
	public Optional<Visualize> getNpcVisualisation() {
		return Optional.empty();
	}

	/**
	 * The task chained to this ability.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class HealerTask extends Task {

		/**
		 * The summoner of this healer.
		 */
		private final Player player;

		/**
		 * The healer this task is running for.
		 */
		private final Healer healer;

		/**
		 * Constructs a new {@link HealerTask}.
		 * @param player {@link #player}.
		 * @param healer {@link #healer}.
		 */
		public HealerTask(Player player, Healer healer) {
			super(1, false);

			this.player = player;
			this.healer = healer;
		}

		/**
		 * The current ticks this task is at.
		 */
		private int current;

		@Override
		public void onSubmit() {
			this.current = healer.ticks;
		}

		@Override
		public void execute() {
			if(!player.getFamiliar().isPresent() || player.getCurrentHealth() == player.getMaximumHealth()) {
				this.cancel();
				return;
			}
			if(player.getFamiliar().get().isDead()) {
				this.cancel();
				return;
			}

			if(--current < 1) {
				if(healer.getNpcVisualisation().isPresent()) {
					healer.getNpcVisualisation().get().play(player.getFamiliar().get());
				}
				if(healer.getPlayerVisualisation().isPresent()) {
					healer.getPlayerVisualisation().get().play(player);
				}
				player.healEntity(healer.amount);
				current = healer.ticks;
				return;
			}
		}

		@Override
		public void onCancel() {
			healer.task = Optional.empty();
		}

	}

}
