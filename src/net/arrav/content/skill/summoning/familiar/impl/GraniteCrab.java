package net.arrav.content.skill.summoning.familiar.impl;

import net.arrav.content.dialogue.impl.NpcDialogue;
import net.arrav.content.skill.Skills;
import net.arrav.content.skill.summoning.Summoning;
import net.arrav.content.skill.summoning.SummoningData;
import net.arrav.content.skill.summoning.familiar.Familiar;
import net.arrav.content.skill.summoning.familiar.FamiliarAbility;
import net.arrav.content.skill.summoning.familiar.impl.forager.ForagerPassiveAbility;
import net.arrav.content.skill.summoning.familiar.passive.PassiveAbility;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.ItemIdentifiers;

import java.util.Optional;

/**
 * Represents the Granite crab familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class GraniteCrab extends Familiar {
	
	/**
	 * Constructs a new {@link GraniteCrab}.
	 */
	public GraniteCrab() {
		super(SummoningData.GRANITE_CRAB);
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
	public void interact(Player player, Mob mob, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
		} else if(id == 2) {
			Summoning.openBeastOfBurden(player, mob);
		}
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"Can I have some fish?", "Rock fish now, please?", "When can we go fishing? I want rock fish."};
}
