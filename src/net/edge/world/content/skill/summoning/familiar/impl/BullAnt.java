package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.Charm;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.content.skill.summoning.Summoning;
import net.edge.world.content.skill.summoning.familiar.Familiar;
import net.edge.world.content.skill.summoning.familiar.ability.BeastOfBurden;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the Bull ant familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BullAnt extends Familiar {

	/**
	 * The identification of the bull ant.
	 */
	private static final int BULL_ANT_ID = 6867;

	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 3000;

	/**
	 * Constructs a new {@link BullAnt}.
	 */
	public BullAnt() {
		super(BULL_ANT_ID, LIFE_TICKS);
	}

	@Override
	public Item getPouch() {
		return new Item(12087);
	}
	
	@Override
	public Charm getCharm() {
		return Charm.GOLD;
	}
	
	@Override
	public int getRequirement() {
		return 47;
	}

	@Override
	public int getPoints() {
		return 5;
	}

	private final BeastOfBurden ability = new BeastOfBurden(9);

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
			player.getDialogueBuilder().append(new NpcDialogue(BULL_ANT_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
		} else if(id == 2) {
			Summoning.openBeastOfBurden(player, npc);
		}
	}

	private final String[] RANDOM_DIALOGUE = new String[]{"All right you worthless biped, fall in!", "What's the matter, Private? Not enjoying the run?"};
}
