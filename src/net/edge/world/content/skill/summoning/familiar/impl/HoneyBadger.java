package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.utils.rand.RandomUtils;
import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.content.skill.summoning.specials.SummoningData;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.content.skill.summoning.familiar.Familiar;
import net.edge.world.content.skill.summoning.familiar.ability.Fighter;

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
	public void interact(Player player, Npc npc, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
		}
	}
	
	private final String[][] RANDOM_DIALOGUE = new String[][] {
			{"*An outpouring of sanity-straining abuse.*",},
			{"*A lambasting of visibly illustrated obscenities.*",}
	};
	
}
