package net.arrav.world.entity.actor.combat.magic;

import net.arrav.content.skill.Skills;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;

import java.util.Optional;

/**
 * Represents a combat spell.
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
			player.getCombat().reset(false, true);
			return false;
		}
		
		if(equipmentRequired().isPresent() && !player.getEquipment().containsAll(equipmentRequired().get())) {
			player.message("You do not have the required equipment to cast this spell.");
			player.getCombat().reset(false, true);
			return false;
		}
		
		return MagicRune.hasRunes(player, runes);
	}
	
}
