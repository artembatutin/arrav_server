package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.Charm;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.content.skill.summoning.familiar.Familiar;
import net.edge.world.content.skill.summoning.familiar.ability.Fighter;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the Giant chinchompa familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class GiantChinchompa extends Familiar {
	
	/**
	 * The identification of the Giant chinchompa.
	 */
	private static final int GIANT_CHINCHOMPA_ID = 7353;
	
	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 3100;
	
	/**
	 * Constructs a new {@link GiantChinchompa}.
	 */
	public GiantChinchompa() {
		super(GIANT_CHINCHOMPA_ID, LIFE_TICKS);
	}
	
	@Override
	public Item getPouch() {
		return new Item(12800);
	}
	
	@Override
	public Charm getCharm() {
		return Charm.BLUE;
	}
	
	@Override
	public int getRequirement() {
		return 29;
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
	public boolean isCombatic() {
		return true;
	}
	
	@Override
	public void interact(Player player, Npc npc, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(GIANT_CHINCHOMPA_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
		}
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"Half a pound of tuppenny rice, half a pound of treacle...", "I seemt o have found a paper bag.", "What's small, brown and blows up?"};
	
}
