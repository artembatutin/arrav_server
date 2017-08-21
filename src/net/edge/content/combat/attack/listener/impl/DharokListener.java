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
    4716, 4880, 4881, 4882, 4883,
    4718, 4886, 4887, 4888, 4889,
    4720, 4892, 4893, 4894, 4895,
    4722, 4898, 4899, 4900, 4901
})
@NpcCombatListenerSignature(npcs = {
    2026
})
public class DharokListener extends SimplifiedListener<Actor> {

    @Override
    public Optional<AttackModifier> getModifier(Actor attacker) {
        if (attacker.isMob()) {
            int maxHealth = attacker.toMob().getDefinition().getHitpoints();
            int health = attacker.getCurrentHealth() > maxHealth ? 0 : maxHealth - attacker.getCurrentHealth();
            AttackModifier modifier = new AttackModifier().damage(health * 0.01);
            return Optional.of(modifier);
        } else if (hasArmor(attacker.toPlayer())) {
            int maxHealth = attacker.toPlayer().getSkills()[Skills.HITPOINTS].getRealLevel();
            int health = attacker.getCurrentHealth() > maxHealth * 10 ? 0 : maxHealth * 10 - attacker.getCurrentHealth();
            AttackModifier modifier = new AttackModifier().damage(health * 0.01);
            return Optional.of(modifier);
        }
        return Optional.empty();
    }

    private static boolean hasArmor(Player player) {
        for (Item[] items : ITEMS) {
            if (!player.getEquipment().containsAny(items)) {
                return false;
            }
        }
        return true;
    }

    private static final Item[][] ITEMS = new Item[][] {

        /* All barrows helms */
        { new Item(4716), new Item(4880), new Item(4881), new Item(4882), new Item(4883) },

        /* All barrows axes */
        { new Item(4718), new Item(4886), new Item(4887), new Item(4888), new Item(4889) },

        /* All barrows bodies */
        { new Item(4720), new Item(4892), new Item(4893), new Item(4894), new Item(4895) },

        /* All barrows legs */
        { new Item(4722), new Item(4898), new Item(4899), new Item(4900), new Item(4901) }

    };

}
