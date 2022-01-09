package com.rageps.content.skill.summoning.familiar.impl;

import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.content.skill.summoning.Summoning;
import com.rageps.content.skill.summoning.SummoningData;
import com.rageps.content.skill.summoning.familiar.Familiar;
import com.rageps.content.skill.summoning.familiar.FamiliarAbility;
import com.rageps.content.skill.summoning.familiar.ability.BeastOfBurden;
import com.rageps.content.skill.summoning.familiar.passive.PassiveAbility;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * Represents the Bull ant familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BullAnt extends Familiar {
	
	/**
	 * Constructs a new {@link BullAnt}.
	 */
	public BullAnt() {
		super(SummoningData.BULL_ANT);
	}
	
	private final BeastOfBurden ability = new BeastOfBurden(9);
	
	@Override
	public FamiliarAbility getAbilityType() {
		return ability;
	}
	
	@Override
	public Optional<PassiveAbility> getPassiveAbility() {
		return Optional.empty();
	}
	
	@Override
	public boolean isCombatic() {
		return true;
	}
	
	@Override
	public void interact(Player player, Mob mob, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
		} else if(id == 2) {
			Summoning.openBeastOfBurden(player, mob);
		}
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"All right you worthless biped, fall in!", "What's the matter, Private? Not enjoying the run?"};
}
