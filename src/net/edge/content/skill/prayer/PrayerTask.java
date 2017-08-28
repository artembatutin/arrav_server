package net.edge.content.skill.prayer;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import net.edge.content.skill.Skills;
import net.edge.task.Task;
import net.edge.world.entity.actor.combat.CombatConstants;
import net.edge.world.entity.actor.player.Player;

/**
 * A task that periodically drains prayer points for {@link Player}s. It
 * utilizes the same formula to used on actual Runescape.
 * @author lare96 <http://github.org/lare96>
 */
public final class PrayerTask extends Task {
	
	/**
	 * The player to drain prayer points.
	 */
	private final Player player;
	
	/**
	 * A set containing the tick counts for all activated {@link Prayer}s.
	 */
	private final Multiset<Prayer> counter = HashMultiset.create();
	
	/**
	 * Creates a new {@link PrayerTask}.
	 * @param player the player to drain prayer points.
	 */
	public PrayerTask(Player player) {
		super(1, false);
		super.attach(player);
		this.player = player;
	}
	
	@Override
	public void execute() {
		
		// Checks if the EnumSet is empty, or in other words in no prayers are
		// currently active. If so cancel the task.
		if(player.getPrayerActive().isEmpty()) {
			this.cancel();
			return;
		}
		
		// Iterate through all of the currently activated prayers, determine if
		// a prayer point can be drained, and last but not least if a prayer
		// point can be drained then determine if the player is out of points.
		for(Prayer prayer : player.getPrayerActive()) {
			if(counter.add(prayer, 1) >= drainFormula(prayer)) {
				player.getSkills()[Skills.PRAYER].decreaseLevel(1, true);
				Skills.refresh(player, Skills.PRAYER);
				counter.setCount(prayer, 0);
				
				// Determine if the player is out of points, if they are then
				// break from the loop and cancel the task.
				if(checkPrayer())
					break;
			}
		}
	}
	
	/**
	 * Determines if the {@link Player}s prayer has ran out.
	 * @return {@code true} if the player ran out of prayer, {@code false}
	 * otherwise.
	 */
	private boolean checkPrayer() {
		if(player.getSkills()[Skills.PRAYER].getLevel() < 1) {
			player.message("You've run out of prayer points!");
			Prayer.deactivateAll(player);
			this.cancel();
			return true;
		}
		return false;
	}
	
	/**
	 * Calculates the amount of ticks needed to drain a single prayer point when
	 * using {@code prayer}. This method utilizes {@link Player}s prayer bonus
	 * and the prayer drain rate.
	 * @param prayer the prayer to calculate for.
	 * @return the amount of ticks needed to drain a prayer point.
	 */
	private int drainFormula(Prayer prayer) {
		double rate = prayer.getDrainRate();
		double addFactor = 1;
		double divideFactor = 30;
		double bonus = player.getEquipment().getBonuses()[CombatConstants.BONUS_PRAYER];
		double tick = 600;
		double second = 1000;
		return (int) (((rate + (addFactor + bonus / divideFactor)) * second) / tick);
	}
}