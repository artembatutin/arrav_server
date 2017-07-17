package net.edge.content.skill.summoning.familiar.impl;

import net.edge.util.rand.RandomUtils;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.skill.summoning.Summoning;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.content.skill.summoning.familiar.ability.BeastOfBurden;
import net.edge.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.content.skill.summoning.SummoningData;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.player.Player;

import java.util.Optional;

/**
 * Represents the Spirit kalphite familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class SpiritKalphite extends Familiar {
	
	/**
	 * Constructs a new {@link SpiritKalphite}.
	 */
	public SpiritKalphite() {
		super(SummoningData.SPIRIT_KALPHITE);
	}
	
	private final BeastOfBurden ability = new BeastOfBurden(6);
	
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
		} else if(id == 2) {
			Summoning.openBeastOfBurden(player, npc);
		}
		
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"This activity is not optimal for us.", "We are growing infuriated. What is our goal?"};
	
}
