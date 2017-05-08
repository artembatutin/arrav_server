package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.skill.summoning.Charm;
import net.edge.world.content.skill.summoning.familiar.ability.Teleporter;
import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.Summoning;
import net.edge.world.content.skill.summoning.familiar.Familiar;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.ability.Fighter;
import net.edge.world.content.skill.summoning.familiar.ability.Healer;
import net.edge.world.content.skill.summoning.familiar.impl.forager.ForagerPassiveAbility;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.content.teleport.impl.DefaultTeleportSpell.TeleportType;
import net.edge.world.model.locale.Location;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

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
	 * @param id   the identification of this familiar.
	 * @param life the amount of ticks it stays alive for.
	 */
	public VoidFamiliar(int id, int life) {
		super(id, life);
	}
	
	@Override
	public Charm getCharm() {
		return Charm.BLUE;
	}
	
	@Override
	public final int getRequirement() {
		return 34;
	}
	
	@Override
	public final int getPoints() {
		return 4;
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
		 * The identification of the void ravager.
		 */
		private static final int VOID_RAVAGER_ID = 7370;
		
		/**
		 * The amount of ticks this familiar stays alive for.
		 */
		private static final int LIFE_TICKS = 2700;
		
		/**
		 * Constructs a new {@link VoidRavager}.
		 */
		public VoidRavager() {
			super(VOID_RAVAGER_ID, LIFE_TICKS);
		}
		
		@Override
		public Item getPouch() {
			return new Item(12818);
		}
		
		@Override
		public Charm getCharm() {
			return Charm.GREEN;
		}
		
		private final ForagerPassiveAbility ability = new ForagerPassiveAbility(436, 438, 440);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public void interact(Player player, Npc npc, int id) {
			if(id == 1) {
				player.getDialogueBuilder().append(new NpcDialogue(VOID_RAVAGER_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
			} else if(id == 2) {
				Summoning.openBeastOfBurden(player, npc);
			}
		}
	}
	
	/**
	 * Represents the Void shifter familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class VoidShifter extends VoidFamiliar {
		
		/**
		 * The identification of the void shifter.
		 */
		private static final int VOID_SHIFTER_ID = 7367;
		
		/**
		 * The amount of ticks this familiar stays alive for.
		 */
		private static final int LIFE_TICKS = 9400;
		
		/**
		 * Constructs a new {@link VoidShifter}.
		 */
		public VoidShifter() {
			super(VOID_SHIFTER_ID, LIFE_TICKS);
		}
		
		@Override
		public Item getPouch() {
			return new Item(12814);
		}
		
		private final Teleporter ability = new Teleporter(Location.PEST_CONTROL, TeleportType.VOID_FAMILIAR, Optional.of(new Teleporter.TeleportPolicy(true, 10)));
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public void interact(Player player, Npc npc, int id) {
			if(id == 1) {
				player.getDialogueBuilder().append(new NpcDialogue(VOID_SHIFTER_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
			}
		}
	}
	
	/**
	 * Represents the Void spinner familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class VoidSpinner extends VoidFamiliar {
		
		/**
		 * The identification of the void spinner.
		 */
		private static final int VOID_SPINNER_ID = 7333;
		
		/**
		 * The amount of ticks this familiar stays alive for.
		 */
		private static final int LIFE_TICKS = 2700;
		
		/**
		 * Constructs a new {@link VoidSpinner}.
		 */
		public VoidSpinner() {
			super(VOID_SPINNER_ID, LIFE_TICKS);
		}
		
		@Override
		public Item getPouch() {
			return new Item(12780);
		}
		
		private final Healer ability = new Healer(25, 1);
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public void interact(Player player, Npc npc, int id) {
			if(id == 1) {
				player.getDialogueBuilder().append(new NpcDialogue(VOID_SPINNER_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
			}
		}
	}
	
	/**
	 * Represents the Void torcher familiar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class VoidTorcher extends VoidFamiliar {
		
		/**
		 * The identification of the void torcher.
		 */
		private static final int VOID_TORCHER_ID = 7351;
		
		/**
		 * The amount of ticks this familiar stays alive for.
		 */
		private static final int LIFE_TICKS = 9400;
		
		/**
		 * Constructs a new {@link VoidTorcher}.
		 */
		public VoidTorcher() {
			super(VOID_TORCHER_ID, LIFE_TICKS);
		}
		
		@Override
		public Item getPouch() {
			return new Item(12798);
		}
		
		private final Fighter ability = new Fighter();
		
		@Override
		public FamiliarAbility getAbilityType() {
			return ability;
		}
		
		@Override
		public void interact(Player player, Npc npc, int id) {
			if(id == 1) {
				player.getDialogueBuilder().append(new NpcDialogue(VOID_TORCHER_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
			} else if(id == 2) {
				player.message("Oops, we didn't know what the ability was for this familiar, if you know it");
				player.message("please let us know through the forums.");
			}
		}
	}
}
