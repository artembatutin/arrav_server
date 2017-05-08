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
 * Represents the Spirit wolf familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class SpiritWolf extends Familiar {
	
	/**
	 * The identification of the spirit wolf.
	 */
	private static final int SPIRIT_WOLF_ID = 6829;
	
	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 600;
	
	/**
	 * Constructs a new {@link SpiritWolf}.
	 */
	public SpiritWolf() {
		super(SPIRIT_WOLF_ID, LIFE_TICKS);
	}
	
	@Override
	public Item getPouch() {
		return new Item(12047);
	}
	
	@Override
	public Charm getCharm() {
		return Charm.GOLD;
	}
	
	@Override
	public int getRequirement() {
		return 1;
	}
	
	@Override
	public int getPoints() {
		return 1;
	}
	
	@Override
	public FamiliarAbility getAbilityType() {
		return new Fighter();
	}
	
	@Override
	public Optional<PassiveAbility> getPassiveAbility() {
		return Optional.empty();
	}
	
	@Override
	public void interact(Player player, Npc npc, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(SPIRIT_WOLF_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
		}
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"What are you doing?", "Danger!", "I smell something good! Hunting time!"};
	
	@Override
	public boolean isCombatic() {
		return true;
	}
}
