package net.edge.content.skill.summoning.familiar.impl;

import net.edge.util.rand.RandomUtils;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.content.skill.summoning.familiar.ability.Fighter;
import net.edge.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.content.skill.summoning.specials.SummoningData;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

import java.util.Optional;

/**
 * The predefined familiar settings for the minotaur familiars.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class MinotaurFamiliar extends Familiar {
	
	private static final String[] RANDOM_DIALOGUE = new String[]{"All this walking about is making me angry.", "Can you tell me why we're not fighting yet?", "Hey no-horns!"};
	
	/**
	 * Constructs a new {@link MinotaurFamiliar}.
	 * @param data Summoning data.
	 */
	MinotaurFamiliar(SummoningData data) {
		super(data);
	}
	
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
			player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
		}
	}
	
	/**
	 * Represents the bronze minotaur familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class BronzeMinotaur extends MinotaurFamiliar {
		
		/**
		 * Constructs a new {@link BronzeMinotaur}.
		 */
		public BronzeMinotaur() {
			super(SummoningData.BRONZE_MINOTAUR);
		}
		
	}
	
	/**
	 * Represents the iron minotaur familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class IronMinotaur extends MinotaurFamiliar {
		
		/**
		 * Constructs a new {@link IronMinotaur}.
		 */
		public IronMinotaur() {
			super(SummoningData.IRON_MINOTAUR);
		}
		
	}
	
	/**
	 * Represents the steel minotaur familiar.
	 * @author Artem Batutin <artembatutin@gmail.com>
	 */
	public static final class SteelMinotaur extends MinotaurFamiliar {
		
		/**
		 * Constructs a new {@link IronMinotaur}.
		 */
		public SteelMinotaur() {
			super(SummoningData.STEEL_MINOTAUR);
		}
		
	}
	
	/**
	 * Represents the mithril minotaur familiar.
	 * @author Artem Batutin <artembatutin@gmail.com>
	 */
	public static final class MithrilMinotaur extends MinotaurFamiliar {
		
		/**
		 * Constructs a new {@link IronMinotaur}.
		 */
		public MithrilMinotaur() {
			super(SummoningData.MITHRIL_MINOTAUR);
		}
		
	}
	
	/**
	 * Represents the adamant minotaur familiar.
	 * @author Artem Batutin <artembatutin@gmail.com>
	 */
	public static final class AdamantMinotaur extends MinotaurFamiliar {
		
		/**
		 * Constructs a new {@link IronMinotaur}.
		 */
		public AdamantMinotaur() {
			super(SummoningData.ADAMANT_MINOTAUR);
		}
		
	}
	
	/**
	 * Represents the rune minotaur familiar.
	 * @author Artem Batutin <artembatutin@gmail.com>
	 */
	public static final class RuneMinotaur extends MinotaurFamiliar {
		
		/**
		 * Constructs a new {@link IronMinotaur}.
		 */
		public RuneMinotaur() {
			super(SummoningData.RUNE_MINOTAUR);
		}
		
	}
}
