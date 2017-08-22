package net.edge.content.combat.attack.listener.impl;

import net.edge.content.combat.attack.AttackModifier;
import net.edge.content.combat.attack.listener.NpcCombatListenerSignature;
import net.edge.content.combat.attack.listener.PlayerCombatListenerSignature;
import net.edge.content.combat.attack.listener.SimplifiedListener;
import net.edge.content.skill.Skills;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * Handles the Dharok's armor effects to the assigned npc and item ids.
 *
 * @author Michael | Chex
 */

@PlayerCombatListenerSignature(items = {
        4716,/* 4880, 4881, 4882, 4883,*/
        4718,/* 4886, 4887, 4888, 4889,*/
        4720,/* 4892, 4893, 4894, 4895,*/
        4722,/* 4898, 4899, 4900, 4901*/
})

@NpcCombatListenerSignature(npcs = {
        2026
})

public class DharokListener extends SimplifiedListener<Actor> {

    @Override
    public Optional<AttackModifier> getModifier(Actor attacker) {
        System.out.println("MODIFIER FOR DHAROKS IS ACTIVE MADAVAKA");
        int maxHealth;

        if (attacker.isMob()) {
            maxHealth = attacker.toMob().getDefinition().getHitpoints();
        } else {
            maxHealth = attacker.toPlayer().getSkills()[Skills.HITPOINTS].getRealLevel();
        }
        int health = attacker.getCurrentHealth() > maxHealth ? 0 : maxHealth - attacker.getCurrentHealth();
        AttackModifier modifier = new AttackModifier().damage(health * 0.001);
        return Optional.of(modifier);
    }

}
