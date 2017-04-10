package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.content.skill.summoning.familiar.Familiar;
import net.edge.world.content.skill.summoning.familiar.ability.LightEnhancer;
import net.edge.world.model.node.entity.npc.Npc;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the Vampyre bat familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class VampyreBat extends Familiar {
	
	/**
	 * The identification of the vampyre bat.
	 */
	private static final int VAMPYRE_BAT_ID = 6835;
	
	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 3300;
	
	/**
	 * Constructs a new {@link VampyreBat}.
	 */
	public VampyreBat() {
		super(VAMPYRE_BAT_ID, LIFE_TICKS);
	}
	
	@Override
	public Item getPouch() {
		return new Item(12053);
	}
	
	@Override
	public int getRequirement() {
		return 31;
	}
	
	@Override
	public int getPoints() {
		return 4;
	}
	
	private final LightEnhancer ability = new LightEnhancer();
	
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
			player.getDialogueBuilder().append(new NpcDialogue(VAMPYRE_BAT_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
		}
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"You're vasting all that blood, can I have some?", "Ven are you going to feed me?",};
}
