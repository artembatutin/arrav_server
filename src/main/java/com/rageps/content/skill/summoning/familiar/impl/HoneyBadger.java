package com.rageps.content.skill.summoning.familiar.impl;

import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.content.skill.summoning.SummoningData;
import com.rageps.content.skill.summoning.familiar.Familiar;
import com.rageps.content.skill.summoning.familiar.FamiliarAbility;
import com.rageps.content.skill.summoning.familiar.ability.Fighter;
import com.rageps.content.skill.summoning.familiar.passive.PassiveAbility;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * Represents the Honey badger familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class HoneyBadger extends Familiar {
	
	/**
	 * Constructs a new {@link HoneyBadger}.
	 */
	public HoneyBadger() {
		super(SummoningData.HONEY_BADGER);
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
	public boolean isCombatic() {
		return true;
	}
	
	@Override
	public void interact(Player player, Mob mob, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
		}
	}
	
	private final String[][] RANDOM_DIALOGUE = new String[][]{{"*An outpouring of sanity-straining abuse.*",}, {"*A lambasting of visibly illustrated obscenities.*",}};
	
}
