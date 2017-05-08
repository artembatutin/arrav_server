package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.Charm;
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
 * Represents the Evil turnip familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class EvilTurnip extends Familiar {
	
	/**
	 * The identification of the evil turnip.
	 */
	private static final int EVIL_TURNIP_ID = 6825;
	
	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 3000;
	
	/**
	 * Constructs a new {@link EvilTurnip}.
	 */
	public EvilTurnip() {
		super(EVIL_TURNIP_ID, LIFE_TICKS);
	}
	
	@Override
	public Item getPouch() {
		return new Item(12051);
	}
	
	@Override
	public Charm getCharm() {
		return Charm.CRIMSON;
	}
	
	@Override
	public int getRequirement() {
		return 42;
	}
	
	@Override
	public int getPoints() {
		return 5;
	}
	
	private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12138, 12136);
	
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
			player.getDialogueBuilder().append(new NpcDialogue(EVIL_TURNIP_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
		} else if(id == 2) {
			Summoning.openBeastOfBurden(player, npc);
		}
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"My roots feel hurty. I thinking it be someone I eated.", "Hur hur hur...", "When we gonna fighting things, boss?"};
	
}
