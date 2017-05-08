package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.skill.Skills;
import net.edge.world.content.skill.summoning.Charm;
import net.edge.world.content.skill.summoning.familiar.impl.forager.ForagerPassiveAbility;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.Summoning;
import net.edge.world.content.skill.summoning.familiar.Familiar;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.item.ItemIdentifiers;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the Granite crab familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class GraniteCrab extends Familiar {
	
	/**
	 * The identification of the granite crab.
	 */
	private static final int GRANITE_CRAB_ID = 6796;
	
	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 1800;
	
	/**
	 * Constructs a new {@link GraniteCrab}.
	 */
	public GraniteCrab() {
		super(GRANITE_CRAB_ID, LIFE_TICKS);
	}
	
	@Override
	public Item getPouch() {
		return new Item(12009);
	}
	
	@Override
	public Charm getCharm() {
		return Charm.CRIMSON;
	}
	
	@Override
	public int getRequirement() {
		return 16;
	}
	
	@Override
	public int getPoints() {
		return 2;
	}
	
	private final ForagerPassiveAbility ability = new ForagerPassiveAbility(ItemIdentifiers.COD, ItemIdentifiers.PIKE, ItemIdentifiers.SEAWEED, ItemIdentifiers.OYSTER) {
		@Override
		public boolean canForage(Player player) {
			return (Boolean) player.getAttr().get("fishing").get();
		}
		
		@Override
		public void onStore(Player player, Item item) {
			if(item.getId() == ItemIdentifiers.PIKE || item.getId() == ItemIdentifiers.COD) {
				Skills.experience(player, 5.5, Skills.FISHING);
			}
		}
	};
	
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
			player.getDialogueBuilder().append(new NpcDialogue(GRANITE_CRAB_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
		} else if(id == 2) {
			Summoning.openBeastOfBurden(player, npc);
		}
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"Can I have some fish?", "Rock fish now, please?", "When can we go fishing? I want rock fish."};
}
