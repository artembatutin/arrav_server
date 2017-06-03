package net.edge.content.combat;

import com.google.common.base.Preconditions;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.special.CombatSpecial;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Hit;
import net.edge.world.node.entity.player.Player;

/**
 * The container that holds data for an entire combat session attack.
 * @author lare96 <http://github.com/lare96>
 */
public class CombatSessionData {
	
	/**
	 * The attacker in this combat session.
	 */
	private final EntityNode attacker;
	
	/**
	 * The victim in this combat session.
	 */
	private final EntityNode victim;
	
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
	 * The flag that determines if at least one hit is accurate.
	 */
	private boolean accurate;
	
	/**
	 * Tick delay which carries on inbetween hits hence
	 * If delay = 2, The first hit will be instant, second hit will have a delay of 2, third hit will have a delay of 4 and so on..
	 * First hit is instant due to the calculateHit method
	 */
	private final int delay;
	
	/**
	 * Determines if this hit should be ignored.
	 */
	private boolean ignore;
	
	/**
	 * Creates a new {@link CombatSessionData}.
	 * @param attacker      the attacker in this combat session.
	 * @param victim        the victim in this combat session.
	 * @param amount        the amount of hits to calculate.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 */
	public CombatSessionData(EntityNode attacker, EntityNode victim, int amount, CombatType type, boolean checkAccuracy) {
		this.attacker = attacker;
		this.victim = victim;
		this.type = type;
		this.checkAccuracy = checkAccuracy;
		this.delay = 0;
		this.hits = calculateHits(amount);
		this.experience = determineExperience();
	}
	
	public CombatSessionData(EntityNode attacker, EntityNode victim, int amount, CombatType type, boolean checkAccuracy, int delay) {
		this.attacker = attacker;
		this.victim = victim;
		this.type = type;
		this.checkAccuracy = checkAccuracy;
		this.delay = delay;
		this.hits = calculateHits(amount);
		this.experience = determineExperience();
	}
	
	/**
	 * Creates a new {@link CombatSessionData} with an {@code amount} of
	 * {@code 0}.
	 * @param attacker      the attacker in this combat session.
	 * @param victim        the victim in this combat session.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 */
	public CombatSessionData(EntityNode attacker, EntityNode victim, CombatType type, boolean checkAccuracy) {
		this(attacker, victim, 0, type, checkAccuracy);
	}
	
	/**
	 * Calculates all of the hits that will be dealt before the attack is
	 * launched.
	 * @param amount the amount of hits to calculate, with minimum of {@code 0} and
	 *               a maximum of {@code 1}.
	 * @return an array of the calculated hits.
	 */
	private Hit[] calculateHits(int amount) {
		Preconditions.checkArgument(amount >= 0 && amount <= 4);
		this.getAttacker().getCombatBuilder().resetAttackTimer();
		// No hit for this turn, but we still need to calculate accuracy.
		if(amount == 0) {
			accurate = !checkAccuracy || Combat.isAccurate(attacker, victim, type);
			return new Hit[]{};
		}
		
		// Create the new array of hits, and populate it. Here we do the maximum
		// hit and accuracy calculations.
		Hit[] array = new Hit[amount];
		for(int i = 0; i < array.length; i++) {
			array[i] = Combat.calculateRandomHit(attacker, victim, type, i == 0 ? 0 : (delay * (i * 2)), checkAccuracy);
			if(array[i].isAccurate())
				accurate = true;
		}
		
		// special attacks
		if(getAttacker().isPlayer() && getAttacker().toPlayer().isSpecialActivated()) {
			Player player = getAttacker().toPlayer();
			if(player.getCombatSpecial().equals(CombatSpecial.DRAGON_CLAWS) && amount == 4) {
				int first = array[0].getDamage();
				if(first > 360) {
					first = 360 + RandomUtils.inclusive(10);
				}
				int second = first <= 0 ? array[1].getDamage() : first / 2;
				int third = first <= 0 && second > 0 ? second / 2 : first <= 0 && second <= 0 ? array[2].getDamage() : RandomUtils.inclusive(second);
				int fourth = first <= 0 && second <= 0 && third <= 0 ? array[3].getDamage() + RandomUtils.inclusive(7) : first <= 0 && second <= 0 ? array[3].getDamage() : third;
				array[0].setDamage(first);
				array[1].setDamage(second);
				array[2].setDamage(third);
				array[3].setDamage(fourth);
			}
		}
		return array;
	}
	
	/**
	 * Launches all of the damage concealed within this container.
	 * @param ignored the flag which determines if the hit should be ignored on the victim.
	 * @return the amount of damage that was dealt.
	 */
	public final int attack(boolean ignored) {
		int counter = 0;
		int index = 0;
		Hit[] container = new Hit[hits.length];
		for(Hit hit : hits) {
			if(!hit.isAccurate()) {
				hit = new Hit(0);
				hit.setAccurate(false);
			}
			counter += hit.getDamage();
			container[index++] = hit;
		}
		if(hits.length > 0 && victim != null && !victim.isDead() && !ignored) {
			victim.damage(container);
		}
		Combat.handleExperience(attacker.getCombatBuilder(), this, counter);
		return counter;
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
	public CombatSessionData preAttack() {
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
	public final EntityNode getAttacker() {
		return attacker;
	}
	
	/**
	 * Gets the victim in this combat session.
	 * @return the victim.
	 */
	public final EntityNode getVictim() {
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
	 * Sets the value for {@link CombatSessionData#accurate}.
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
