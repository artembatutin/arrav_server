package net.edge.content.combat;

import net.edge.content.combat.effect.CombatEffect;
import net.edge.content.combat.effect.CombatEffectType;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.hit.HitIcon;
import net.edge.content.combat.hit.Hitsplat;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Projectile;
import net.edge.world.World;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.move.MovementQueue;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.AntifireDetails;
import net.edge.world.locale.Boundary;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A collection of utility methods and constants related to combat.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatUtil {

    /**
     * The default constructor.
     *
     * @throws UnsupportedOperationException if this class is instantiated.
     */
    private CombatUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }

    /**
     * Gets the ranged distance based on {@code weapon}.
     *
     * @param weapon the weapon you have equipped.
     * @return the ranged distance.
     * @throws IllegalArgumentException if the weapon interface type is
     *                                  invalid.
     */
    public static int getRangedDistance(WeaponInterface weapon) {
        switch (weapon) {
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

    public static <A extends Actor> List<A> actorsWithinDistance(Actor player, Set<A> actors, int radius) {
        List<A> collected = new LinkedList<>();

        for (A other : actors) {
            if (other == null) continue;
            if (!other.getPosition().withinDistance(player.getPosition(), radius))
                continue;
            if (other.same(player)) continue;
            if (other.getCurrentHealth() <= 0 || other.isDead()) continue;
            collected.add(other);
        }
        return collected;
    }

    /**
     * Gets the hit delay for the specified {@code type}.
     *
     * @param attacker the character doing the hit
     * @param defender the victim being hit
     * @param type     the combat type of this hit
     * @return the delay for the combat type
     * @throws IllegalArgumentException if the combat type is invalid
     */
    public static int getHitDelay(Actor attacker, Actor defender, CombatType type) {
        int delay = defender.isMob() ? 1 : 0;

        if (type.equals(CombatType.MELEE)) {
            return delay;
        }

        int distance = (int) attacker.getPosition().getDistance(defender.getPosition());

        if (type.equals(CombatType.MAGIC)) {
            return Projectile.MAGIC_DELAYS[distance > 10 ? 10 : distance];
        }

        if (type.equals(CombatType.RANGED)) {
            return Projectile.RANGED_DELAYS[distance > 10 ? 10 : distance];
        }

        return delay;
    }

    /**
     * Gets the hitsplat delay for the specified {@code type}.
     *
     * @param type the combat type of this hit
     * @return the delay for the combat type.
     * @throws IllegalArgumentException if the combat type is invalid.
     */
    public static int getHitsplatDelay(CombatType type) {
        return type != CombatType.MELEE ? 1 : 0;
    }

    /**
     * Applies the {@code effect} in any context.
     *
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
     *
     * @param combatLevel      the combat level of the first person.
     * @param otherCombatLevel the combat level of the other person.
     * @return the combat level difference.
     */
    public static int combatLevelDifference(int combatLevel, int otherCombatLevel) {
        if (combatLevel > otherCombatLevel) {
            return (combatLevel - otherCombatLevel);
        } else if (otherCombatLevel > combatLevel) {
            return (otherCombatLevel - combatLevel);
        } else {
            return 0;
        }
    }

    /**
     * Calculates a pseudo-random hit for {@code character} based on {@code
     * victim} and {@code type}.
     *
     * @param victim the victim of this hit that will be used as a factor.
     * @param hit    the hit being processed in the combat.
     * @return the generated hit, will most likely return a different result if
     * called on two different occasions even with the same arguments.
     * @throws IllegalArgumentException if the combat type is invalid.
     */
    public static Hit calculateSoaking(Actor victim, CombatType type, Hit hit) {
        if (hit.getHitIcon() == HitIcon.NONE && victim.isPlayer()) {
            Player player = victim.toPlayer();
            int i = type == CombatType.MAGIC ? 2 : type == CombatType.RANGED ? 1 : 0;
            int reducedDamage = (int) ((hit.getDamage() - 200) * player.getEquipment().getBonuses()[CombatConstants.ABSORB_MELEE + i] / 100.D);
            if (hit.getDamage() - reducedDamage > 200 && victim.getCurrentHealth() > 200) {
                if (reducedDamage > 0) {
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
     * @return {@code true} if the character is close enough
     */
    public static boolean checkAttackDistance(Actor attacker) {
        Actor defender = attacker.getCombat().getLastDefender();
        CombatStrategy strategy = attacker.getCombat().getStrategy();
        if (strategy == null || defender == null) {
            return false;
        }
        int distance = strategy.getAttackDistance(attacker, attacker.getCombat().getFightType());
        MovementQueue movement = attacker.getMovementQueue();
        MovementQueue otherMovement = defender.getMovementQueue();

        if (!movement.isMovementDone() && !otherMovement.isMovementDone() && !movement.isLockMovement() && !attacker.isFrozen()) {
            distance += 1;

            // XXX: Might have to change this back to 1 or even remove it, not
            // sure what it's like on actual runescape. Are you allowed to
            // attack when the character is trying to run away from you?
            if (movement.isRunning()) {
                distance += 2;
            }
        }

        if (strategy.getCombatType() == CombatType.MELEE) {//Melee clipping.
            if (!World.getSimplePathChecker().checkLine(attacker.getPosition(), defender.getPosition(), attacker.size())) {
                if (!attacker.isFollowing()) {
                    attacker.getMovementQueue().follow(defender);
                    attacker.setFollowing(true);
                }
                return false;
            }
        } else {//Projectile clipping.
            if (attacker.getAttr().get("master_archery").getBoolean())
                return true;
            if (!World.getSimplePathChecker().checkProjectile(attacker.getPosition(), defender.getPosition())) {
                if (!attacker.isFollowing()) {
                    attacker.getMovementQueue().follow(defender);
                    attacker.setFollowing(true);
                }
                return false;
            }
        }

        if (distance == 1 || distance == 2) {
            if (!attacker.isFollowing()) {
                attacker.getMovementQueue().follow(defender);
                attacker.setFollowing(true);
            }
        } else {
            if (new Boundary(attacker.getPosition(), attacker.size()).within(defender.getPosition(), defender.size(), distance)) {
                attacker.getMovementQueue().reset();
                attacker.setFollowing(false);
                System.out.println(attacker.isMob() + " mob reset #3");
                return true;
            } else {
                attacker.getMovementQueue().follow(defender);
                attacker.setFollowing(true);
                return false;
            }
        }

        return new Boundary(attacker.getPosition(), attacker.size()).within(defender.getPosition(), defender.size(), distance);
    }

    public static Animation getBlockAnimation(Actor actor) {
        if (actor.isPlayer()) {
            int animation = 404;
            Player player = actor.toPlayer();
            if (player.getShieldAnimation() != null) {
                animation = player.getShieldAnimation().getBlock();
            } else if (player.getWeaponAnimation() != null) {
                animation = player.getWeaponAnimation().getBlocking();
            }
            return new Animation(animation, Animation.AnimationPriority.LOW);
        }

        return new Animation(actor.toMob().getDefinition().getDefenceAnimation(), Animation.AnimationPriority.LOW);
    }


    public static boolean canAttack(Actor attacker, Actor defender) {
        return validate(attacker) && validate(defender) && attacker.getInstance() == defender.getInstance();
    }

    private static boolean validate(Actor actor) {
        return actor != null && !actor.isDead() && actor.isVisible() && !actor.isTeleporting() && !actor.isNeedsPlacement();
    }

    public static CombatHit generateDragonfire(Mob attacker, Actor defender, int minimumHit) {
        int hitDelay = CombatUtil.getHitDelay(attacker, defender, CombatType.MAGIC);
        int hitsplatDelay = CombatUtil.getHitsplatDelay(CombatType.MAGIC);

        int max = 650;
        if (defender.isPlayer()) {
            Player player = defender.toPlayer();
            boolean shield = player.getEquipment().containsAny(1540, 11283);

            if (shield && player.getAntifireDetails().isPresent()) {
                max = 0;
            } else if (player.getAntifireDetails().isPresent()) {
                max = player.getAntifireDetails().get().getType() == AntifireDetails.AntifireType.REGULAR ? 162 : 81;
            } else if (shield) {
                max = 150;
            }
        }

        int damage = RandomUtils.inclusive(minimumHit, max);
        Hit hit = new Hit(damage, Hitsplat.NORMAL, HitIcon.NONE, true);
        return new CombatHit(hit, hitDelay, hitsplatDelay);
    }

}
