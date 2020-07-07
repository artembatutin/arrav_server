package com.rageps.content.skill.summoning.familiar.impl;

import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.content.skill.summoning.familiar.ability.Fighter;
import com.rageps.content.skill.summoning.familiar.ability.Healer;
import com.rageps.content.skill.summoning.familiar.ability.Teleporter;
import com.rageps.content.skill.summoning.familiar.impl.forager.ForagerPassiveAbility;
import com.rageps.content.skill.summoning.familiar.passive.PassiveAbility;
import com.rageps.content.skill.summoning.Summoning;
import com.rageps.content.skill.summoning.SummoningData;
import com.rageps.content.skill.summoning.familiar.Familiar;
import com.rageps.content.skill.summoning.familiar.FamiliarAbility;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.loc.Area;

import java.util.Optional;

/**
 * The predefined familiar settings for the void familiars.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class VoidFamiliar extends Familiar {
	
	/**
	 * Dialogues.
	 */
	private static final String[] RANDOM_DIALOGUE = new String[]{"Let's go play hide an' seek!", "I'm coming to tickle you!",};
	
	/**
	 * Constructs a new {@link VoidFamiliar}.
	 * @param data Summoning data
	 */
	public VoidFamiliar(SummoningData data) {
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
	
	/**
	 * Represents the Void ravager familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class VoidRavager extends VoidFamiliar {
		
		/**
		 * Constructs a new {@link VoidRavager}.
		 */
		public VoidRavager() {
			super(SummoningData.VOID_RAVAGER);
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(436, 438, 440);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public void interact(Player player, Mob mob, int id) {
			if(id == 1) {
				player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
			} else if(id == 2) {
				Summoning.openBeastOfBurden(player, mob);
			}
		}
	}
	
	/**
	 * Represents the Void shifter familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class VoidShifter extends VoidFamiliar {
		
		/**
		 * Constructs a new {@link VoidShifter}.
		 */
		public VoidShifter() {
			super(SummoningData.VOID_SHIFTER);
		}
		
		private final Teleporter ability = new Teleporter(Area.PEST_CONTROL, Optional.of(new Teleporter.TeleportPolicy(true, 10)));
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public void interact(Player player, Mob mob, int id) {
			if(id == 1) {
				player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
			}
		}
	}
	
	/**
	 * Represents the Void spinner familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class VoidSpinner extends VoidFamiliar {
		
		/**
		 * Constructs a new {@link VoidSpinner}.
		 */
		public VoidSpinner() {
			super(SummoningData.VOID_SPINNER);
		}
		
		private final Healer ability = new Healer(25, 1);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public void interact(Player player, Mob mob, int id) {
			if(id == 1) {
				player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
			}
		}
	}
	
	/**
	 * Represents the Void torcher familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class VoidTorcher extends VoidFamiliar {
		
		/**
		 * Constructs a new {@link VoidTorcher}.
		 */
		public VoidTorcher() {
			super(SummoningData.VOID_TORCHER);
		}
		
		private final Fighter ability = new Fighter();
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public void interact(Player player, Mob mob, int id) {
			if(id == 1) {
				player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
			} else if(id == 2) {
				player.message("Oops, we didn't know what the ability was for this familiar, if you know it");
				player.message("please let us know through the forums.");
			}
		}
	}
}
