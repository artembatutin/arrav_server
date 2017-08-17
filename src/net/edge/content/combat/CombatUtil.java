package net.edge.content.combat;

import net.edge.content.combat.effect.CombatEffect;
import net.edge.content.combat.effect.CombatEffectType;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.hit.HitIcon;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.world.World;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.move.MovementQueue;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Equipment;
import net.edge.world.locale.Boundary;
import net.edge.world.locale.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * A collection of utility methods and constants related to combat.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatUtil {

	/**
	 * The default constructor.
	 * @throws UnsupportedOperationException if this class is instantiated.
	 */
	private CombatUtil() {
		throw new UnsupportedOperationException("This class cannot be instantiated!");
	}

	/**
	 * Deals the damage contained within {@code data} to all {@code characters}
	 * within {@code radius} of {@code position}. This method also executes
	 * {@code action} for every character. Please note that this only accounts
	 * for local characters.
	 * @param attacker      the attacker in this combat session.
	 * @param victims       the characters that will be attempted to be hit.
	 * @param position      the position that the radius will be calculated from.
	 * @param radius        the radius of the damage.
	 * @param hits          the amount of hits to calculate.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 * @param action        the action to execute for each victim.
	 */
	private static <E extends Actor> CombatHit[] damageActorsWithin(Actor attacker, Iterable<E> victims, Position position, int radius, int hits, CombatType type, boolean checkAccuracy, Consumer<E> action) {
		List<CombatHit> combatHits = new ArrayList<>();
		for(E c : victims) {
			if(c == null)
				continue;
			if(!c.getPosition().withinDistance(position, radius) || c.same(attacker) || c.same(attacker.getNewCombat().getDefender()) || c.getCurrentHealth() <= 0 || c.isDead())
				continue;
//			CombatHit data = new CombatHit(attacker, c, hits, type, checkAccuracy); FIXME: FIX THIS SHIT
//			c.getNewCombat().getDamageCache().add(attacker, data.getDamage());
//			if(action != null)
//				action.accept(c);
//			combatHits.add(data);
		}
		return combatHits.toArray(new CombatHit[combatHits.size()]);
	}

	/**
	 * Deals the damage contained within {@code data} to all {@code characters}
	 * within {@code radius} of {@code position}. This method also executes
	 * {@code action} for every character. Please note that this only accounts
	 * for local characters.
	 * @param attacker      the attacker in this combat session.
	 * @param victims       the characters that will be attempted to be hit.
	 * @param position      the position that the radius will be calculated from.
	 * @param radius        the radius of the damage.
	 * @param hits          the amount of hits to calculate.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 */
	public static CombatHit[] damageActorsWithin(Actor attacker, Iterable<? extends Actor> victims, Position position, int radius, int hits, CombatType type, boolean checkAccuracy) {
		return damageActorsWithin(attacker, victims, position, radius, hits, type, checkAccuracy, null);
	}

	/**
	 * Deals the damage contained within {@code data} to all {@link Player}s
	 * within {@code radius} of {@code position}. Please note that this only
	 * accounts for local characters.
	 * @param attacker      the attacker in this combat session.
	 * @param position      the position that the radius will be calculated from.
	 * @param radius        the radius of the damage.
	 * @param hits          the amount of hits to calculate.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 * @param action        the action to execute for each victim.
	 */
	private static CombatHit[] damagePlayersWithin(Actor attacker, Position position, int radius, int hits, CombatType type, boolean checkAccuracy, Consumer<Player> action) {
		return damageActorsWithin(attacker, () -> World.get().getLocalPlayers(attacker), position, radius, hits, type, checkAccuracy, action);
	}

	/**
	 * Deals the damage contained within {@code data} to all {@link Player}s
	 * within {@code radius} of {@code position}. This method also executes
	 * {@code action} for every character. Please note that this only accounts
	 * for local characters.
	 * @param attacker      the attacker in this combat session.
	 * @param position      the position that the radius will be calculated from.
	 * @param radius        the radius of the damage.
	 * @param hits          the amount of hits to calculate.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 */
	public static CombatHit[] damagePlayersWithin(Actor attacker, Position position, int radius, int hits, CombatType type, boolean checkAccuracy) {
		return damagePlayersWithin(attacker, position, radius, hits, type, checkAccuracy, null);
	}

	/**
	 * Deals the damage contained within {@code data} to all {@link Mob}s within
	 * {@code radius} of {@code position}. This method also executes
	 * {@code action} for every character. Please note that this only accounts
	 * for local characters.
	 * @param attacker      the attacker in this combat session.
	 * @param position      the position that the radius will be calculated from.
	 * @param radius        the radius of the damage.
	 * @param hits          the amount of hits to calculate.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 * @param action        the action to execute for each victim.
	 */
	private static CombatHit[] damageMobsWithin(Actor attacker, Position position, int radius, int hits, CombatType type, boolean checkAccuracy, Consumer<Mob> action) {
		return damageActorsWithin(attacker, () -> World.get().getLocalMobs(attacker), position, radius, hits, type, checkAccuracy, action);
	}

	/**
	 * Deals the damage contained within {@code data} to all {@link Mob}s within
	 * {@code radius} of {@code position}. This method also executes
	 * {@code action} for every character. Please note that this only accounts
	 * for local characters.
	 * @param attacker      the attacker in this combat session.
	 * @param position      the position that the radius will be calculated from.
	 * @param radius        the radius of the damage.
	 * @param hits          the amount of hits to calculate.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 */
	public static CombatHit[] damageMobsWithin(Actor attacker, Position position, int radius, int hits, CombatType type, boolean checkAccuracy) {
		return damageMobsWithin(attacker, position, radius, hits, type, checkAccuracy, null);
	}

	/**
	 * Determines if {@code player} is wielding a crystal bow.
	 * @param player the player to determine this for.
	 * @return {@code true} if the player is wielding a crystal bow,
	 * {@code false} otherwise.
	 */
	public static boolean isCrystalBow(Player player) {
        Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
        return item != null && item.getDefinition().getName().toLowerCase().contains("crystal bow");
    }

	/**
	 * Gets the ranged distance based on {@code weapon}.
	 * @param weapon the weapon you have equipped.
	 * @return the ranged distance.
	 * @throws IllegalArgumentException if the weapon interface type is invalid.
	 */

	public static int getRangedDistance(WeaponInterface weapon) {
		switch(weapon) {
			case SALAMANDER:
				return 1;
			case CHINCHOMPA:
				return 8;
			case DART:
			case THROWNAXE:
				return 4;
			case KNIFE:
			case JAVELIN:
				return 5;
			case CROSSBOW:
			case LONGBOW:
			case COMPOSITE_BOW:
				return 8;
			case SHORTBOW:
				return 7;
			default:
				throw new IllegalArgumentException("Invalid weapon interface type!");
		}
	}

	/**
	 * Gets the delay for the specified {@code type}.
	 * @param character the character doing the hit.
	 * @param victim the victim being hit.
	 * @param type the combat type to retrieve the delay for.
	 * @return the delay for the combat type.
	 * @throws IllegalArgumentException if the combat type is invalid.
	 */
	public static int getDelay(Actor character, Actor victim, CombatType type) {
		int delay = character.isPlayer() && victim.isMob() ? 1 : 0;
		if(character.isPlayer() && character.toPlayer().getWeapon().equals(WeaponInterface.SALAMANDER)) {
			return 1 + delay;
		}
		if(type.equals(CombatType.MELEE)) {
			return 1 + delay;
		}
		if(type.equals(CombatType.RANGED)) {
			return 2 + delay;
		}
		if(type.equals(CombatType.MAGIC)) {
			return 3 + delay;
		}
		return 1 + delay;
	}

	/**
	 * Applies the {@code effect} in any context.
	 * @param effect the effect that must be applied.
	 * @return {@code true} if it was successfully applied, {@code false}
	 * otherwise.
	 */
	public static boolean effect(Actor character, CombatEffectType effect) {
		return CombatEffect.EFFECTS.get(effect).start(character);
	}

	/**
	 * Calculates the combat level difference for wilderness player vs. player
	 * combat.
	 * @param combatLevel      the combat level of the first person.
	 * @param otherCombatLevel the combat level of the other person.
	 * @return the combat level difference.
	 */
	public static int combatLevelDifference(int combatLevel, int otherCombatLevel) {
		if(combatLevel > otherCombatLevel) {
			return (combatLevel - otherCombatLevel);
		} else if(otherCombatLevel > combatLevel) {
			return (otherCombatLevel - combatLevel);
		} else {
			return 0;
		}
	}

	/**
	 * Calculates a pseudo-random hit for {@code character} based on {@code victim} and {@code type}.
	 * @param victim the victim of this hit that will be used as a factor.
	 * @param hit    the hit being processed in the combat.
	 * @return the generated hit, will most likely return a different result if
	 * called on two different occasions even with the same arguments.
	 * @throws IllegalArgumentException if the combat type is invalid.
	 */
	public static Hit calculateSoaking(Actor victim, CombatType type, Hit hit) {
		if(hit.getHitIcon() == HitIcon.NONE && victim.isPlayer()) {
			Player player = victim.toPlayer();
			int i = type == CombatType.MAGIC ? 2 : type == CombatType.RANGED ? 1 : 0;
			int reducedDamage = (int) ((hit.getDamage() - 200) * player.getEquipment().getBonuses()[CombatConstants.ABSORB_MELEE + i] / 100.D);
			if(hit.getDamage() - reducedDamage > 200 && victim.getCurrentHealth() > 200) {
				if(reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
//					hit.setSoak(reducedDamage); // TODO: Integrate soaking
				}
			}
		}
		return hit;
	}

	/**
	 * Determines if the character within {@code builder} is close enough to
	 * it's victim to attack.
	 *
	 * @param strategy the strategy that will be checked
	 * @return {@code true} if the character is close enough
	 */
	public static boolean checkAttackDistance(Actor attacker, Actor defender, CombatStrategy<?> strategy) {
		if (strategy == null) {
			return false;
		}
		int distance = strategy.getAttackDistance(attacker.getNewCombat().getFightType());
		MovementQueue movement = attacker.getMovementQueue();
		MovementQueue otherMovement = defender.getMovementQueue();

		if(!movement.isMovementDone() && !otherMovement.isMovementDone() && !movement.isLockMovement() && !attacker.isFrozen()) {
			distance += 1;

			// XXX: Might have to change this back to 1 or even remove it, not
			// sure what it's like on actual runescape. Are you allowed to
			// attack when the character is trying to run away from you?
			if(movement.isRunning()) {
				distance += 2;
			}
		}

		if(strategy.getCombatType() == CombatType.MELEE) {//Melee clipping.
			if(!World.getSimplePathChecker().checkLine(attacker.getPosition(), defender.getPosition(), attacker.size())) {
				if(!attacker.isFollowing()) {
					attacker.getMovementQueue().follow(defender);
					attacker.setFollowing(true);
				}
				return false;
			}
		} else {//Projectile clipping.
			if(attacker.getAttr().get("master_archery").getBoolean())
				return true;
			if(!World.getSimplePathChecker().checkProjectile(attacker.getPosition(), defender.getPosition())) {
				if(!attacker.isFollowing()) {
					attacker.getMovementQueue().follow(defender);
					attacker.setFollowing(true);
				}
				return false;
			}
		}

		if(distance == 1 || distance == 2) {
			if(!attacker.isFollowing()) {
				attacker.getMovementQueue().follow(defender);
			}
		} else {
			if(new Boundary(attacker.getPosition(), attacker.size()).within(defender.getPosition(), defender.size(), distance)) {
				attacker.getMovementQueue().reset();
				attacker.setFollowing(false);
				return true;
			} else {
				attacker.getMovementQueue().follow(defender);
				attacker.setFollowing(true);
				return false;
			}
		}

		return new Boundary(attacker.getPosition(), attacker.size()).within(defender.getPosition(), defender.size(), distance);
	}

	public static <E extends Actor> List<E> actorsWithinDistance(Actor node, Iterator<E> target, int radius) {
		List<E> list = new ArrayList<>();
		while(target.hasNext()) {
			E character = target.next();
			if(character == null) {
				continue;
			}
			if(character.getPosition().withinDistance(node.getPosition(), radius) && !character.same(node) && character.getCurrentHealth() > 0 && !character.isDead())
				list.add(character);
		}
		return list;
	}

	public static void playersWithinDistance(Actor node, Consumer<Player> action, int radius) {
		node.getLocalPlayers().forEach(p -> {
			if(p.getPosition().withinDistance(node.getPosition(), radius) && !p.same(node) && p.getCurrentHealth() > 0 && !p.isDead())
				action.accept(p);
		});
	}

	public static void mobsWithinDistance(Actor node, Consumer<Mob> action, int radius) {
		node.getLocalMobs().forEach(p -> {
			if(p.getPosition().withinDistance(node.getPosition(), radius) && !p.same(node) && p.getCurrentHealth() > 0 && !p.isDead())
				action.accept(p);
		});
	}

	public static void relativeWithinDistance(Actor node, Consumer<Actor> action, int radius) {
		if(node.inWilderness() && (node.getNewCombat().getDefender() != null && node.getNewCombat().getDefender().isPlayer())) {
			node.getLocalPlayers().forEach(p -> {
				if(p.getPosition().withinDistance(node.getPosition(), radius) && !p.same(node) && p.getCurrentHealth() > 0 && !p.isDead())
					action.accept(p);
			});
		} else {
			node.getLocalMobs().forEach(p -> {
				if(p.getPosition().withinDistance(node.getPosition(), radius) && !p.same(node) && p.getCurrentHealth() > 0 && !p.isDead())
					action.accept(p);
			});
		}
	}
}
