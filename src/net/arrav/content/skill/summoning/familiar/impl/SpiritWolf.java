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
 * Represents the Spirit wolf familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class SpiritWolf extends Familiar {
	
	/**
	 * Constructs a new {@link SpiritWolf}.
	 */
	public SpiritWolf() {
		super(SummoningData.SPIRIT_WOLF);
	}
	
	@Override
	public FamiliarAbility getAbilityType() {
		return new Fighter();
	}
	
	@Override
	public Optional<PassiveAbility> getPassiveAbility() {
		return Optional.empty();
	}
	
	@Override
	public void interact(Player player, Mob mob, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
		}
	}
	
	@Override
	public boolean isCombatic() {
		return true;
	}
	
	@Override
	public Familiar create() {
		return null;
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"What are you doing?", "Danger!", "I smell something good! Hunting time!"};
}
