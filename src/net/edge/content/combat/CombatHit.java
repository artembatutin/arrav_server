package net.edge.content.combat;

import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.special.CombatSpecial;
import net.edge.world.node.actor.Actor;
import net.edge.world.Hit;
import net.edge.world.node.actor.player.Player;

import java.util.OptionalInt;

/**
 * The container that holds data for an entire combat session attack.
 * @author lare96 <http://github.com/lare96>
 */
public class CombatHit {
	
	/**
	 * The attacker in this combat session.
	 */
	private final Actor attacker;
	
	/**
	 * The victim in this combat session.
	 */
	private final Actor victim;
	
	/**
	 * The hits that will be sent when the attacker attacks.
	 */
	private final Hit[] hits;
	
	/**
	 * The skills that experience will be given to.
	 */
	private final int[] experience;
	
	/**
	 * The combat type the attacker is using.
	 */
	private final CombatType type;
	
	/**
	 * The flag that determines if accuracy should be taken into account.
	 */
	private final boolean checkAccuracy;
	
	/**
	 * Total damage dealt by this data.
	 */
	private final int damage;
	
	/**
	 * The flag that determines if at least one hit is accurate.
	 */
	private boolean accurate;
	
	/**
	 * Tick delay which carries on inbetween hits hence
	 * If delay = 2, The first hit will be instant, second hit will have a delay of 2, third hit will have a delay of 4 and so on..
	 * First hit is instant due to the calculateHit method
	 */
	public final OptionalInt delay;
	
	/**
	 * Determines if this hit should be ignored.
	 */
	private boolean ignore;
	
	/**
	 * Creates a new {@link CombatHit}.
	 * @param attacker      the attacker in this combat session.
	 * @param victim        the victim in this combat session.
	 * @param amount        the amount of hits to calculate.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 * @param delay         the delay of the hit.
	 */
	public CombatHit(Actor attacker, Actor victim, int amount, CombatType type, boolean checkAccuracy, OptionalInt delay) {
		this.attacker = attacker;
		this.victim = victim;
		this.type = type;
		this.checkAccuracy = checkAccuracy;
		this.delay = delay;
		this.experience = determineExperience();
		
		this.getAttacker().getCombatBuilder().resetAttackTimer();
		int counter = 0;
		// No hit for this turn, but we still need to calculate accuracy.
		if(amount == 0) {
			accurate = !checkAccuracy || Combat.isAccurate(attacker, victim, type);
			this.hits = new Hit[]{};
			this.damage = 0;
		} else {
			// Create the new array of hits, and populate it. Here we do the maximum
			// hit and accuracy calculations.
			this.hits = new Hit[amount];
			for(int i = 0; i < hits.length; i++) {
				hits[i] = Combat.calculateRandomHit(attacker, victim, type, 0/**i == 0 ? 0 : (delay * (i * 2))*/, checkAccuracy);
				if(hits[i].isAccurate()) {
					accurate = true;
					counter += hits[i].getDamage();
				}
			}
			
			// dragon claws special attack
			if(getAttacker().isPlayer() && getAttacker().toPlayer().isSpecialActivated()) {
				Player player = getAttacker().toPlayer();
				if(player.getCombatSpecial().equals(CombatSpecial.DRAGON_CLAWS) && amount == 4) {
					int first = hits[0].getDamage();
					if(first > 360) {
						first = 360 + RandomUtils.inclusive(10);
					}
					int second = first <= 0 ? hits[1].getDamage() : first / 2;
					int third = first <= 0 && second > 0 ? second / 2 : first <= 0 && second <= 0 ? hits[2].getDamage() : RandomUtils.inclusive(second);
					int fourth = first <= 0 && second <= 0 && third <= 0 ? hits[3].getDamage() + RandomUtils.inclusive(7) : first <= 0 && second <= 0 ? hits[3].getDamage() : third;
					hits[0].setDamage(first);
					hits[1].setDamage(second);
					hits[2].setDamage(third);
					hits[3].setDamage(fourth);
					if(hits[0].isAccurate())
						counter += hits[0].getDamage();
					if(hits[1].isAccurate())
						counter += hits[1].getDamage();
					if(hits[2].isAccurate())
						counter += hits[2].getDamage();
					if(hits[3].isAccurate())
						counter += hits[3].getDamage();
				}
			}
			this.damage = counter;
		}
	}
	
	public CombatHit(Actor attacker, Actor victim, int amount, CombatType type, boolean checkAccuracy) {
		this(attacker, victim, amount, type, checkAccuracy, OptionalInt.empty());
	}
	
	public CombatHit(Actor attacker, Actor victim, int amount, CombatType type, boolean checkAccuracy, int delay) {
		this(attacker, victim, amount, type, checkAccuracy, OptionalInt.of(delay));
	}
	
	public CombatHit(Actor attacker, Actor victim, CombatType type, boolean checkAccuracy) {
		this(attacker, victim, 0, type, checkAccuracy);
	}
	
	/**
	 * Launches all of the damage concealed within this container.
	 * @return the amount of damage that was dealt.
	 */
	public final int attack() {
		CombatHitTask.applyPrayerEffects(this);
		int index = 0;
		Hit[] container = new Hit[hits.length];
		for(Hit hit : hits) {
			if(!hit.isAccurate()) {
				hit = new Hit(0);
				hit.setAccurate(false);
			}
			container[index++] = hit;
		}
		if(hits.length > 0 && victim != null && !victim.isDead() && !isIgnored()) {
			if(attacker.isPlayer()) {
				Player p = attacker.toPlayer();
				p.getMinigame().ifPresent(m -> m.onInflictDamage(p, victim, container));
			}
			victim.damage(container);
		}
		return damage;
	}
	
	public void experience() {
		Combat.handleExperience(attacker.getCombatBuilder(), this, damage);
	}
	
	/**
	 * Determines which skills will be given experience based on the combat
	 * type.
	 * @return an array of skills that will be given experience for this attack.
	 */
	private int[] determineExperience() {
		return attacker.isNpc() ? new int[]{} : attacker.toPlayer().getFightType().getStyle().skills(type);
	}
	
	/**
	 * Ignores this combat session hit.
	 */
	public void ignore() {
		this.ignore = true;
	}
	
	/**
	 * Determines if this combat session data should be ignored.
	 * @return {@code ignore}.
	 */
	public boolean isIgnored() {
		return ignore;
	}
	
	/**
	 * The method that can be overridden to do any last second modifications to
	 * the container before hits are dealt to the victim.
	 * @return the modified combat session data container.
	 */
	public CombatHit preAttack() {
		return this;
	}
	
	/**
	 * The method that can be overridden to do any post-attack effects to the
	 * victim. Do <b>not</b> reset the combat builder here!
	 * @param counter the amount of damage this attack inflicted, always {@code 0}
	 *                if the attack was inaccurate.
	 */
	public void postAttack(int counter) {
		
	}
	
	/**
	 * Gets the attacker in this combat session.
	 * @return the attacker.
	 */
	public final Actor getAttacker() {
		return attacker;
	}
	
	/**
	 * Gets the victim in this combat session.
	 * @return the victim.
	 */
	public final Actor getVictim() {
		return victim;
	}
	
	/**
	 * Determines if at least one hit in this container is accurate.
	 * @return {@code true} if one hit is accurate, {@code false} otherwise.
	 */
	public final boolean isAccurate() {
		return accurate;
	}
	
	/**
	 * Sets the value for {@link CombatHit#accurate}.
	 * @param accurate the new value to set.
	 */
	public final void setAccurate(boolean accurate) {
		this.accurate = accurate;
	}
	
	/**
	 * Gets the hits that will be sent when the attacker attacks.
	 * @return the hits that will be sent.
	 */
	public final Hit[] getHits() {
		return hits;
	}
	
	/**
	 * Gets the skills that experience will be given to.
	 * @return the skills for experience.
	 */
	public final int[] getExperience() {
		return experience;
	}
	
	/**
	 * Gets the combat type the attacker is using.
	 * @return the combat type.
	 */
	public final CombatType getType() {
		return type;
	}
	
	/**
	 * Determines if accuracy should be taken into account.
	 * @return {@code true} if accuracy should be calculated, {@code false}
	 * otherwise.
	 */
	public final boolean isCheckAccuracy() {
		return checkAccuracy;
	}
}
