package net.edge.world.entity.actor.combat.attack.listener.item;

import net.edge.content.skill.Skills;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.CombatModifier;
import net.edge.world.entity.actor.combat.attack.listener.ItemCombatListenerSignature;
import net.edge.world.entity.actor.combat.attack.listener.NpcCombatListenerSignature;
import net.edge.world.entity.actor.combat.attack.listener.SimplifiedListener;

import java.util.Optional;

/**
 * Handles the Dharok's armor effects to the assigned npc and item ids.
 * @author Michael | Chex
 */
@NpcCombatListenerSignature(npcs = {1671})
@ItemCombatListenerSignature(items = {4716, 4718, 4720, 4722})
public class DharokListener extends SimplifiedListener<Actor> {

    private static final CombatModifier MODIFIER = new CombatModifier().damage(DharokListener::modifyDamage);

    @Override
    public Optional<CombatModifier> getModifier(Actor attacker) {
        return Optional.of(MODIFIER);
    }

    private static int modifyDamage(Actor attacker, Actor defender, int damage) {
        int maxHealth;

        if (attacker.isMob()) {
            maxHealth = attacker.toMob().getDefinition().getHitpoints();
        } else {
            maxHealth = attacker.toPlayer().getMaximumHealth();
        }

        double multiplier = (((maxHealth - attacker.getCurrentHealth()) / 10 ) * 0.01) + 1;

        return (int) ((damage * multiplier));
    }

}
