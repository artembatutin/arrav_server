package net.edge.content.skill.summoning.familiar.impl;

import net.edge.util.rand.RandomUtils;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.StatementDialogue;
import net.edge.content.skill.summoning.Summoning;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.content.skill.summoning.familiar.impl.forager.ForagerPassiveAbility;
import net.edge.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.content.skill.summoning.SummoningData;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

import java.util.Optional;

/**
 * Represents the Macaw familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Macaw extends Familiar {
	
	/**
	 * Constructs a new {@link Macaw}.
	 */
	public Macaw() {
		super(SummoningData.MACAW);
	}
	
	private final ForagerPassiveAbility ability = new ForagerPassiveAbility(199, 201, 203, 205) {
		@Override
		public boolean canForage(Player player) {
			if(RandomUtils.inclusive(100) > 10) {
				return false;
			}
			return true;
		}
	};
	
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
		return false;
	}
	
	@Override
	public void interact(Player player, Npc npc, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new OptionDialogue(t -> {
				if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
					player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
				} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
					Summoning.openBeastOfBurden(player, npc);
				} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
					player.getDialogueBuilder().advance();
				} else if(t.equals(OptionDialogue.OptionType.FOURTH_OPTION)) {
					player.closeWidget();
				}
			}, "Interact", "View storage", "Remote view", "Nevermind"), new StatementDialogue("This ability has not been added yet."));
		} else if(id == 2) {
			Summoning.openBeastOfBurden(player, npc);
		}
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"Awk! Gimme the rum! Gimme the rum!", "Awk! I'm a pirate! Awk! Yo, ho ho!"};
	
}
