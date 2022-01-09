package com.rageps.content.skill.summoning.familiar.impl;

import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.content.skill.summoning.SummoningData;
import com.rageps.content.skill.summoning.familiar.Familiar;
import com.rageps.content.skill.summoning.familiar.FamiliarAbility;
import com.rageps.content.skill.summoning.familiar.passive.PassiveAbility;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * Represents the Spirit tz-kih familiar. familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class SpiritTzKih extends Familiar {
	
	/**
	 * Constructs a new {@link SpiritTzKih}.
	 */
	public SpiritTzKih() {
		super(SummoningData.SPIRIT_TZ_KIH);
	}
	
	@Override
	public FamiliarAbility getAbilityType() {
		// TODO Auto-generated method stub
		return null;
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
	
	private final String[] RANDOM_DIALOGUE = new String[]{"Hmph, silly JalYt.", "Have you heard of blood bat, JalYt?",};
	
}
