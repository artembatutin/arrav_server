package net.arrav.content.skill.summoning.familiar.impl;

import net.arrav.content.dialogue.impl.NpcDialogue;
import net.arrav.content.skill.summoning.SummoningData;
import net.arrav.content.skill.summoning.familiar.Familiar;
import net.arrav.content.skill.summoning.familiar.FamiliarAbility;
import net.arrav.content.skill.summoning.familiar.ability.Fighter;
import net.arrav.content.skill.summoning.familiar.passive.PassiveAbility;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * Represents the Dread fowl familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class DreadFowl extends Familiar {
	
	/**
	 * Constructs a new {@link DreadFowl}.
	 */
	public DreadFowl() {
		super(SummoningData.DREADFOWL);
	}
	
	private final Fighter ability = new Fighter();
	
	@Override
	public FamiliarAbility getAbilityType() {
		return ability;
	}
	
	@Override
	public Optional<PassiveAbility> getPassiveAbility() {
		return Optional.empty();
	}
	
	@Override
	public void interact(Player player, Mob mob, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
		} else if(id == 2) {
			player.message("Support for skill boosts haven't been added yet.");
		}
	}
	
	@Override
	public boolean isCombatic() {
		return true;
	}
	
	private final String[] RANDOM_DIALOGUE = {"Attack! Fight! Annihilate", "Can it be fightin' time, please?", "I want to fight something."};
}
