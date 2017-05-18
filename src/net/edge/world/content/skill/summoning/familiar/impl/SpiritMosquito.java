package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.utils.rand.RandomUtils;
import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.familiar.Familiar;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.ability.Fighter;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.content.skill.summoning.specials.SummoningData;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

import java.util.Optional;

/**
 * Represents the Spirit mosquito familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class SpiritMosquito extends Familiar {
	
	/**
	 * Constructs a new {@link SpiritMosquito}.
	 */
	public SpiritMosquito() {
		super(SummoningData.SPIRIT_MOSQUITO);
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
	public void interact(Player player, Npc npc, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
		}
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"You have lovely ankles.", "Have you ever tasted pirate blood?", "I'm sooo hungry!"};
}
