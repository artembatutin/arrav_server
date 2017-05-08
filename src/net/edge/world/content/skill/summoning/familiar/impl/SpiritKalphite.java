package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.Charm;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.ability.BeastOfBurden;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.content.skill.summoning.Summoning;
import net.edge.world.content.skill.summoning.familiar.Familiar;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the Spirit kalphite familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class SpiritKalphite extends Familiar {
	
	/**
	 * The identification of the spirit kalphite.
	 */
	private static final int SPIRIT_KALPHITE_ID = 6994;
	
	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 2200;
	
	/**
	 * Constructs a new {@link SpiritKalphite}.
	 */
	public SpiritKalphite() {
		super(SPIRIT_KALPHITE_ID, LIFE_TICKS);
	}
	
	@Override
	public Item getPouch() {
		return new Item(12063);
	}
	
	@Override
	public Charm getCharm() {
		return Charm.BLUE;
	}
	
	@Override
	public int getRequirement() {
		return 25;
	}
	
	@Override
	public int getPoints() {
		return 3;
	}
	
	private final BeastOfBurden ability = new BeastOfBurden(6);
	
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
			player.getDialogueBuilder().append(new NpcDialogue(SPIRIT_KALPHITE_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
		} else if(id == 2) {
			Summoning.openBeastOfBurden(player, npc);
		}
		
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"This activity is not optimal for us.", "We are growing infuriated. What is our goal?",};
	
}
