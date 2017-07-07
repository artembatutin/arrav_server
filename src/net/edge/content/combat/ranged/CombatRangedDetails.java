package net.edge.content.combat.ranged;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.edge.util.TextUtils;
import net.edge.content.combat.weapon.FightStyle;
import net.edge.world.node.item.container.impl.Equipment;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemDefinition;

import java.util.Arrays;
import java.util.Optional;

/**
 * The class which is cached in the player class to access functionality regarding
 * the ranged combat details.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CombatRangedDetails {
	
	/**
	 * A mapping which contains every combat ranged weapon definition by the ranged weapon items.
	 */
	public static final Int2ObjectArrayMap<CombatRangedWeapon> RANGED_WEAPONS = new Int2ObjectArrayMap<>();
	
	/**
	 * A mapping which contains every combat ranged weapon by the combat ranged ammo definitions.
	 */
	public static final Int2ObjectArrayMap<CombatRangedAmmunition[]> RANGED_AMMUNITION = new Int2ObjectArrayMap<>();
	
	static {
		for(CombatRangedWeapon weapon : RANGED_WEAPONS.values()) {
			RANGED_AMMUNITION.put(weapon.id, weapon.ammunitions);
		}
	}
	
	/**
	 * The player these details are for.
	 */
	private final Player player;
	
	/**
	 * The weapon this player is using.
	 */
	private Optional<CombatRangedWeapon> weapon;
	
	/**
	 * The player's ammunition.
	 */
	private Optional<CombatRangedAmmunition> ammunition;
	
	/**
	 * Constructs a new {@link CombatRangedDetails}.
	 * @param player {@link #player}.
	 */
	public CombatRangedDetails(Player player) {
		this.player = player;
	}
	
	/**
	 * Gets the delay for this weapon.
	 * @return the numerical value which defines the delay.
	 */
	public int delay() {
		if(!weapon.isPresent()) {
			throw new IllegalStateException("Ranged weapon is not present and it reached execution state.");
		}
		
		FightStyle style = player.getFightType().getStyle();
		int delay = weapon.get().getDelay();
		int fightStyle = style.equals(FightStyle.ACCURATE) || style.equals(FightStyle.DEFENSIVE) ? 1 : 0;
		return delay + fightStyle;
	}
	
	/**
	 * Determines the players ammunition.
	 * @param ammunition the ammunition this player is using.
	 * @param def        the definition of this ammunition.
	 * @return {@link CombatRangedAmmunition} or null if no value is found.
	 */
	public CombatRangedAmmunition determineAmmo(Item ammunition, CombatRangedWeapon def) {
		if(def.type.equals(CombatRangedType.SPECIAL_BOW)) {
			return CombatRangedDetails.RANGED_WEAPONS.get(def.getWeapon()).ammunitions[0];
		}
		for(CombatRangedAmmunition ammo : def.getAmmunitions()) {
			for(Item i : ammo.getAmmus()) {
				if(ammunition.getId() == i.getId())
					return ammo;
			}
		}
		return null;
	}
	
	/**
	 * Determines the weapon to use for the player.
	 */
	public boolean determine() {
		if(!weapon.isPresent()) {
			player.message("This ranged weapon hasn't been configured yet, please report on our forums...");
			player.getCombatBuilder().reset();
			return false;
		}
		int slot = weapon.get().getType().checkAmmunition() ? Equipment.ARROWS_SLOT : Equipment.WEAPON_SLOT;
		Item ammo = player.getEquipment().get(slot);
		if(ammo == null) {
			player.message("You don't have any ammunition to shoot with...");
			player.getCombatBuilder().reset();
			return false;
		}
		if(!ammunition.isPresent()) {
			player.message("You cannot use " + TextUtils.appendPluralCheck(ammo.getDefinition().getName().toLowerCase()) + " with this ranged weapon.");
			player.getCombatBuilder().reset();
			return false;
		}
		return true;
	}
	
	/**
	 * @return the weapon
	 */
	public Optional<CombatRangedWeapon> getWeapon() {
		return weapon;
	}
	
	/**
	 * @param weapon the weapon to set
	 */
	public void setWeapon(CombatRangedWeapon weapon) {
		if(weapon == null)
			this.weapon = Optional.empty();
		else
			this.weapon = Optional.of(weapon);
	}
	
	/**
	 * @return the ammunition
	 */
	public Optional<CombatRangedAmmunition> getAmmunition() {
		return ammunition;
	}
	
	/**
	 * @param ammunition the ammunition to set
	 */
	public void setAmmunition(CombatRangedAmmunition ammunition) {
		if(ammunition == null)
			this.ammunition = Optional.empty();
		else
			this.ammunition = Optional.of(ammunition);
	}
	/**
	 * Represents a single ranged ammunition.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class CombatRangedAmmo {
		
		/**
		 * The identifier of this ammunition.
		 */
		private final Item item;
		
		/**
		 * The definition of this ranged ammunition.
		 */
		private final CombatRangedAmmunition definition;
		
		/**
		 * Constructs a new {@link CombatRangedAmmo}.
		 * @param item       {@link #item}.
		 * @param definition {@link #definition}.
		 */
		public CombatRangedAmmo(Item item, CombatRangedAmmunition definition) {
			this.item = item;
			this.definition = definition;
		}
		
		@Override
		public String toString() {
			return ItemDefinition.DEFINITIONS[item.getId()].getName();
		}
		
		/**
		 * @return the id
		 */
		public Item getItem() {
			return item;
		}
		
		/**
		 * @return the definition
		 */
		public CombatRangedAmmunition getDefinition() {
			return definition;
		}
	}
	
	/**
	 * Represents a single ranged weapon.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class CombatRangedWeapon {
		
		/**
		 * The ammunitions which this weapon can use.
		 */
		private final CombatRangedAmmunition[] ammunitions;
		
		/**
		 * The identification of this weapon.
		 */
		private int id;
		
		/**
		 * The ammunition this weapon is currently using.
		 */
		private CombatRangedAmmo ammunition;
		
		/**
		 * The combat ranged type for this ranged weapon.
		 */
		private final CombatRangedType type;
		
		/**
		 * The delay between shooting from this ranged weapon.
		 */
		private final int delay;
		
		/**
		 * Constructs a new {@link CombatRangedWeapon}
		 * @param ammunitions {@link #ammunitions}.
		 * @param type        {@link #type}.
		 * @param delay       {@link #delay}.
		 */
		public CombatRangedWeapon(CombatRangedAmmunition[] ammunitions, CombatRangedType type, int delay) {
			this.ammunitions = ammunitions;
			this.type = type;
			this.delay = delay;
		}
		
		/**
		 * @return the ammunition
		 */
		public CombatRangedAmmunition[] getAmmunitions() {
			return ammunitions;
		}
		
		/**
		 * @return the id.
		 */
		public int getWeapon() {
			return id;
		}
		
		/**
		 * @param id the id to set.
		 */
		public void setId(int id) {
			this.id = id;
		}
		
		/**
		 * @return the ammunition.
		 */
		public CombatRangedAmmo getAmmunition() {
			return ammunition;
		}
		
		/**
		 * @param ammunition the ammunition to set.
		 */
		public void setAmmunition(CombatRangedAmmo ammunition) {
			this.ammunition = ammunition;
		}
		
		/**
		 * @return the type
		 */
		public CombatRangedType getType() {
			return type;
		}
		
		/**
		 * @return the delay
		 */
		public int getDelay() {
			return delay;
		}
	}
}
