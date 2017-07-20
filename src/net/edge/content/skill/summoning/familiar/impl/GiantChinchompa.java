package net.edge.content.skill.summoning.familiar.impl;

import net.edge.util.rand.RandomUtils;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.content.skill.summoning.familiar.ability.Fighter;
import net.edge.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.content.skill.summoning.SummoningData;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * Represents the Giant chinchompa familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class GiantChinchompa extends Familiar {
	
	/**
	 * Constructs a new {@link GiantChinchompa}.
	 */
	public GiantChinchompa() {
		super(SummoningData.GIANT_CHINCHOMPA);
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
	
	private final String[] RANDOM_DIALOGUE = new String[]{"Half a pound of tuppenny rice, half a pound of treacle...", "I seemt o have found a paper bag.", "What's small, brown and blows up?"};
	
}
