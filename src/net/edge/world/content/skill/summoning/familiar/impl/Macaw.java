package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.dialogue.impl.OptionDialogue;
import net.edge.world.content.dialogue.impl.StatementDialogue;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.impl.forager.ForagerPassiveAbility;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.content.skill.summoning.Summoning;
import net.edge.world.content.skill.summoning.familiar.Familiar;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the Macaw familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Macaw extends Familiar {

	/**
	 * The identification of the bronze minotaur.
	 */
	private static final int MACAW_ID = 6851;

	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 3100;

	/**
	 * Constructs a new {@link Macaw}.
	 */
	public Macaw() {
		super(MACAW_ID, LIFE_TICKS);
	}

	@Override
	public Item getPouch() {
		return new Item(12071);
	}

	@Override
	public int getRequirement() {
		return 41;
	}

	@Override
	public int getPoints() {
		return 5;
	}

	private final ForagerPassiveAbility ability = new ForagerPassiveAbility(199, 201, 203, 205) {
		@Override
		public boolean canForage(Player player) {
			if(ThreadLocalRandom.current().nextInt(100) > 10) {
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
					player.getDialogueBuilder().append(new NpcDialogue(MACAW_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
				} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
					Summoning.openBeastOfBurden(player, npc);
				} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
					player.getDialogueBuilder().advance();
				} else if(t.equals(OptionDialogue.OptionType.FOURTH_OPTION)) {
					player.getMessages().sendCloseWindows();
				}
			}, "Interact", "View storage", "Remote view", "Nevermind"), new StatementDialogue("This ability has not been added yet."));
		} else if(id == 2) {
			Summoning.openBeastOfBurden(player, npc);
		}
	}

	private final String[] RANDOM_DIALOGUE = new String[]{"Awk! Gimme the rum! Gimme the rum!", "Awk! I'm a pirate! Awk! Yo, ho ho!"};

}
