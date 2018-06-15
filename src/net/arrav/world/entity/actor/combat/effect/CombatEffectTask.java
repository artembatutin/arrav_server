package net.arrav.world.entity.actor.combat.effect;

import net.arrav.task.Task;
import net.arrav.world.entity.EntityState;
import net.arrav.world.entity.actor.Actor;

/**
 * The {@link Task} implementation that provides processing for
 * {@link CombatEffect}s.
 * @author lare96 <http://github.org/lare96>
 */
public final class CombatEffectTask extends Task {
	
	/**
	 * The character that this task is for.
	 */
	private final Actor c;
	
	/**
	 * The combat effect that is being processed.
	 */
	private final CombatEffect effect;
	
	/**
	 * Creates a new {@link CombatEffectTask}.
	 * @param c the character that this task is for.
	 * @param effect the combat effect that is being processed.
	 */
	public CombatEffectTask(Actor c, CombatEffect effect) {
		super(effect.getDelay(), false);
		super.attach(c);
		this.c = c;
		this.effect = effect;
	}
	
	@Override
	public void execute() {
		if(effect.removeOn(c) || c.getState() != EntityState.ACTIVE) {
			this.cancel();
			return;
		}
		effect.process(c);
	}
}
