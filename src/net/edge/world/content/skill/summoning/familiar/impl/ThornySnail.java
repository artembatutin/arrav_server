package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.dialogue.impl.NpcDialogue;
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
 * Represents the Thorny snail familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ThornySnail extends Familiar {
	
	/**
	 * The identification of the thorny snail.
	 */
	private static final int THORNY_SNAIL_ID = 6806;
	
	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 1600;
	
	/**
	 * Constructs a new {@link ThornySnail}.
	 */
	public ThornySnail() {
		super(THORNY_SNAIL_ID, LIFE_TICKS);
	}
	
	@Override
	public Item getPouch() {
		return new Item(12019);
	}
	
	@Override
	public int getRequirement() {
		return 13;
	}
	
	@Override
	public int getPoints() {
		return 2;
	}
	
	private final BeastOfBurden bob = new BeastOfBurden(3);
	
	@Override
	public FamiliarAbility getAbilityType() {
		return bob;
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
			player.getDialogueBuilder().append(RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]);
		} else if(id == 2) {
			Summoning.openBeastOfBurden(player, npc);
		}
	}
	
	private final NpcDialogue[] RANDOM_DIALOGUE = new NpcDialogue[]{new NpcDialogue(THORNY_SNAIL_ID, "All this running around the place is fun!"), new NpcDialogue(THORNY_SNAIL_ID, "Okay, I have to ask, what are those things", "you people totter on about?"),};
}
