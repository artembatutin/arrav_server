package net.edge.world.content.minigame.dueling;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.world.content.container.impl.Equipment;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.content.container.session.impl.DuelSession;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The enumerated type whose elements represent the dueling rules for a
 * duel session.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum DuelingRules {
	NO_RANGED(148002, "You can not use ranged attacks.") {
		@Override
		public boolean meets(Player player, DuelSession duel) {
			if(duel.getRules().contains(NO_MELEE) && duel.getRules().contains(NO_MAGIC)) {
				player.message("You must have atleast one combat type enabled.");
				return false;
			}
			if(duel.getRules().contains(WHIP_DDS_ONLY)) {
				player.message("You can't toggle no-ranged while having whip & dds only enabled.");
				return false;
			}
			if(duel.getRules().contains(ARROWS)) {
				player.message("You can't toggle no-ranged while having arrows disabled.");
				return false;
			}
			return true;
		}
	},
	NO_MELEE(148003, "You can not use melee attacks.") {
		@Override
		public boolean meets(Player player, DuelSession duel) {
			if(duel.getRules().contains(NO_RANGED) && duel.getRules().contains(NO_MAGIC)) {
				player.message("You must have atleast one combat type enabled.");
				return false;
			}
			if(duel.getRules().contains(WHIP_DDS_ONLY)) {
				player.message("You can't toggle no-melee while having whip & dds only enabled.");
				return false;
			}
			if(duel.getRules().contains(WEAPON)) {
				player.message("You can't toggle no-melee while having weapons disabled.");
				return false;
			}
			return true;
		}
	},
	NO_MAGIC(148004, "You can not use magic attacks.") {
		@Override
		public boolean meets(Player player, DuelSession duel) {
			if(duel.getRules().contains(NO_MELEE) && duel.getRules().contains(NO_RANGED)) {
				player.message("You must have atleast one combat type enabled.");
				return false;
			}
			if(duel.getRules().contains(WHIP_DDS_ONLY)) {
				player.message("You can't toggle no-magic while having whip & dds only enabled.");
				return false;
			}
			return true;
		}
	},
	WHIP_DDS_ONLY(148005, "You can only attack with whip & dds.") {
		@Override
		public boolean meets(Player player, DuelSession duel) {
			if(duel.getRules().contains(WEAPON)) {
				player.message("You can't toggle whip & dds only while having weapons disabled.");
				return false;
			}
			if(duel.getRules().contains(NO_MELEE)) {
				player.message("You can't toggle whip & dds only while having no melee disabled.");
				return false;
			}
			if(duel.getRules().contains(NO_MAGIC)) {
				player.message("You can't toggle whip & dds only while having no magic enabled.");
				return false;
			}
			if(duel.getRules().contains(NO_RANGED)) {
				player.message("You can't toggle whip & dds only while having no ranged enabled.");
				return false;
			}
			return true;
		}
	},
	NO_FORFEIT(148006, "You can not forfeit the duel.") {
		@Override
		public boolean meets(Player player, DuelSession duel) {
			if(duel.getRules().contains(NO_MOVEMENT)) {
				player.message("You can't toggle no-forfeiting while having no-movement enabled.");
				return false;
			}
			if(duel.getRules().contains(OBSTACLES)) {
				player.message("You can't toggle no-forfeiting while having obstacles enabled.");
				return false;
			}
			return true;
		}
	},
	NO_DRINKS(148007, "You can not use drinks."),
	NO_FOOD(148008, "You can not use food."),
	NO_PRAYER(148009, "You can not use prayer."),
	NO_MOVEMENT(148010, "You can not move during the duel.") {
		@Override
		public boolean meets(Player player, DuelSession duel) {
			if(duel.getRules().contains(NO_FORFEIT)) {
				player.message("You can't toggle no-movement while having no-forfeiting enabled.");
				return false;
			}
			if(duel.getRules().contains(OBSTACLES)) {
				player.message("You can't toggle no-movement while having obstacles enabled.");
				return false;
			}
			return true;
		}
	},
	OBSTACLES(148011, "There will be obstacles in the arena.") {
		@Override
		public boolean meets(Player player, DuelSession duel) {
			if(duel.getRules().contains(NO_FORFEIT)) {
				player.message("You can't toggle obstacles while having no-forfeiting enabled.");
				return false;
			}
			if(duel.getRules().contains(NO_MOVEMENT)) {
				player.message("You can't toggle obstacles while having no-movement enabled.");
				return false;
			}
			return true;
		}
	},
	NO_SPECIAL_ATTACKS(148012, "You can not use special attacks."),
	
	HELM(148038, Equipment.HEAD_SLOT, "You can not wear items on your head."),
	AMULET(148028, Equipment.AMULET_SLOT, "You can not wear items on your neck."),
	TORSO(148029, Equipment.CHEST_SLOT, "You can not wear items on your chest."),
	LEGS(148030, Equipment.LEGS_SLOT, "You can not wear items on your legs."),
	BOOTS(148031, Equipment.FEET_SLOT, "You can not wear items on your feet."),
	CAPE(148032, Equipment.CAPE_SLOT, "You can not wear items on your back."),
	ARROWS(148033, Equipment.ARROWS_SLOT, "You can not wear items in your quiver.") {
		@Override
		public boolean meets(Player player, DuelSession duel) {
			if(duel.getRules().contains(NO_RANGED)) {
				player.message("You can't disable arrows while having no-ranged enabled.");
				return false;
			}
			return true;
		}
	},
	WEAPON(148034, Equipment.WEAPON_SLOT, "You can not wear weapons.") {
		@Override
		public boolean meets(Player player, DuelSession duel) {
			if(duel.getRules().contains(NO_MELEE)) {
				player.message("You can't disable weapons while having no-melee enabled.");
				return false;
			}
			if(duel.getRules().contains(WHIP_DDS_ONLY)) {
				player.message("You can't disable weapons while having whip & dds only enabled.");
				return false;
			}
			return true;
		}
	},
	SHIELD(148035, Equipment.SHIELD_SLOT, "You can not wear items on your offhand(Includes 2h weapons)"),
	GLOVES(148036, Equipment.HANDS_SLOT, "You can not wear items on your hands."),
	RING(148037, Equipment.RING_SLOT, "You can not wear items on your fingers.");
	
	/**
	 * Caches our enum values.
	 */
	public static final ImmutableSet<DuelingRules> VALUES = Sets.immutableEnumSet(EnumSet.allOf(DuelingRules.class));
	
	/**
	 * The button identification of this rule.
	 */
	private final int buttonId;
	
	/**
	 * The slot of this equipment.
	 */
	private final int slot;
	
	/**
	 * The message displayed on the interface if this rule is enabled.
	 */
	private final String interfaceMessage;
	
	/**
	 * The rule condition chained to this rule.
	 */
	public boolean meets(Player player, DuelSession duel) {
		return true;
	}
	
	/**
	 * Constructs a new {@link DuelingRules}.
	 * @param buttonId         {@link #buttonId}.
	 * @param interfaceMessage {@link #interfaceMessage}.
	 */
	DuelingRules(int buttonId, String interfaceMessage) {
		this.buttonId = buttonId;
		this.interfaceMessage = interfaceMessage;
		this.slot = -1;
	}
	
	/**
	 * Constructs a new {@link DuelingRules}.
	 * @param buttonId         {@link #buttonId}.
	 * @param slot             {@link #slot}.
	 * @param interfaceMessage {@link #interfaceMessage}.
	 */
	DuelingRules(int buttonId, int slot, String interfaceMessage) {
		this.buttonId = buttonId;
		this.interfaceMessage = interfaceMessage;
		this.slot = slot;
	}
	
	/**
	 * Searches for a match.
	 * @param buttonId the button id to check a match for.
	 * @return a {@link DuelingRules} enumerator wrapped in an optional, {@link Optional#empty()} otherwise.
	 */
	public static Optional<DuelingRules> getRules(int buttonId) {
		return VALUES.stream().filter(def -> def.buttonId == buttonId).findFirst();
	}
	
	/**
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}
	
	/**
	 * @return the interfaceMessage
	 */
	public String getInterfaceMessage() {
		return interfaceMessage;
	}
}