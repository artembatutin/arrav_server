package net.edge.world.entity.actor.combat.attack.listener.item;

import net.edge.content.skill.Skills;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.AttackModifier;
import net.edge.world.entity.actor.combat.attack.listener.ItemCombatListenerSignature;
import net.edge.world.entity.actor.combat.attack.listener.NpcCombatListenerSignature;
import net.edge.world.entity.actor.combat.attack.listener.SimplifiedListener;

import java.util.Optional;

/**
 * Handles the Dharok's armor effects to the assigned npc and item ids.
 *
 * @author Michael | Chex
 */
@NpcCombatListenerSignature(npcs = { 2026 })
@ItemCombatListenerSignature(items = { 4716, 4718, 4720, 4722 })
public class DharokListener extends SimplifiedListener<Actor> {

    @Override
    public Optional<AttackModifier> getModifier(Actor attacker) {
        int maxHealth;

        if (attacker.isMob()) {
            maxHealth = attacker.toMob().getDefinition().getHitpoints();
        } else {
            maxHealth = attacker.toPlayer().getSkills()[Skills.HITPOINTS].getRealLevel() * 10;
        }

        int health = attacker.getCurrentHealth() > maxHealth ? 0 : maxHealth - attacker.getCurrentHealth();
        AttackModifier modifier = new AttackModifier().damage(health * 0.001);
        return Optional.of(modifier);
    }

}
