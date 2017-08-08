package net.edge.content.combat;

import net.edge.Application;
import net.edge.content.combat.formula.Formula;
import net.edge.content.combat.formula.MagicFormula;
import net.edge.content.combat.formula.MeleeFormula;
import net.edge.content.combat.formula.RangedFormula;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.effect.CombatEffect;
import net.edge.content.combat.effect.CombatEffectType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.magic.CombatSpells;
import net.edge.content.combat.strategy.Strategy;
import net.edge.content.combat.strategy.base.MagicStrategy;
import net.edge.content.combat.strategy.base.MeleeStrategy;
import net.edge.content.combat.strategy.base.RangedStrategy;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.item.container.impl.Equipment;
import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.world.locale.Boundary;
import net.edge.world.locale.Position;
import net.edge.world.*;
import net.edge.world.entity.actor.move.MovementQueue;
import net.edge.world.entity.actor.mob.impl.gwd.GodwarsFaction;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.*;
import java.util.function.Consumer;

/**
 * A collection of utility methods and constants related to combat.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatUtil {
	
	/**
	 * The formulas instances.
	 */
	public static final Formula[] FORMULAS = {
			new MeleeFormula(),
			new RangedFormula(),
			new MagicFormula()
	};
	
	/**
	 * The default constructor.
	 * @throws UnsupportedOperationException if this class is instantiated.
	 */
	private CombatUtil() {
		throw new UnsupportedOperationException("This class cannot be instantiated!");
	}
	
	/**
	 * Handles the distribution of experience for the amount of damage dealt in
	 * this combat session attack.
	 * @param builder the combat builder for this combat session.
	 * @param data    the data for this combat session.
	 * @param counter the total amount of damage dealt.
	 */
	static void handleExperience(Combat builder, CombatHit data, int counter) {
		if(builder.getCharacter().isPlayer()) {
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
			for(int skills : data.getExperience()) {
				Skills.experience(player, exp, skills);
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
	public static <E extends Actor> CombatHit[] damageActorsWithin(Actor attacker, Iterable<E> victims, Position position, int radius, int hits, CombatType type, boolean checkAccuracy, Consumer<E> action) {
		List<CombatHit> combatHits = new ArrayList<>();
		for(E c : victims) {
			if(c == null)
				continue;
			if(!c.getPosition().withinDistance(position, radius) || c.same(attacker) || c.same(attacker.getCombat().getVictim()) || c.getCurrentHealth() <= 0 || c.isDead())
				continue;
			CombatHit data = new CombatHit(attacker, c, hits, type, checkAccuracy);
			c.getCombat().getDamageCache().add(attacker, data.attack());
			if(action != null)
				action.accept(c);
			combatHits.add(data);
		}
		return combatHits.toArray(new CombatHit[combatHits.size()]);
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
	public static CombatHit[] damageActorsWithin(Actor attacker, Iterable<? extends Actor> victims, Position position, int radius, int hits, CombatType type, boolean checkAccuracy) {
		return damageActorsWithin(attacker, victims, position, radius, hits, type, checkAccuracy, null);
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
	public static CombatHit[] damagePlayersWithin(Actor attacker, Position position, int radius, int hits, CombatType type, boolean checkAccuracy, Consumer<Player> action) {
		return damageActorsWithin(attacker, () -> World.get().getLocalPlayers(attacker), position, radius, hits, type, checkAccuracy, action);
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
	public static CombatHit[] damagePlayersWithin(Actor attacker, Position position, int radius, int hits, CombatType type, boolean checkAccuracy) {
		return damagePlayersWithin(attacker, position, radius, hits, type, checkAccuracy, null);
	}
	
	/**
	 * Deals the damage contained within {@code data} to all {@link Mob}s within
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
	public static CombatHit[] damageMobsWithin(Actor attacker, Position position, int radius, int hits, CombatType type, boolean checkAccuracy, Consumer<Mob> action) {
		return damageActorsWithin(attacker, () -> World.get().getLocalMobs(attacker), position, radius, hits, type, checkAccuracy, action);
	}
	
	/**
	 * Deals the damage contained within {@code data} to all {@link Mob}s within
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
	public static CombatHit[] damageMobsWithin(Actor attacker, Position position, int radius, int hits, CombatType type, boolean checkAccuracy) {
		return damageMobsWithin(attacker, position, radius, hits, type, checkAccuracy, null);
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
	public static Strategy determineStrategy(int npc) {
		Strategy combat = CombatConstants.DEFAULT_STRATEGIES.get(npc);
		if(combat == null)
			return CombatUtil.newDefaultMeleeStrategy();
		return combat;
	}
	
	/**
	 * Determines which spell {@code mob} will use when they have the
	 * {@link MagicStrategy} combat strategy.
	 * @param mob the mob that needs a spell.
	 * @return the spell that the mob will cast.
	 */
	public static CombatSpells prepareSpellCast(Mob mob) {
		switch(mob.getId()) {
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
			case 3752:
			case 3753:
			case 3754:
			case 3755:
			case 3756:
			case 3757:
			case 3758:
			case 3759:
			case 3760:
			case 376:
				return CombatSpells.FIRE_STRIKE_TORCHER;
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
	public static boolean isFullVoid(Actor character) {
		if(character.isMob() && character.toMob().getDefinition().getName().contains("Void Knight"))
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
	public static boolean isFullVeracs(Actor character) {
		return character.isMob() ? character.toMob().getDefinition().getName().equals("Verac the Defiled") : character.toPlayer().getEquipment().containsAll(4753, 4757, 4759, 4755);
	}
	
	/**
	 * Determines if {@code character} is wearing full dharoks.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full dharoks,
	 * {@code false} otherwise.
	 */
	public static boolean isFullDharoks(Actor character) {
		return character.isMob() ? character.toMob().getDefinition().getName().equals("Dharok the Wretched") : character.toPlayer().getEquipment().containsAll(4716, 4720, 4722, 4718);
	}
	
	/**
	 * Determines if {@code character} is wearing full karils.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full karils,
	 * {@code false} otherwise.
	 */
	public static boolean isFullKarils(Actor character) {
		return character.isMob() ? character.toMob().getDefinition().getName().equals("Karil the Tainted") : character.toPlayer().getEquipment().containsAll(4732, 4736, 4738, 4734);
	}
	
	/**
	 * Determines if {@code character} is wearing full ahrims.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full ahrims,
	 * {@code false} otherwise.
	 */
	public static boolean isFullAhrims(Actor character) {
		return character.isMob() ? character.toMob().getDefinition().getName().equals("Ahrim the Blighted") : character.toPlayer().getEquipment().containsAll(4708, 4712, 4714, 4710);
	}
	
	/**
	 * Determines if {@code character} is wearing full torags.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full torags,
	 * {@code false} otherwise.
	 */
	public static boolean isFullTorags(Actor character) {
		return character.isMob() ? character.toMob().getDefinition().getName().equals("Torag the Corrupted") : character.toPlayer().getEquipment().containsAll(4745, 4749, 4751, 4747);
	}
	
	/**
	 * Determines if {@code character} is wearing full guthans.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full guthans,
	 * {@code false} otherwise.
	 */
	public static boolean isFullGuthans(Actor character) {
		return character.isMob() ? character.toMob().getDefinition().getName().equals("Guthan the Infested") : character.toPlayer().getEquipment().containsAll(4724, 4728, 4730, 4726);
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
	 * @param character the character doing the hit.
	 * @param victim the victim being hit.
	 * @param type the combat type to retrieve the delay for.
	 * @return the delay for the combat type.
	 * @throws IllegalArgumentException if the combat type is invalid.
	 */
	public static int getDelay(Actor character, Actor victim, CombatType type) {
		int delay = character.isPlayer() && victim.isMob() ? 1 : 0;
		if(character.isPlayer() && character.toPlayer().getWeapon().equals(WeaponInterface.SALAMANDER)) {
			return 1 + delay;
		}
		if(type.equals(CombatType.MELEE)) {
			return 1 + delay;
		}
		if(type.equals(CombatType.RANGED)) {
			return 2 + delay;
		}
		if(type.equals(CombatType.MAGIC)) {
			return 3 + delay;
		}
		return 1 + delay;
	}
	
	/**
	 * Applies the {@code effect} in any context.
	 * @param effect the effect that must be applied.
	 * @return {@code true} if it was successfully applied, {@code false}
	 * otherwise.
	 */
	public static boolean effect(Actor character, CombatEffectType effect) {
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
	public static Hit calculateRandomHit(Actor character, Actor victim, CombatType type, int delay, boolean checkAccuracy) {
		if(type == CombatType.NONE)
			throw new IllegalArgumentException("Invalid combat type!");
		Formula formula = FORMULAS[type.ordinal()];
		int max = formula.maxHit(character, victim);
		int hit = RandomUtils.inclusive(1, max < 1 ? 1 : max);
		Hit.HitIcon icon = type == CombatType.MAGIC ? Hit.HitIcon.MAGIC : (type == CombatType.RANGED ? Hit.HitIcon.RANGED : (type == CombatType.MELEE ? Hit.HitIcon.MELEE : Hit.HitIcon.NONE));
		if(Application.DEBUG && character.isPlayer()) {
			character.toPlayer().message("[COMBAT-HIT]: " + "Max: [" + max + "] Hit: [" + hit + "].");
		}
		return calculateSoaking(victim, type, new Hit(hit, ((hit * 100f) / max) > 95 ? Hit.HitType.CRITICAL : Hit.HitType.NORMAL, icon, delay, !checkAccuracy || isAccurate(character, victim, type), character.getSlot()));
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
			public Optional<Projectile> projectile(Actor cast, Actor castOn) {
				return Optional.empty();
			}
			
			@Override
			public Optional<Graphic> endGraphic() {
				return Optional.empty();
			}
			
			@Override
			public void executeOnHit(Actor cast, Actor castOn, boolean accurate, int damage) {
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
	public static Hit calculateSoaking(Actor victim, CombatType type, Hit hit) {
		if(type != CombatType.NONE && victim.isPlayer()) {
			Player player = victim.toPlayer();
			int i = type == CombatType.MAGIC ? 2 : type == CombatType.RANGED ? 1 : 0;
			int reducedDamage = (int) ((hit.getDamage() - 200) * player.getEquipment().getBonuses()[CombatConstants.ABSORB_MELEE + i] / 100.D);
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
	public static boolean isAccurate(Actor attacker, Actor victim, CombatType type) {
		if(type == CombatType.NONE)
			return false;
		Formula formula = FORMULAS[type.ordinal()];
		
		double roll;
		double attackRoll = formula.attack(attacker, victim);
		double defenceRoll = formula.defence(attacker, victim);
		if(attackRoll > defenceRoll) {
			roll = 1 - (defenceRoll+2) / (2*(attackRoll+1));
		} else {
			roll = attackRoll / (2*(defenceRoll+1));
		}
		if(Application.DEBUG && attacker.isPlayer())
			attacker.toPlayer().message("[" + attacker.toPlayer().getFormatUsername() + "]: " + attackRoll + ", Defender: [" + defenceRoll + "], roll: " + roll);
		if(Application.DEBUG && victim.isPlayer())
			victim.toPlayer().message("[" + attacker.toMob().getDefinition().getName() + "]: " + attackRoll + ", Defender: [" + defenceRoll + "], roll: " + roll);
		return RandomUtils.success(roll);
	}

	/**
	 * Determines if the {@code character} can be attacked by the specified {@code attacker}.
	 * @param character the character being attacked.
	 * @param attacker  the attacker attacking the character.
	 * @return {@code true} if the character can be attacked, {@code false} otherwise.
	 */
	public static boolean canBeAttacked(Actor character, Actor attacker) {
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
	public static boolean checkAttackDistance(Combat builder) {
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
			if(builder.getCharacter().getAttr().get("master_archery").getBoolean())
				return true;
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
	public static Strategy newDefaultMeleeStrategy() {
		return new MeleeStrategy();
	}
	
	/**
	 * A static factory method that constructs the default magic combat strategy
	 * implementation.
	 * @return the default magic combat strategy implementation.
	 */
	public static Strategy newDefaultMagicStrategy() {
		return new MagicStrategy();
	}
	
	/**
	 * A static factory method that constructs the default ranged combat
	 * strategy implementation.
	 * @return the default ranged combat strategy implementation.
	 */
	public static Strategy newDefaultRangedStrategy() {
		return new RangedStrategy();
	}
	
	public static <E extends Actor> List<E> actorsWithinDistance(Actor node, Iterator<E> target, int radius) {
		List<E> list = new ArrayList<>();
		while(target.hasNext()) {
			E character = target.next();
			if(character == null) {
				continue;
			}
			if(character.getPosition().withinDistance(node.getPosition(), radius) && !character.same(node) && character.getCurrentHealth() > 0 && !character.isDead())
				list.add(character);
		}
		return list;
	}
	
	public static void playersWithinDistance(Actor node, Consumer<Player> action, int radius) {
		node.getLocalPlayers().forEach(p -> {
			if(p.getPosition().withinDistance(node.getPosition(), radius) && !p.same(node) && p.getCurrentHealth() > 0 && !p.isDead())
				action.accept(p);
		});
	}
	
	public static void mobsWithinDistance(Actor node, Consumer<Mob> action, int radius) {
		node.getLocalMobs().forEach(p -> {
			if(p.getPosition().withinDistance(node.getPosition(), radius) && !p.same(node) && p.getCurrentHealth() > 0 && !p.isDead())
				action.accept(p);
		});
	}
	
	public static void relativeWithinDistance(Actor node, Consumer<Actor> action, int radius) {
		if(node.inWilderness() && (node.getCombat().getVictim() != null && node.getCombat().getVictim().isPlayer())) {
			node.getLocalPlayers().forEach(p -> {
				if(p.getPosition().withinDistance(node.getPosition(), radius) && !p.same(node) && p.getCurrentHealth() > 0 && !p.isDead())
					action.accept(p);
			});
		} else {
			node.getLocalMobs().forEach(p -> {
				if(p.getPosition().withinDistance(node.getPosition(), radius) && !p.same(node) && p.getCurrentHealth() > 0 && !p.isDead())
					action.accept(p);
			});
		}
	}
}
