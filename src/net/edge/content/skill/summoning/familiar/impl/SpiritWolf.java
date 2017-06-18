package net.edge.content.skill.summoning.familiar.impl;

import net.edge.util.rand.RandomUtils;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.content.skill.summoning.familiar.ability.Fighter;
import net.edge.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.content.skill.summoning.SummoningData;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

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
	public void interact(Player player, Npc npc, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
		}
	}
	
	@Override
	public boolean isCombatic() {
		return true;
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"What are you doing?", "Danger!", "I smell something good! Hunting time!"};
}
