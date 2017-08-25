package net.edge.content.combat.content;

import net.edge.content.skill.Skills;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * Created by mmaks on 8/24/2017.
 */
public abstract class MagicSpell {

    public final int level;

    public final double baseExperience;

    public final RequiredRune[] runes;

    public MagicSpell(int level, double baseExperience, RequiredRune... runes) {
        this.level = level;
        this.baseExperience = baseExperience;
        this.runes = runes;
    }

    /**
     * The functionality that should occur for the specified caster.
     * @param caster the caster casting the spell.
     * @param victim the victim hit by the spell.
     */
    public void effect(Actor caster, Optional<Actor> victim) {

    }

    public Optional<Item[]> equipmentRequired() {
        return Optional.empty();
    }

    public boolean canCast(Actor attacker, Optional<Actor> defender) {
        if(attacker.isMob()) {
            return true;
        }

        Player player = attacker.toPlayer();

        if(player.getSkills()[Skills.MAGIC].getLevel() < level) {
            player.message("You need a Magic level of " + level + " to cast this spell.");
            player.getCombat().reset();
            return false;
        }

        if(equipmentRequired().isPresent() && !player.getEquipment().containsAll(equipmentRequired().get())) {
            player.message("You do not have the required equipment to cast this spell.");
            player.getCombat().reset();
            return false;
        }

        return MagicRune.hasRunes(player, runes);
    }

}
