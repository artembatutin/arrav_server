package net.edge.content.newcombat.strategy.basic;

import net.edge.content.newcombat.attack.AttackStance;
import net.edge.content.newcombat.hit.Hit;
import net.edge.content.newcombat.strategy.CombatStrategy;
import net.edge.content.skill.Skills;
import net.edge.world.World;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.move.MovementQueue;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Boundary;

public abstract class RangedStrategy<T extends Actor> extends CombatStrategy<T> {

    private static final int BASE_EXPERIENCE_MULTIPLIER = 4;

    @Override
    public boolean withinDistance(T attacker, Actor defender) {
        AttackStance stance = attacker.getNewCombat().getAttackStance();
        int distance = getAttackDistance(stance);

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

        if (attacker.getAttr().get("master_archery").getBoolean())
            return true;

        if (!World.getSimplePathChecker().checkProjectile(attacker.getPosition(), defender.getPosition())) {
            if (!attacker.isFollowing()) {
                attacker.getMovementQueue().follow(defender);
                attacker.setFollowing(true);
            }
            return false;
        }

        if (distance == 1 || distance == 2) {
            if (!attacker.isFollowing()) {
                attacker.getMovementQueue().follow(defender);
            }
        } else {
            if (new Boundary(attacker.getPosition(), attacker.size()).within(defender.getPosition(), defender.size(), distance)) {
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

    @Override
    public boolean canAttack(T attacker, Actor defender) {
        return true;
    }

    protected static void addCombatExperience(Player player, Hit hit) {
        int exp = hit.getDamage() * BASE_EXPERIENCE_MULTIPLIER;

        Skills.experience(player, exp / 3, Skills.HITPOINTS);
        switch (player.getNewCombat().getAttackStance()) {
            case RAPID:
            case ACCURATE:
            case CONTROLLED:
                Skills.experience(player, exp, Skills.RANGED);
                break;
            case LONGRANGE:
            case DEFENSIVE:
                Skills.experience(player, exp / 2, Skills.DEFENCE);
                Skills.experience(player, exp / 2, Skills.RANGED);
                break;

            default: break;
        }
    }

}
