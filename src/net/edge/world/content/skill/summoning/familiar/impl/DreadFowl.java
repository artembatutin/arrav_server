package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.Charm;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.ability.Fighter;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.content.skill.summoning.familiar.Familiar;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the Dread fowl familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class DreadFowl extends Familiar {
	
	/**
	 * The identification of the dread fowl.
	 */
	private static final int DREAD_FOWL_ID = 6825;
	
	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 400;
	
	/**
	 * Constructs a new {@link DreadFowl}.
	 */
	public DreadFowl() {
		super(DREAD_FOWL_ID, LIFE_TICKS);
	}
	
	@Override
	public Item getPouch() {
		return new Item(12043);
	}
	
	@Override
	public Charm getCharm() {
		return Charm.GOLD;
	}
	
	@Override
	public int getRequirement() {
		return 4;
	}
	
	@Override
	public int getPoints() {
		return 1;
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
	public void interact(Player player, Npc npc, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(DREAD_FOWL_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
		} else if(id == 2) {
			player.message("Support for skill boosts haven't been added yet.");
		}
	}
	
	private final String[] RANDOM_DIALOGUE = {"Attack! Fight! Annihilate", "Can it be fightin' time, please?", "I want to fight something."};
	
	@Override
	public boolean isCombatic() {
		return true;
	}
}
