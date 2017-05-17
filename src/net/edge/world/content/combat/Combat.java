package net.edge.world.content.combat;

import net.edge.Server;
import net.edge.utils.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.content.combat.effect.CombatEffect;
import net.edge.world.content.combat.effect.CombatEffectType;
import net.edge.world.content.combat.magic.CombatNormalSpell;
import net.edge.world.content.combat.magic.CombatSpells;
import net.edge.world.content.combat.magic.CombatWeaken;
import net.edge.world.content.combat.strategy.CombatStrategy;
import net.edge.world.content.combat.strategy.base.MagicCombatStrategy;
import net.edge.world.content.combat.strategy.base.MeleeCombatStrategy;
import net.edge.world.content.combat.strategy.base.RangedCombatStrategy;
import net.edge.world.content.combat.weapon.FightStyle;
import net.edge.world.content.combat.weapon.WeaponInterface;
import net.edge.world.content.container.impl.Equipment;
import net.edge.world.content.skill.Skills;
import net.edge.world.content.skill.prayer.Prayer;
import net.edge.world.model.locale.Boundary;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.model.Graphic;
import net.edge.world.model.node.entity.model.Hit;
import net.edge.world.model.node.entity.model.Projectile;
import net.edge.world.model.node.entity.move.MovementQueue;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.npc.impl.gwd.GodwarsFaction;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;

import java.util.*;
import java.util.function.Consumer;

import static net.edge.world.model.node.NodeType.NPC;
import static net.edge.world.model.node.NodeType.PLAYER;

/**
 * A collection of utility methods and constants related to combat.
 * @author lare96 <http://github.com/lare96>
 */
public final class Combat {
	
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
	public static final Map<Integer, CombatStrategy> DEFAULT_STRATEGIES = new HashMap<>();
	
	/**
	 * The default constructor.
	 * @throws UnsupportedOperationException if this class is instantiated.
	 */
	private Combat() {
		throw new UnsupportedOperationException("This class cannot be instantiated!");
	}
	
	/**
	 * Handles the distribution of experience for the amount of damage dealt in
	 * this combat session attack.
	 * @param builder the combat builder for this combat session.
	 * @param data    the data for this combat session.
	 * @param counter the total amount of damage dealt.
	 */
	protected static void handleExperience(CombatBuilder builder, CombatSessionData data, int counter) {
		if(builder.getCharacter().getType() == PLAYER) {
			if(data.getExperience().length == 0 && data.getType() != CombatType.MAGIC) {
				return;
			}
			Player player = (Player) builder.getCharacter();
			double exp;
			double hitpointsExp;
			
			if(data.getType() == CombatType.MAGIC) {
				exp = ((counter / 10.0) * 4d) + builder.getCharacter().getCurrentlyCasting().baseExperience();
				hitpointsExp = (exp / 3d);
				Skills.experience(player, exp, Skills.MAGIC);
				Skills.experience(player, hitpointsExp, Skills.HITPOINTS);
				return;
			}
			exp = (((counter / 10.0) * 4d) / data.getExperience().length);
			hitpointsExp = (exp / 3d);
			
			for(int amount : data.getExperience()) {
				Skills.experience(player, exp, amount);
			}
			Skills.experience(player, hitpointsExp, Skills.HITPOINTS);
		}
	}
	
	/**
	 * Deals the damage contained within {@code data} to all {@code characters}
	 * within {@code radius} of {@code position}. This method also executes
	 * {@code action} for every character. Please note that this only accounts
	 * for local characters.
	 * @param attacker      the attacker in this combat session.
	 * @param victims       the characters that will be attempted to be hit.
	 * @param position      the position that the radius will be calculated from.
	 * @param radius        the radius of the damage.
	 * @param hits          the amount of hits to calculate.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 * @param action        the action to execute for each victim.
	 */
	public static <E extends EntityNode> void damageCharactersWithin(EntityNode attacker, Iterable<E> victims, Position position, int radius, int hits, CombatType type, boolean checkAccuracy, Consumer<E> action) {
		for(E c : victims) {
			if(c == null)
				continue;
			if(!c.getPosition().withinDistance(position, radius) || c.equals(attacker) || c.equals(attacker.getCombatBuilder().getVictim()) || c.getCurrentHealth() <= 0 || c.isDead())
				continue;
			CombatSessionData data = new CombatSessionData(attacker, c, hits, type, checkAccuracy);
			c.getCombatBuilder().getDamageCache().add(attacker, data.attack(false));
			if(action != null)
				action.accept(c);
		}
	}
	
	/**
	 * Deals the damage contained within {@code data} to all {@code characters}
	 * within {@code radius} of {@code position}. This method also executes
	 * {@code action} for every character. Please note that this only accounts
	 * for local characters.
	 * @param attacker      the attacker in this combat session.
	 * @param victims       the characters that will be attempted to be hit.
	 * @param position      the position that the radius will be calculated from.
	 * @param radius        the radius of the damage.
	 * @param hits          the amount of hits to calculate.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 */
	public static void damageCharactersWithin(EntityNode attacker, Iterable<? extends EntityNode> victims, Position position, int radius, int hits, CombatType type, boolean checkAccuracy) {
		damageCharactersWithin(attacker, victims, position, radius, hits, type, checkAccuracy, null);
	}
	
	/**
	 * Deals the damage contained within {@code data} to all {@link Player}s
	 * within {@code radius} of {@code position}. Please note that this only
	 * accounts for local characters.
	 * @param attacker      the attacker in this combat session.
	 * @param position      the position that the radius will be calculated from.
	 * @param radius        the radius of the damage.
	 * @param hits          the amount of hits to calculate.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 * @param action        the action to execute for each victim.
	 */
	public static void damagePlayersWithin(EntityNode attacker, Position position, int radius, int hits, CombatType type, boolean checkAccuracy, Consumer<Player> action) {
		damageCharactersWithin(attacker, () -> World.getLocalPlayers(attacker), position, radius, hits, type, checkAccuracy, action);
	}
	
	/**
	 * Deals the damage contained within {@code data} to all {@link Player}s
	 * within {@code radius} of {@code position}. This method also executes
	 * {@code action} for every character. Please note that this only accounts
	 * for local characters.
	 * @param attacker      the attacker in this combat session.
	 * @param position      the position that the radius will be calculated from.
	 * @param radius        the radius of the damage.
	 * @param hits          the amount of hits to calculate.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 */
	public static void damagePlayersWithin(EntityNode attacker, Position position, int radius, int hits, CombatType type, boolean checkAccuracy) {
		damagePlayersWithin(attacker, position, radius, hits, type, checkAccuracy, null);
	}
	
	/**
	 * Deals the damage contained within {@code data} to all {@link Npc}s within
	 * {@code radius} of {@code position}. This method also executes
	 * {@code action} for every character. Please note that this only accounts
	 * for local characters.
	 * @param attacker      the attacker in this combat session.
	 * @param position      the position that the radius will be calculated from.
	 * @param radius        the radius of the damage.
	 * @param hits          the amount of hits to calculate.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 * @param action        the action to execute for each victim.
	 */
	public static void damageNpcsWithin(EntityNode attacker, Position position, int radius, int hits, CombatType type, boolean checkAccuracy, Consumer<Npc> action) {
		damageCharactersWithin(attacker, () -> World.getLocalNpcs(attacker), position, radius, hits, type, checkAccuracy, action);
	}
	
	/**
	 * Deals the damage contained within {@code data} to all {@link Npc}s within
	 * {@code radius} of {@code position}. This method also executes
	 * {@code action} for every character. Please note that this only accounts
	 * for local characters.
	 * @param attacker      the attacker in this combat session.
	 * @param position      the position that the radius will be calculated from.
	 * @param radius        the radius of the damage.
	 * @param hits          the amount of hits to calculate.
	 * @param type          the combat type the attacker is using.
	 * @param checkAccuracy determines if accuracy should be calculated for hits.
	 */
	public static void damageNpcsWithin(EntityNode attacker, Position position, int radius, int hits, CombatType type, boolean checkAccuracy) {
		damageNpcsWithin(attacker, position, radius, hits, type, checkAccuracy, null);
	}
	
	/**
	 * Gets the corresponding combat prayer to {@code type}.
	 * @param type the combat type to get the prayer for.
	 * @return the corresponding combat prayer.
	 * @throws IllegalArgumentException if the combat type is invalid.
	 */
	static Prayer[] getProtectingPrayer(CombatType type) {
		switch(type) {
			case MELEE:
				return new Prayer[]{Prayer.PROTECT_FROM_MELEE, Prayer.DEFLECT_MELEE};
			case MAGIC:
				return new Prayer[]{Prayer.PROTECT_FROM_MAGIC, Prayer.DEFLECT_MAGIC};
			case RANGED:
				return new Prayer[]{Prayer.PROTECT_FROM_MISSILES, Prayer.DEFLECT_MISSILES};
			default:
				throw new IllegalArgumentException("Invalid combat type: " + type);
		}
	}
	
	/**
	 * Determines the combat strategy for {@code npcId}.
	 * @param npc the npc to determine the combat strategy for.
	 * @return the combat strategy.
	 */
	public static CombatStrategy determineStrategy(int npc) {
		CombatStrategy combat = DEFAULT_STRATEGIES.get(npc);
		if(combat == null)
			return Combat.newDefaultMeleeStrategy();
		return combat;
	}
	
	/**
	 * Determines which spell {@code npc} will use when they have the
	 * {@link MagicCombatStrategy} combat strategy.
	 * @param npc the npc that needs a spell.
	 * @return the spell that the npc will cast.
	 */
	public static CombatSpells prepareSpellCast(Npc npc) {
		switch(npc.getId()) {
			case 6221:
				return CombatSpells.FIRE_WAVE;
			case 6254:
				return CombatSpells.WIND_WAVE;
			case 6257:
				return CombatSpells.WATER_WAVE;
			case 6278:
				return CombatSpells.SHADOW_RUSH;
			case 2025:
				return CombatSpells.FIRE_WAVE;
			case 13:
			case 172:
			case 174:
				return RandomUtils.random(new CombatSpells[]{CombatSpells.WEAKEN, CombatSpells.FIRE_STRIKE, CombatSpells.EARTH_STRIKE, CombatSpells.WATER_STRIKE});
			default:
				return CombatSpells.FIRE_STRIKE;
		}
	}
	
	/**
	 * Determines if {@code character} is wearing full void.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full void,
	 * {@code false} otherwise.
	 */
	public static boolean isFullVoid(EntityNode character) {
		if(character.getType() == NPC && character.toNpc().getDefinition().getName().contains("Void Knight"))
			return true;
		Item top = ((Player) character).getEquipment().get(Equipment.CHEST_SLOT);
		return top != null && !(top.getId() != 8839 && top.getId() != 10611) && character.toPlayer().getEquipment().containsAll(8840, 8842);
	}
	
	/**
	 * Determines if {@code character} is wearing full veracs.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full veracs,
	 * {@code false} otherwise.
	 */
	public static boolean isFullVeracs(EntityNode character) {
		return character.getType() == NPC ? character.toNpc().getDefinition().getName().equals("Verac the Defiled") : character.toPlayer().getEquipment().containsAll(4753, 4757, 4759, 4755);
	}
	
	/**
	 * Determines if {@code character} is wearing full dharoks.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full dharoks,
	 * {@code false} otherwise.
	 */
	public static boolean isFullDharoks(EntityNode character) {
		return character.getType() == NPC ? character.toNpc().getDefinition().getName().equals("Dharok the Wretched") : character.toPlayer().getEquipment().containsAll(4716, 4720, 4722, 4718);
	}
	
	/**
	 * Determines if {@code character} is wearing full karils.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full karils,
	 * {@code false} otherwise.
	 */
	public static boolean isFullKarils(EntityNode character) {
		return character.getType() == NPC ? character.toNpc().getDefinition().getName().equals("Karil the Tainted") : character.toPlayer().getEquipment().containsAll(4732, 4736, 4738, 4734);
	}
	
	/**
	 * Determines if {@code character} is wearing full ahrims.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full ahrims,
	 * {@code false} otherwise.
	 */
	public static boolean isFullAhrims(EntityNode character) {
		return character.isNpc() ? character.toNpc().getDefinition().getName().equals("Ahrim the Blighted") : character.toPlayer().getEquipment().containsAll(4708, 4712, 4714, 4710);
	}
	
	/**
	 * Determines if {@code character} is wearing full torags.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full torags,
	 * {@code false} otherwise.
	 */
	public static boolean isFullTorags(EntityNode character) {
		return character.getType() == NPC ? character.toNpc().getDefinition().getName().equals("Torag the Corrupted") : character.toPlayer().getEquipment().containsAll(4745, 4749, 4751, 4747);
	}
	
	/**
	 * Determines if {@code character} is wearing full guthans.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full guthans,
	 * {@code false} otherwise.
	 */
	public static boolean isFullGuthans(EntityNode character) {
		return character.getType() == NPC ? character.toNpc().getDefinition().getName().equals("Guthan the Infested") : character.toPlayer().getEquipment().containsAll(4724, 4728, 4730, 4726);
	}
	
	/**
	 * Determines if {@code player} is wielding a crystal bow.
	 * @param player the player to determine this for.
	 * @return {@code true} if the player is wielding a crystal bow,
	 * {@code false} otherwise.
	 */
	public static boolean isCrystalBow(Player player) {
		Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
		if(item == null)
			return false;
		return item.getDefinition().getName().toLowerCase().contains("crystal bow");
	}
	
	/**
	 * Gets the ranged distance based on {@code weapon}.
	 * @param weapon the weapon you have equipped.
	 * @return the ranged distance.
	 * @throws IllegalArgumentException if the weapon interface type is invalid.
	 */
	
	public static int getRangedDistance(WeaponInterface weapon) {
		switch(weapon) {
			case SALAMANDER:
				return 1;
			case CHINCHOMPA:
				return 8;
			case DART:
			case THROWNAXE:
				return 4;
			case KNIFE:
			case JAVELIN:
				return 5;
			case CROSSBOW:
			case LONGBOW:
			case COMPOSITE_BOW:
				return 8;
			case SHORTBOW:
				return 7;
			default:
				throw new IllegalArgumentException("Invalid weapon interface type!");
		}
	}
	
	/**
	 * Gets the delay for the specified {@code type}.
	 * @param type the combat type to retrieve the delay for.
	 * @return the delay for the combat type.
	 * @throws IllegalArgumentException if the combat type is invalid.
	 */
	public static int getDelay(EntityNode character, CombatType type) {
		if(character.isPlayer() && character.toPlayer().getWeapon().equals(WeaponInterface.SALAMANDER)) {
			return 1;
		}
		
		switch(type) {
			case MELEE:
				return 1;
			case RANGED:
				return 2;
			case MAGIC:
				return 3;
			default:
				throw new IllegalArgumentException("Invalid combat type!");
		}
	}
	
	/**
	 * Applies the {@code effect} in any context.
	 * @param effect the effect that must be applied.
	 * @return {@code true} if it was successfully applied, {@code false}
	 * otherwise.
	 */
	public static boolean effect(EntityNode character, CombatEffectType effect) {
		return CombatEffect.EFFECTS.get(effect).start(character);
	}
	
	/**
	 * Calculates the combat level difference for wilderness player vs. player
	 * combat.
	 * @param combatLevel      the combat level of the first person.
	 * @param otherCombatLevel the combat level of the other person.
	 * @return the combat level difference.
	 */
	public static int combatLevelDifference(int combatLevel, int otherCombatLevel) {
		if(combatLevel > otherCombatLevel) {
			return (combatLevel - otherCombatLevel);
		} else if(otherCombatLevel > combatLevel) {
			return (otherCombatLevel - combatLevel);
		} else {
			return 0;
		}
	}
	
	/**
	 * Calculates a pseudo-random hit for {@code character} based on
	 * {@code victim} and {@code type}.
	 * @param character     the character this hit is being calculated for.
	 * @param victim        the victim of this hit that will be used as a factor.
	 * @param type          the character's combat type that will be used as a factor.
	 * @param delay         the delay of the hit to calculate.
	 * @param checkAccuracy the condition to check for the accuracy of the hit.
	 * @return the generated hit, will most likely return a different result if
	 * called on two different occasions even with the same arguments.
	 * @throws IllegalArgumentException if the combat type is invalid.
	 */
	public static Hit calculateRandomHit(EntityNode character, EntityNode victim, CombatType type, int delay, boolean checkAccuracy) {
		int max;
		int hit;
		switch(type) {
			case MELEE:
				max = Combat.calculateMaxMeleeHit(character, victim);
				hit = RandomUtils.inclusive(1, max < 1 ? 2 : max);
				if(Server.DEBUG && character.isPlayer())
					character.toPlayer().message("[DEBUG]: " + "Maximum hit this turn is [" + hit + "].");
				return calculateSoaking(victim, type, new Hit(hit, ((hit * 100f) / max) > 95 ? Hit.HitType.CRITICAL : Hit.HitType.NORMAL, Hit.HitIcon.MELEE, delay, !checkAccuracy || isAccurate(character, victim, type), character.getSlot()));
			case RANGED:
				max = Combat.calculateMaxRangedHit(character, victim);
				hit = RandomUtils.inclusive(1, max);
				if(Server.DEBUG && character.isPlayer())
					character.toPlayer().message("[DEBUG]: " + "Maximum hit this turn is [" + hit + "].");
				return calculateSoaking(victim, type, new Hit(hit, ((hit * 100f) / max) > 95 ? Hit.HitType.CRITICAL : Hit.HitType.NORMAL, Hit.HitIcon.RANGED, delay, !checkAccuracy || isAccurate(character, victim, type), character.getSlot()));
			case MAGIC:
				max = character.getCurrentlyCasting().maximumHit();
				hit = RandomUtils.inclusive(1, max);
				if(Server.DEBUG && character.isPlayer())
					character.toPlayer().message("[DEBUG]: " + "Maximum hit this turn is [" + hit + "].");
				return calculateSoaking(victim, type, new Hit(hit, ((hit * 100f) / max) > 95 ? Hit.HitType.CRITICAL : Hit.HitType.NORMAL, Hit.HitIcon.MAGIC, delay, !checkAccuracy || isAccurate(character, victim, type), character.getSlot()));
			default:
				throw new IllegalArgumentException("Invalid combat type!");
		}
	}
	
	/**
	 * Returns an empty spell which prevents the use of nullpointers.
	 * @param maxHit the max hit of this spell.
	 * @return an empty spell.
	 */
	public static CombatNormalSpell emptySpell(int maxHit) {
		return new CombatNormalSpell() {
			
			@Override
			public int spellId() {
				return 0;
			}
			
			@Override
			public int maximumHit() {
				return maxHit;
			}
			
			@Override
			public Optional<Animation> castAnimation() {
				return Optional.empty();
			}
			
			@Override
			public Optional<Graphic> startGraphic() {
				return Optional.empty();
			}
			
			@Override
			public Optional<Projectile> projectile(EntityNode cast, EntityNode castOn) {
				return Optional.empty();
			}
			
			@Override
			public Optional<Graphic> endGraphic() {
				return Optional.empty();
			}
			
			@Override
			public void executeOnHit(EntityNode cast, EntityNode castOn, boolean accurate, int damage) {
			}
			
			@Override
			public int levelRequired() {
				return 0;
			}
			
			@Override
			public double baseExperience() {
				return 0;
			}
			
			@Override
			public Optional<Item[]> itemsRequired(Player player) {
				return Optional.empty();
			}
			
			@Override
			public Optional<Item[]> equipmentRequired(Player player) {
				return Optional.empty();
			}
			
		};
	}
	
	/**
	 * Calculates a pseudo-random hit for {@code character} based on {@code victim} and {@code type}.
	 * @param victim the victim of this hit that will be used as a factor.
	 * @param hit    the hit being processed in the combat.
	 * @return the generated hit, will most likely return a different result if
	 * called on two different occasions even with the same arguments.
	 * @throws IllegalArgumentException if the combat type is invalid.
	 */
	public static Hit calculateSoaking(EntityNode victim, CombatType type, Hit hit) {
		if(type != CombatType.NONE && victim.isPlayer()) {
			Player player = victim.toPlayer();
			int i = type == CombatType.MAGIC ? 2 : type == CombatType.RANGED ? 1 : 0;
			int reducedDamage = (int) ((hit.getDamage() - 200) * player.getEquipment().getBonuses()[ABSORB_MELEE + i] / 100.D);
			if(hit.getDamage() - reducedDamage > 200 && victim.getCurrentHealth() > 200) {
				if(reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoak(reducedDamage);
				}
			}
		}
		return hit;
	}
	
	/**
	 * Determines if {@code attacker}'s attack will be successful.
	 * @param attacker the attacker that this will be determined for.
	 * @param victim   the victim of the attacker.
	 * @param type     the combat type used by the attacker.
	 * @return {@code true} if the hit was accurate, {@code false} otherwise.
	 */
	public static boolean isAccurate(EntityNode attacker, EntityNode victim, CombatType type) {
		boolean veracEffect = false;
		
		if(type == CombatType.MELEE) {
			if(Combat.isFullVeracs(attacker)) {
				if(RandomUtils.inclusive(8) == 3) {
					veracEffect = true;
				}
			}
		}
		
		double prayerMod = 1;
		double equipmentBonus = 1;
		double specialBonus = 1;
		int styleBonus = 0;
		int bonusType = -1;
		if(attacker.getType() == PLAYER) {
			Player player = (Player) attacker;
			
			equipmentBonus = type == CombatType.MAGIC ? player.getEquipment().getBonuses()[Combat.ATTACK_MAGIC] : player.getEquipment().getBonuses()[player.getFightType().getBonus()];
			bonusType = player.getFightType().getCorrespondingBonus();
			if(type == CombatType.MELEE) {
				if(Prayer.isActivated(player, Prayer.CLARITY_OF_THOUGHT)) {
					prayerMod = 1.05;
				} else if(Prayer.isActivated(player, Prayer.IMPROVED_REFLEXES)) {
					prayerMod = 1.10;
				} else if(Prayer.isActivated(player, Prayer.INCREDIBLE_REFLEXES)) {
					prayerMod = 1.15;
				} else if(Prayer.isActivated(player, Prayer.CHIVALRY)) {
					prayerMod = 1.18;
				} else if(Prayer.isActivated(player, Prayer.PIETY)) {
					prayerMod = 1.23;
				} else if(Prayer.isActivated(player, Prayer.TURMOIL)) {
					if(victim.isPlayer()) {
						//1000 because if strength is 99 / 100 * 10 = 9.9 which is wayy to much.
						prayerMod = (victim.toPlayer().getSkills()[Skills.ATTACK].getRealLevel() / 1000) * 15;
					} else {
						prayerMod = prayerMod + 1.15;
					}
				}
			} else if(type == CombatType.RANGED) {
				if(Prayer.isActivated(player, Prayer.SHARP_EYE)) {
					prayerMod = 1.05;
				} else if(Prayer.isActivated(player, Prayer.HAWK_EYE)) {
					prayerMod = 1.10;
				} else if(Prayer.isActivated(player, Prayer.EAGLE_EYE)) {
					prayerMod = 1.15;
				}
			} else if(type == CombatType.MAGIC) {
				if(Prayer.isActivated(player, Prayer.MYSTIC_WILL)) {
					prayerMod = 1.05;
				} else if(Prayer.isActivated(player, Prayer.MYSTIC_LORE)) {
					prayerMod = 1.10;
				} else if(Prayer.isActivated(player, Prayer.MYSTIC_MIGHT)) {
					prayerMod = 1.15;
				}
			}
			
			if(player.getFightType().getStyle() == FightStyle.ACCURATE) {
				styleBonus = 3;
			} else if(player.getFightType().getStyle() == FightStyle.CONTROLLED) {
				styleBonus = 1;
			}
			
			if(player.isSpecialActivated()) {
				specialBonus = player.getCombatSpecial().getAccuracy();
			}
			
			if(victim.isNpc()) {
				Npc npc = victim.toNpc();
				/* SLAYER */
				if(player.getSlayer().isPresent() && Objects.equals(npc.getDefinition().getSlayerKey(), player.getSlayer().get().getKey())) {
					Item head = player.getEquipment().get(Equipment.HEAD_SLOT);
					if(head != null) {
						if(head.getId() == 13263)
							styleBonus *= 1.12;
						if(head.getId() == 15492)
							styleBonus *= 1.25;//full slayer
						if(head.getId() == 15488 && player.getCombatBuilder().getCombatType() == CombatType.MAGIC)
							styleBonus *= 1.125;//hexcrest
						if(head.getId() == 15490 && player.getCombatBuilder().getCombatType() == CombatType.RANGED)
							styleBonus *= 1.125;//focus sight
						if(head.getDefinition().getName().contains("Black mask") && player.getCombatBuilder().getCombatType() == CombatType.MELEE)
							styleBonus *= 1.125;//black mask
					}
				}
			}
		}
		
		double attackCalc = Math.floor(equipmentBonus + attacker.getBaseAttack(type)) + 8;
		attackCalc *= prayerMod;
		attackCalc += styleBonus;
		
		if(equipmentBonus < -67) {
			attackCalc = RandomUtils.inclusive(8) == 0 ? attackCalc : 0;
		}
		attackCalc *= specialBonus;
		
		equipmentBonus = 1;
		prayerMod = 1;
		styleBonus = 0;
		if(victim.getType() == PLAYER) {
			Player player = (Player) victim;
			
			if(bonusType == -1) {
				equipmentBonus = type == CombatType.MAGIC ? player.getEquipment().getBonuses()[Combat.DEFENCE_MAGIC] : player.getSkills()[Skills.DEFENCE].getLevel();
			} else {
				equipmentBonus = type == CombatType.MAGIC ? player.getEquipment().getBonuses()[Combat.DEFENCE_MAGIC] : player.getEquipment().getBonuses()[bonusType];
			}
			
			if(Prayer.isActivated(player, Prayer.THICK_SKIN)) {
				prayerMod = 1.05;
			} else if(Prayer.isActivated(player, Prayer.ROCK_SKIN)) {
				prayerMod = 1.10;
			} else if(Prayer.isActivated(player, Prayer.STEEL_SKIN)) {
				prayerMod = 1.15;
			} else if(Prayer.isActivated(player, Prayer.CHIVALRY)) {
				prayerMod = 1.20;
			} else if(Prayer.isActivated(player, Prayer.PIETY)) {
				prayerMod = 1.25;
			} else if(Prayer.isActivated(player, Prayer.TURMOIL)) {
				if(victim.isPlayer()) {
					//1000 because if strength is 99 / 100 * 10 = 9.9 which is wayy to much.
					prayerMod = (victim.toPlayer().getSkills()[Skills.DEFENCE].getRealLevel() / 1000) * 15;
				}
				prayerMod = prayerMod + 0.15;
			}
			
			if(player.getFightType().getStyle() == FightStyle.DEFENSIVE) {
				styleBonus = 3;
			} else if(player.getFightType().getStyle() == FightStyle.CONTROLLED) {
				styleBonus = 1;
			}
		}
		
		double defenceCalc = Math.floor(equipmentBonus + victim.getBaseDefence(type)) + 8;
		defenceCalc *= prayerMod;
		defenceCalc += styleBonus;
		
		if(equipmentBonus < -67) {
			defenceCalc = RandomUtils.inclusive(8) == 0 ? defenceCalc : 0;
		}
		if(veracEffect) {
			defenceCalc = 0;
		}
		double A = Math.floor(attackCalc);
		double D = Math.floor(defenceCalc);
		double hitSucceed = A < D ? (A - 1.0) / (2.0 * D) : 1.0 - (D + 1.0) / (2.0 * A);
		hitSucceed = hitSucceed >= 1.0 ? 0.99 : hitSucceed <= 0.0 ? 0.01 : hitSucceed;
		
		if(attacker.isPlayer() && Server.DEBUG) {
			attacker.toPlayer().message("[DEBUG]: Your roll " + "[" + (Math.round(attackCalc * 1000.0) / 1000.0) + "] : " + "Victim's roll [" + (Math.round(defenceCalc * 1000.0) / 1000.0) + "] : Chance to hit [" + (100 * Math.round(hitSucceed * 1000.0) / 1000.0) + "%]");
		}
		return hitSucceed >= RandomUtils.nextDouble();
	}
	
	/**
	 * Calculates the maximum hit that can be dealt using melee for
	 * {@code character}.
	 * @param character the character to calculate the max hit for.
	 * @param victim    the victim being attacked.
	 * @return the maximum hit this character can deal.
	 */
	private static int calculateMaxMeleeHit(EntityNode character, EntityNode victim) {
		int maxHit;
		
		if(character.getType() == NPC) {
			Npc npc = character.toNpc();
			maxHit = npc.getDefinition().getMaxHit();
			if(npc.getWeakenedBy() == CombatWeaken.STRENGTH_LOW || npc.getWeakenedBy() == CombatWeaken.STRENGTH_HIGH)
				maxHit -= (int) ((npc.getWeakenedBy().getRate()) * (maxHit));
			if(npc.getId() == 2026) { //Dharok the wretched
				maxHit += (int) (npc.getMaxHealth() - npc.getCurrentHealth() * 0.2);
			}
			return maxHit;
		}
		
		Player player = (Player) character;
		
		double specialMultiplier = 1;
		// TODO: void melee = 1.2, slayer helm = 1.15, salve amulet = 1.15,
		// salve amulet(e) = 1.2
		int level = player.getSkills()[Skills.STRENGTH].getLevel();
		int bonus = player.getEquipment().getBonuses()[Combat.BONUS_STRENGTH];
		double prayer = 1.0;
		if(Prayer.isActivated(player, Prayer.BURST_OF_STRENGTH)) {
			prayer = 1.05;
		} else if(Prayer.isActivated(player, Prayer.SUPERHUMAN_STRENGTH)) {
			prayer = 1.1;
		} else if(Prayer.isActivated(player, Prayer.ULTIMATE_STRENGTH)) {
			prayer = 1.15;
		} else if(Prayer.isActivated(player, Prayer.CHIVALRY)) {
			prayer = 1.18;
		} else if(Prayer.isActivated(player, Prayer.PIETY)) {
			prayer = 1.23;
		} else if(Prayer.isActivated(player, Prayer.TURMOIL)) {
			if(victim.isPlayer()) {
				//1000 because if strength is 99 / 100 * 10 = 9.9 which is wayy to much.
				prayer = (victim.toPlayer().getSkills()[Skills.STRENGTH].getRealLevel() / 1000) * 10;
			}
			prayer = prayer + 0.23;
		}
		double cumulativeStr = Math.floor(level * prayer);
		if(player.getFightType().getStyle() == FightStyle.AGGRESSIVE) {
			cumulativeStr += 3;
		} else if(player.getFightType().getStyle() == FightStyle.CONTROLLED) {
			cumulativeStr += 1;
		}
		
		if(isFullDharoks(player)) {
			cumulativeStr *= 1.0 + ((player.getMaximumHealth() - player.getCurrentHealth()) * 0.01);
		} else if(isFullVoid(player) && player.getEquipment().contains(11665)) {
			cumulativeStr *= 1.1;
		} else {
		    /*int itemId = player.getEquipment().get(Equipment.HEAD_SLOT).getId();
		    if (itemId >= 8901 && itemId <= 8921) {
				if (victim.getType() == NPC && ((Npc) victim).getId() == player.getSlayer...) {
					return 1.15;
				}
			}*/
		}
		
		if(victim.isNpc()) {
			Npc npc = (Npc) victim;
			if(npc.getWeakenedBy() == CombatWeaken.DEFENCE_LOW) {
				cumulativeStr *= 0.10;
			} else if(npc.getWeakenedBy() == CombatWeaken.DEFENCE_HIGH) {
				cumulativeStr *= 0.20;
			}

			/* SLAYER */
			if(player.getSlayer().isPresent() && Objects.equals(npc.getDefinition().getSlayerKey(), player.getSlayer().get().getKey())) {
				Item head = player.getEquipment().get(Equipment.HEAD_SLOT);
				if(head != null) {
					if(head.getId() == 13263)
						cumulativeStr *= 1.12;
					if(head.getId() == 15492)
						cumulativeStr *= 1.25;//full slayer
					if(head.getId() == 15488 && player.getCombatBuilder().getCombatType() == CombatType.MAGIC)
						cumulativeStr *= 1.125;//hexcrest
					if(head.getId() == 15490 && player.getCombatBuilder().getCombatType() == CombatType.RANGED)
						cumulativeStr *= 1.125;//focus sight
					if(head.getDefinition().getName().contains("Black mask") && player.getCombatBuilder().getCombatType() == CombatType.MELEE)
						cumulativeStr *= 1.125;//black mask
				}
			}
		}
		
		double baseDamage = ((16 + cumulativeStr + (bonus / 8) + ((cumulativeStr * bonus) * 0.016865)) / 10);
		
		if(player.isSpecialActivated()) {
			specialMultiplier = player.getCombatSpecial().getStrength();
		}
		
		maxHit = (int) (baseDamage * specialMultiplier);
		
		if(Server.DEBUG)
			player.message("[DEBUG]: Maximum hit this turn " + "is [" + maxHit + "].");
		return maxHit * 10;
		
	}
	
	/**
	 * Calculates the maximum hit that can be dealt using ranged for
	 * {@code character}.
	 * @param character the character to calculate the max hit for.
	 * @param victim    the victim being attacked.
	 * @return the maximum hit this character can deal.
	 */
	private static int calculateMaxRangedHit(EntityNode character, EntityNode victim) {
		int maxHit = 0;
		if(character.getType() == NPC) {
			Npc npc = (Npc) character;
			maxHit = npc.getDefinition().getMaxHit();
			return maxHit;
		}
		
		Player player = (Player) character;
		
		double specialMultiplier = 1;
		double prayerMultiplier = 1;
		double otherBonusMultiplier = 1;
		int rangedStrength = player.getEquipment().getBonuses()[Combat.BONUS_RANGED_STRENGTH];
		int rangeLevel = player.getSkills()[Skills.RANGED].getLevel();
		int combatStyleBonus = 0;
		
		switch(player.getFightType().getStyle()) {
			case ACCURATE:
				combatStyleBonus = 3;
				break;
			default:
				break;
		}
		
		// if (fullVoidRange(character)) {
		// otherBonusMultiplier = 1.1;
		// }
		
		int effectiveRangeDamage = (int) ((rangeLevel * prayerMultiplier * otherBonusMultiplier) + combatStyleBonus);
		double baseDamage = 1.3 + (effectiveRangeDamage / 10) + (rangedStrength / 80) + ((effectiveRangeDamage * rangedStrength) / 640);
		
		if(player.isSpecialActivated()) {
			specialMultiplier = player.getCombatSpecial().getStrength();
		}
		
		maxHit = (int) (baseDamage * specialMultiplier);
		
		if(Server.DEBUG)
			player.message("[DEBUG]: Maximum hit this turn " + "is [" + maxHit + "].");
		return maxHit * 10;
	}
	
	/**
	 * Determines if the {@code character} can be attacked by the specified {@code attacker}.
	 * @param character the character being attacked.
	 * @param attacker  the attacker attacking the character.
	 * @return {@code true} if the character can be attacked, {@code false} otherwise.
	 */
	public static boolean canBeAttacked(EntityNode character, EntityNode attacker) {
		if(character == null || attacker == null) {
			return false;
		}
		if(!GodwarsFaction.canBeAttacked(character, attacker)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Determines if the character within {@code builder} is close enough to
	 * it's victim to attack.
	 * @param builder the builder that will be checked.
	 * @return {@code true} if the character is close enough, {@code false}
	 * otherwise.
	 */
	public static boolean checkAttackDistance(CombatBuilder builder) {
		if(builder.getStrategy() == null) {
			return false;
		}
		Position attacker = builder.getCharacter().getPosition();
		Position victim = builder.getVictim().getPosition();
		int distance = builder.getStrategy().attackDistance(builder.getCharacter());
		MovementQueue movement = builder.getCharacter().getMovementQueue();
		MovementQueue otherMovement = builder.getVictim().getMovementQueue();
		
		if(!movement.isMovementDone() && !otherMovement.isMovementDone() && !movement.isLockMovement() && !builder.getCharacter().isFrozen()) {
			distance += 1;
			
			// XXX: Might have to change this back to 1 or even remove it, not
			// sure what it's like on actual runescape. Are you allowed to
			// attack when the character is trying to run away from you?
			if(movement.isRunning()) {
				distance += 2;
			}
		}
		
		if(builder.getCombatType() == CombatType.MELEE) {//Melee clipping.
			if(!World.getSimplePathChecker().checkLine(attacker, victim, builder.getCharacter().size())) {
				if(!builder.getCharacter().isFollowing()) {
					builder.getCharacter().getMovementQueue().follow(builder.getVictim());
					builder.getCharacter().setFollowing(true);
				}
				return false;
			}
		} else {//Projectile clipping.
			if(!World.getSimplePathChecker().checkProjectile(attacker, victim)) {
				if(!builder.getCharacter().isFollowing()) {
					builder.getCharacter().getMovementQueue().follow(builder.getVictim());
					builder.getCharacter().setFollowing(true);
				}
				return false;
			}
		}
		
		if(distance == 1 || distance == 2) {
			if(!builder.getCharacter().isFollowing()) {
				builder.getCharacter().getMovementQueue().follow(builder.getVictim());
			}
		} else {
			if(new Boundary(attacker, builder.getCharacter().size()).within(victim, builder.getVictim().size(), distance)) {
				builder.getCharacter().getMovementQueue().reset();
				builder.getCharacter().setFollowing(false);
				return true;
			} else {
				builder.getCharacter().getMovementQueue().follow(builder.getVictim());
				builder.getCharacter().setFollowing(true);
				return false;
			}
		}
		
		return new Boundary(attacker, builder.getCharacter().size()).within(victim, builder.getVictim().size(), distance);
	}
	
	/**
	 * A static factory method that constructs the default melee combat strategy
	 * implementation.
	 * @return the default melee combat strategy implementation.
	 */
	public static CombatStrategy newDefaultMeleeStrategy() {
		return new MeleeCombatStrategy();
	}
	
	/**
	 * A static factory method that constructs the default magic combat strategy
	 * implementation.
	 * @return the default magic combat strategy implementation.
	 */
	public static CombatStrategy newDefaultMagicStrategy() {
		return new MagicCombatStrategy();
	}
	
	/**
	 * A static factory method that constructs the default ranged combat
	 * strategy implementation.
	 * @return the default ranged combat strategy implementation.
	 */
	public static CombatStrategy newDefaultRangedStrategy() {
		return new RangedCombatStrategy();
	}
	
	/**
	 * Used to return a collection which are within distance of the {@code node} type.
	 * @param node   The node type to check distance within.
	 * @param target The node types we are targetting.
	 * @param radius The radius we are targetting {@code targets} at.
	 * @return A {@link List> containing the character nodes which are within distance of the main
	 * {@code node} type.
	 */
	public static <E extends EntityNode> List<E> charactersWithinDistance(EntityNode node, Iterator<E> target, int radius) {
		List<E> list = new ArrayList<>();
		while(target.hasNext()) {
			E character = target.next();
			
			if(character == null) {
				continue;
			}
			
			if(character.getPosition().withinDistance(node.getPosition(), radius) && !character.equals(node) && character.getCurrentHealth() > 0 && !character.isDead())
				list.add(character);
		}
		
		return list;
	}
}
