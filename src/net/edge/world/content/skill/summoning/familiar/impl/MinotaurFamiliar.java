package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.familiar.Familiar;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.ability.Fighter;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The predefined familiar settings for the minotaur familiars.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class MinotaurFamiliar extends Familiar {
	
	/**
	 * Constructs a new {@link MinotaurFamiliar}.
	 * @param id   the identification of this familiar.
	 * @param life the amount of ticks it stays alive for.
	 */
	public MinotaurFamiliar(int id, int life) {
		super(id, life);
	}
	
	private static final String[] RANDOM_DIALOGUE = new String[]{"All this walking about is making me angry.", "Can you tell me why we're not fighting yet?", "Hey no-horns!"};
	
	private final Fighter ability = new Fighter();
	
	@Override
	public final FamiliarAbility getAbilityType() {
		return ability;
	}
	
	@Override
	public final Optional<PassiveAbility> getPassiveAbility() {
		return Optional.empty();
	}
	
	@Override
	public final boolean isCombatic() {
		return true;
	}
	
	@Override
	public final void interact(Player player, Npc npc, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(this.getId(), RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
		}
	}
	
	/**
	 * Represents the Bronze minotaur familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class BronzeMinotaur extends MinotaurFamiliar {
		
		/**
		 * The identification of the bronze minotaur.
		 */
		private static final int BRONZE_MINOTAUR_ID = 6853;
		
		/**
		 * The amount of ticks this familiar stays alive for.
		 */
		private static final int LIFE_TICKS = 3000;
		
		/**
		 * Constructs a new {@link BronzeMinotaur}.
		 */
		public BronzeMinotaur() {
			super(BRONZE_MINOTAUR_ID, LIFE_TICKS);
		}
		
		@Override
		public Item getPouch() {
			return new Item(12073);
		}
		
		@Override
		public int getRequirement() {
			return 36;
		}
		
		@Override
		public int getPoints() {
			return 3;
		}
	}
	
	/**
	 * Represents the Iron minotaur familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class IronMinotaur extends MinotaurFamiliar {
		
		/**
		 * The identification of the iron minotaur.
		 */
		private static final int IRON_MINOTAUR_ID = 6855;
		
		/**
		 * The amount of ticks this familiar stays alive for.
		 */
		private static final int LIFE_TICKS = 3700;
		
		/**
		 * Constructs a new {@link IronMinotaur}.
		 */
		public IronMinotaur() {
			super(IRON_MINOTAUR_ID, LIFE_TICKS);
		}
		
		@Override
		public Item getPouch() {
			return new Item(12075);
		}
		
		@Override
		public int getRequirement() {
			return 46;
		}
		
		@Override
		public int getPoints() {
			return 9;
		}
	}
}
