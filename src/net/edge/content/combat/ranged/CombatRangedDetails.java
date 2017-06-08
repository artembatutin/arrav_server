package net.edge.content.combat.ranged;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.edge.util.TextUtils;
import net.edge.content.combat.weapon.FightStyle;
import net.edge.content.container.impl.Equipment;
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
	public static final Int2ObjectArrayMap<CombatRangedAmmoDefinition[]> RANGED_AMMUNITION = new Int2ObjectArrayMap<>();
	
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
	 * Constructs a new {@link CombatRangedDetails}.
	 * @param player {@link #player}.
	 */
	public CombatRangedDetails(Player player) {
		this.player = player;
	}
	
	/**
	 * The weapon this player is using.
	 */
	private Optional<CombatRangedWeapon> weapon;
	
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
	 * @return {@link CombatRangedAmmoDefinition} or null if no value is found.
	 */
	public CombatRangedAmmoDefinition determineAmmo(Item ammunition, CombatRangedWeapon def) {
		if(def.type.equals(CombatRangedType.SPECIAL_BOW)) {
			return CombatRangedDetails.RANGED_WEAPONS.get(def.getWeapon()).ammunitions[0];
		}
		return Arrays.stream(def.getAmmunitions()).filter(t -> Arrays.stream(t.getAmmus()).anyMatch(a -> a.getId() == ammunition.getId())).findFirst().orElse(null);
	}
	
	/**
	 * Determines the weapon to use for the player.
	 */
	public boolean determine() {
		int weaponId = player.getEquipment().get(Equipment.WEAPON_SLOT).getId();
		
		CombatRangedWeapon mappedWeapon = CombatRangedDetails.RANGED_WEAPONS.get(weaponId);
		
		weapon = Optional.ofNullable(mappedWeapon);
		
		if(!weapon.isPresent()) {
			player.message("This ranged weapon hasn't been configured yet, please report on our forums...");
			player.getCombatBuilder().reset();
			return false;
		}
		
		CombatRangedWeapon weapon = this.weapon.get();
		
		weapon.setId(weaponId);
		int slot = weapon.getType().checkAmmunition() ? Equipment.ARROWS_SLOT : Equipment.WEAPON_SLOT;
		Item ammunition = player.getEquipment().get(slot);
		if(ammunition == null) {
			player.message("You don't have any ammunition to shoot with...");
			player.getCombatBuilder().reset();
			return false;
		}
		
		CombatRangedAmmoDefinition def = determineAmmo(ammunition, weapon);
		if(def == null) {
			player.message("You cannot use " + TextUtils.appendPluralCheck(ammunition.getDefinition().getName().toLowerCase()) + " with this ranged weapon.");
			player.getCombatBuilder().reset();
			return false;
		}
		
		weapon.setAmmunition(new CombatRangedAmmo(ammunition, def));
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
	public void setWeapon(Optional<CombatRangedWeapon> weapon) {
		this.weapon = weapon;
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
		private final CombatRangedAmmoDefinition definition;
		
		/**
		 * Constructs a new {@link CombatRangedAmmo}.
		 * @param item       {@link #item}.
		 * @param definition {@link #definition}.
		 */
		public CombatRangedAmmo(Item item, CombatRangedAmmoDefinition definition) {
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
		public CombatRangedAmmoDefinition getDefinition() {
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
		private final CombatRangedAmmoDefinition[] ammunitions;
		
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
		public CombatRangedWeapon(CombatRangedAmmoDefinition[] ammunitions, CombatRangedType type, int delay) {
			this.ammunitions = ammunitions;
			this.type = type;
			this.delay = delay;
		}
		
		/**
		 * @return the ammunition
		 */
		public CombatRangedAmmoDefinition[] getAmmunitions() {
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
