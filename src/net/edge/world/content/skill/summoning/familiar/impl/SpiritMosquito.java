package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.dialogue.impl.NpcDialogue;
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
 * Represents the Spirit mosquito familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class SpiritMosquito extends Familiar {
	
	/**
	 * The identification of the granite crab.
	 */
	private static final int SPIRIT_MOSQUITO_ID = 7331;
	
	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 1200;
	
	/**
	 * Constructs a new {@link SpiritMosquito}.
	 */
	public SpiritMosquito() {
		super(SPIRIT_MOSQUITO_ID, LIFE_TICKS);
	}
	
	@Override
	public Item getPouch() {
		return new Item(12778);
	}
	
	@Override
	public int getRequirement() {
		return 17;
	}
	
	@Override
	public int getPoints() {
		return 2;
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
			player.getDialogueBuilder().append(new NpcDialogue(SPIRIT_MOSQUITO_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
		}
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"You have lovely ankles.", "Have you ever tasted pirate blood?", "I'm sooo hungry!"};
}
