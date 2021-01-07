package com.rageps.world.entity.actor.mob.impl;

import com.rageps.combat.strategy.npc.boss.KalphiteQueenStrategy;
import com.rageps.task.LinkedTaskSequence;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.mob.MobAggression;
import com.rageps.world.locale.Position;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 29-6-2017.
 */
public final class KalphiteQueen extends Mob {
	
	private Phase phase;
	
	/**
	 * Creates a new {@link Mob}.
	 * @param position the position of this character in the world.
	 */
	public KalphiteQueen(Position position) {
		super(Phase.PHASE_ONE.npcId, position);
		MobAggression.AGGRESSIVE.add(this.getId());
		this.setPhase(Phase.PHASE_ONE);
		this.setStrategy(new KalphiteQueenStrategy());
	}
	
	@Override
	public void appendDeath() {
		switch(getPhase()) {
			case PHASE_ONE:
				setDead(true);
				int deathAnimation = getDefinition().getDeathAnimation();
				this.animation(new Animation(deathAnimation, Animation.AnimationPriority.HIGH));
				LinkedTaskSequence sequence = new LinkedTaskSequence();
				sequence.connect(3, () -> {
					this.setCurrentHealth(getMaxHealth());
					this.transform(Phase.PHASE_TWO.npcId);
					this.setPhase(Phase.PHASE_TWO);
					this.graphic(new Graphic(1055));
					this.animation(new Animation(6270, Animation.AnimationPriority.HIGH));
				});
				sequence.connect(14, () -> {
					this.setDead(false);
					Actor victim = this.getCombat().getDefender();
					if(victim != null && victim.getPosition().withinDistance(this.getPosition(), 12) && !victim.isDead()) {
						this.getCombat().attack(victim);
					}
				});
				sequence.start();
				break;
			case PHASE_TWO:
				this.setRespawn(true);
				super.appendDeath();
				break;
		}
	}
	
	/**
	 * Creates the particular {@link Mob instance}.
	 * @return new {@link Mob} instance.
	 */
	@Override
	public Mob create() {
		return new KalphiteQueen(getPosition());
	}
	
	/**
	 * The current phase of the kalphite queen.
	 */
	public Phase getPhase() {
		return phase;
	}
	
	public void setPhase(Phase phase) {
		this.phase = phase;
	}
	
	/**
	 * The enumerated type whose elements represent a set of constants used to define
	 * information between the phases.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 * @since 29-6-2017.
	 */
	public enum Phase {
		PHASE_ONE(1158), PHASE_TWO(1160);
		
		/**
		 * The npc id of this phase.
		 */
		public final int npcId;
		
		/**
		 * Constructs a new {@link Phase}.
		 * @param npcId {@link #npcId}.
		 */
		Phase(int npcId) {
			this.npcId = npcId;
		}
	}
}
