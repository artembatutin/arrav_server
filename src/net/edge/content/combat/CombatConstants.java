package net.edge.content.combat;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.edge.content.combat.strategy.CombatStrategy;

/**
 * The class that contains a collection of constants related to combat. This
 * class serves no other purpose than to hold constants.
 * @author lare96 <http://github.org/lare96>
 */
public final class CombatConstants {
	
	/**
	 * The amount of time it takes in seconds for cached damage to timeout.
	 */
	public static final long DAMAGE_CACHE_TIMEOUT = 60;
	
	/**
	 * The percentage at which damage is reduced by combat protection prayers.
	 */
	public static final double PRAYER_DAMAGE_REDUCTION = .20;
	
	/**
	 * The percentage at which accuracy is reduced by combat protection prayers.
	 */
	public static final double PRAYER_ACCURACY_REDUCTION = .255;
	
	/**
	 * The percentage at which hitpoints will be healed by from the prayer level
	 * when using redemption.
	 */
	public static final double REDEMPTION_PRAYER_HEAL = .25;
	
	/**
	 * The maximum amount of damage that retribution can inflict.
	 */
	public static final int MAXIMUM_RETRIBUTION_DAMAGE = 150;
	
	/**
	 * The attack stab bonus identifier.
	 */
	public static final int ATTACK_STAB = 0;
	
	/**
	 * The attack slash bonus identifier.
	 */
	public static final int ATTACK_SLASH = 1;
	
	/**
	 * The attack crush bonus identifier.
	 */
	public static final int ATTACK_CRUSH = 2;
	
	/**
	 * The attack magic bonus identifier.
	 */
	public static final int ATTACK_MAGIC = 3;
	
	/**
	 * The attack ranged bonus identifier.
	 */
	public static final int ATTACK_RANGED = 4;
	
	/**
	 * The defence stab bonus identifier.
	 */
	public static final int DEFENCE_STAB = 5;
	
	/**
	 * The defence slash bonus identifier.
	 */
	public static final int DEFENCE_SLASH = 6;
	
	/**
	 * The defence crush bonus identifier.
	 */
	public static final int DEFENCE_CRUSH = 7;
	
	/**
	 * The defence magic bonus identifier.
	 */
	public static final int DEFENCE_MAGIC = 8;
	
	/**
	 * The defence ranged bonus identifier.
	 */
	public static final int DEFENCE_RANGED = 9;
	
	/**
	 * The summoning bonus identifier.
	 */
	public static final int DEFENCE_SUMMONING = 10;
	
	/**
	 * The absorb melee bonus identifier.
	 */
	public static final int ABSORB_MELEE = 11;
	
	/**
	 * The absorb magic identifier.
	 */
	public static final int ABSORB_MAGIC = 12;
	
	/**
	 * The absorb ranged bonus identifier.
	 */
	public static final int ABSORB_RANGED = 13;
	
	/**
	 * The strength bonus identifier.
	 */
	public static final int BONUS_STRENGTH = 14;
	
	/**
	 * The ranged strength bonus identifier.
	 */
	public static final int BONUS_RANGED_STRENGTH = 15;
	
	/**
	 * The prayer bonus identifier.
	 */
	public static final int BONUS_PRAYER = 16;
	
	/**
	 * The magic damage bonus identifier.
	 */
	public static final int BONUS_MAGIC_DAMAGE = 17;
	
	/**
	 * The bonus
	 */
	public static final int[] BONUS = {ATTACK_STAB, ATTACK_SLASH, ATTACK_CRUSH, ATTACK_MAGIC, ATTACK_RANGED, DEFENCE_STAB, DEFENCE_SLASH, DEFENCE_CRUSH, DEFENCE_MAGIC, DEFENCE_RANGED, DEFENCE_SUMMONING, ABSORB_MELEE, ABSORB_MAGIC, ABSORB_RANGED, BONUS_STRENGTH, BONUS_RANGED_STRENGTH, BONUS_PRAYER, BONUS_MAGIC_DAMAGE};
	
	/**
	 * The names of all the bonuses in their exact identified slots.
	 */
	public static final String[] BONUS_NAMES = {"Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range", "Summoning", "Absorb Melee", "Absorb Magic", "Absorb Ranged", "Strength", "Ranged Strength", "Prayer", "Magic Damage"};
	
	/**
	 * The names of all the bonuses in their exact identified slots.
	 */
	public static final int[] BONUS_IDS = {1675, 1676, 1677, 1678, 1679, 1680, 1681, 1682, 1683, 1684, 19148, 19149, 19150, 19151, 1686, 19152, 1687, 19153};
	
	/**
	 * The hash collection of all the default NPCs mapped to their combat strategies.
	 */
	public static final Int2ObjectArrayMap<CombatStrategy> DEFAULT_STRATEGIES = new Int2ObjectArrayMap<>();
	
	/**
	 * The default constructor.
	 * @throws UnsupportedOperationException if this class is instantiated.
	 */
	private CombatConstants() {
		throw new UnsupportedOperationException("This class cannot be instantiated!");
	}
}
