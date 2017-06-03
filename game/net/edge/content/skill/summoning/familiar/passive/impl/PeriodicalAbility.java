package net.edge.content.skill.summoning.familiar.passive.impl;

import net.edge.task.Task;
import net.edge.World;
import net.edge.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.node.entity.player.Player;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * The periodical ability which executes functionality every period, an example
 * of where this should be utilized is the bunyip familiar.
 * Which heals the player every 15 ticks, aslong as the familiar is summoned.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class PeriodicalAbility implements PassiveAbility {

	/**
	 * The periodical rate in ticks the ability gets executed at.
	 */
	private final int ticks;

	/**
	 * The consumer for this combat ability action.
	 */
	private final Consumer<Player> action;

	/**
	 * Constructs a new {@link PeriodicalAbility}.
	 * @param ticks  {@link #ticks}.
	 * @param action {@link #action}.
	 */
	public PeriodicalAbility(int ticks, Consumer<Player> action) {
		this.ticks = ticks;
		this.action = action;
	}

	/**
	 * The task chained to this ability.
	 */
	private Optional<PeriodicalAbilityTask> task = Optional.empty();

	/**
	 * Checks if this player can perform the ability.
	 * @param player the player we're checking.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public boolean canPerform(Player player) {
		//overriding purposes.
		return true;
	}

	@Override
	public final boolean canExecute(Player player) {
		if(!canPerform(player)) {
			return false;
		}
		return true;
	}

	@Override
	public final void onExecute(Player player) {
		if(task.isPresent()) {
			return;
		}
		task = Optional.of(new PeriodicalAbilityTask(this, player));
		World.get().submit(task.get());
	}

	@Override
	public final PassiveAbilityType getPassiveAbilityType() {
		return PassiveAbilityType.PERIODICAL;

	}

	/**
	 * The task this {@link PeriodicalAbility} is independant of.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class PeriodicalAbilityTask extends Task {

		/**
		 * The ability this task is handling.
		 */
		private final PeriodicalAbility ability;

		/**
		 * The player this ability is for.
		 */
		private final Player player;

		/**
		 * Constructs a new {@link PeriodicalAbilityTask}.
		 * @param ability {@link #ability}.
		 * @param player  {@link #player}.
		 */
		public PeriodicalAbilityTask(PeriodicalAbility ability, Player player) {
			super(1, false);

			this.ability = ability;
			this.player = player;
		}

		/**
		 * The current ticks this task is at.
		 */
		private int currentTicks;

		@Override
		public void onSubmit() {
			this.currentTicks = ability.ticks;
		}

		@Override
		public void execute() {
			if(!player.getFamiliar().isPresent()) {
				this.cancel();
				return;
			}
			if(!ability.canExecute(player)) {
				return;
			}
			if(currentTicks-- < 1) {
				ability.action.accept(player);
				this.currentTicks = ability.ticks;
				return;
			}
		}
	}

}
