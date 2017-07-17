package net.edge.content.skill.summoning.familiar.impl;

import net.edge.util.rand.RandomUtils;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.skill.summoning.Summoning;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.content.skill.summoning.familiar.impl.forager.ForagerPassiveAbility;
import net.edge.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.content.skill.summoning.SummoningData;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.player.Player;

import java.util.Optional;

/**
 * The predefined familiar settings for the void familiars.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class SpiritTriceFamiliar extends Familiar {
	
	/**
	 * Dialogues.
	 */
	private static final String[] RANDOM_DIALOGUE = new String[]{"Where's my brothers?", "Where's my sisters?"};
	
	/**
	 * Constructs a new {@link VoidFamiliar}.
	 * @param data Summoning data
	 */
	public SpiritTriceFamiliar(SummoningData data) {
		super(data);
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
		} else if(id == 2) {
			Summoning.openBeastOfBurden(player, npc);
		} else if(id == 3) {
			player.message("Oops, we didn't know what the ability was for this familiar, if you know it");
			player.message("please let us know through the forums.");
		}
	}
	
	/**
	 * Represents the Spirit cockatrice familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class SpiritCockatrice extends SpiritTriceFamiliar {
		
		/**
		 * Constructs a new {@link SpiritCockatrice}.
		 */
		public SpiritCockatrice() {
			super(SummoningData.SPIRIT_COCKATRICE);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12109);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
	}
	
	/**
	 * Represents the Spirit guthatrice familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class SpiritGuthatrice extends SpiritTriceFamiliar {
		
		/**
		 * Constructs a new {@link SpiritGuthatrice}.
		 */
		public SpiritGuthatrice() {
			super(SummoningData.SPIRIT_GUTHATRICE);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12111);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
	}
	
	/**
	 * Represents the Spirit saratrice familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class SpiritSaratrice extends SpiritTriceFamiliar {
		
		/**
		 * Constructs a new {@link SpiritSaratrice}.
		 */
		public SpiritSaratrice() {
			super(SummoningData.SPIRIT_SARATRICE);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12113);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
	}
	
	/**
	 * Represents the Spirit zamatrice familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class SpiritZamatrice extends SpiritTriceFamiliar {
		
		/**
		 * Constructs a new {@link SpiritZamatrice}.
		 */
		public SpiritZamatrice() {
			super(SummoningData.SPIRIT_ZAMATRICE);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12115);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
	}
	
	/**
	 * Represents the Spirit pengatrice familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class SpiritPengatrice extends SpiritTriceFamiliar {
		
		/**
		 * Constructs a new {@link SpiritPengatrice}.
		 */
		public SpiritPengatrice() {
			super(SummoningData.SPIRIT_PENGATRICE);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12117);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
	}
	
	/**
	 * Represents the Spirit Coraxatrice familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class SpiritCoraxatrice extends SpiritTriceFamiliar {
		
		/**
		 * Constructs a new {@link SpiritCoraxatrice}.
		 */
		public SpiritCoraxatrice() {
			super(SummoningData.SPIRIT_CORAXATRICE);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12119);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
	}
	
	/**
	 * Represents the Spirit Vulatrice familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class SpiritVulatrice extends SpiritTriceFamiliar {
		
		/**
		 * Constructs a new {@link SpiritVulatrice}.
		 */
		public SpiritVulatrice() {
			super(SummoningData.SPIRIT_VULATRICE);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12121);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
	}
}
