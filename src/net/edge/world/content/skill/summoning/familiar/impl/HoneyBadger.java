package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.dialogue.impl.NpcDialogue;
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
 * Represents the Honey badger familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class HoneyBadger extends Familiar {
	
	/**
	 * The identification of the honey badger.
	 */
	private static final int HONEY_BADGER_ID = 6845;
	
	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 2500;
	
	/**
	 * Constructs a new {@link HoneyBadger}.
	 */
	public HoneyBadger() {
		super(HONEY_BADGER_ID, LIFE_TICKS);
	}
	
	@Override
	public Item getPouch() {
		return new Item(12065);
	}
	
	@Override
	public int getRequirement() {
		return 32;
	}
	
	@Override
	public int getPoints() {
		return 4;
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
			player.getDialogueBuilder().append(new NpcDialogue(HONEY_BADGER_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
		}
	}
	
	private final String[][] RANDOM_DIALOGUE = new String[][]{{"*An outpouring of sanity-straining abuse.*",}, {"*A lambasting of visibly illustrated obscenities.*",},};
	
}
