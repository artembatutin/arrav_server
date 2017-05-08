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
 * The predefined familiar settings for the void familiars.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class SpiritTriceFamiliar extends Familiar {
	
	/**
	 * The life ticks.
	 */
	private static final int LIFE_TICKS = 3600;
	
	/**
	 * Dialogues.
	 */
	private static final String[] RANDOM_DIALOGUE = new String[]{"Where's my brothers?", "Where's my sisters?"};
	
	/**
	 * Constructs a new {@link VoidFamiliar}.
	 * @param id the identification of this familiar.
	 */
	public SpiritTriceFamiliar(int id) {
		super(id, LIFE_TICKS);
	}
	
	@Override
	public Charm getCharm() {
		return Charm.GREEN;
	}
	
	@Override
	public final int getRequirement() {
		return 43;
	}
	
	@Override
	public final int getPoints() {
		return 5;
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
		 * The identification of the spirit cockatrice.
		 */
		private static final int SPIRIT_COCKATRICE_ID = 6875;
		
		/**
		 * Constructs a new {@link SpiritCockatrice}.
		 */
		public SpiritCockatrice() {
			super(SPIRIT_COCKATRICE_ID);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12109);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public Item getPouch() {
			return new Item(12095);
		}
		
	}
	
	/**
	 * Represents the Spirit guthatrice familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class SpiritGuthatrice extends SpiritTriceFamiliar {
		
		/**
		 * The identification of the spirit guthatrice.
		 */
		private static final int SPIRIT_GUTHATRICE_ID = 6877;
		
		/**
		 * Constructs a new {@link SpiritGuthatrice}.
		 */
		public SpiritGuthatrice() {
			super(SPIRIT_GUTHATRICE_ID);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12111);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public Item getPouch() {
			return new Item(12097);
		}
	}
	
	/**
	 * Represents the Spirit saratrice familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class SpiritSaratrice extends SpiritTriceFamiliar {
		
		/**
		 * The identification of the spirit saratrice.
		 */
		private static final int SPIRIT_SARATRICE_ID = 6879;
		
		/**
		 * Constructs a new {@link SpiritSaratrice}.
		 */
		public SpiritSaratrice() {
			super(SPIRIT_SARATRICE_ID);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12113);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public Item getPouch() {
			return new Item(12099);
		}
	}
	
	/**
	 * Represents the Spirit zamatrice familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class SpiritZamatrice extends SpiritTriceFamiliar {
		
		/**
		 * The identification of the spirit zamatrice.
		 */
		private static final int SPIRIT_ZAMATRICE_ID = 6881;
		
		/**
		 * Constructs a new {@link SpiritZamatrice}.
		 */
		public SpiritZamatrice() {
			super(SPIRIT_ZAMATRICE_ID);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12115);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public Item getPouch() {
			return new Item(12101);
		}
	}
	
	/**
	 * Represents the Spirit pengatrice familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class SpiritPengatrice extends SpiritTriceFamiliar {
		
		/**
		 * The identification of the spirit pengatrice.
		 */
		private static final int SPIRIT_PENGATRICE_ID = 6879;
		
		/**
		 * Constructs a new {@link SpiritPengatrice}.
		 */
		public SpiritPengatrice() {
			super(SPIRIT_PENGATRICE_ID);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12117);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public Item getPouch() {
			return new Item(12103);
		}
	}
	
	/**
	 * Represents the Spirit Coraxatrice familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class SpiritCoraxatrice extends SpiritTriceFamiliar {
		
		/**
		 * The identification of the spirit coraxatrice.
		 */
		private static final int SPIRIT_CORAXATRICE_ID = 6885;
		
		/**
		 * Constructs a new {@link SpiritCoraxatrice}.
		 */
		public SpiritCoraxatrice() {
			super(SPIRIT_CORAXATRICE_ID);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12119);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public Item getPouch() {
			return new Item(12105);
		}
	}
	
	/**
	 * Represents the Spirit Vulatrice familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class SpiritVulatrice extends SpiritTriceFamiliar {
		
		/**
		 * The identification of the spirit vulatrice.
		 */
		private static final int SPIRIT_VULATRICE_ID = 6887;
		
		/**
		 * Constructs a new {@link SpiritVulatrice}.
		 */
		public SpiritVulatrice() {
			super(SPIRIT_VULATRICE_ID);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(12121);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public Item getPouch() {
			return new Item(12107);
		}
	}
}
