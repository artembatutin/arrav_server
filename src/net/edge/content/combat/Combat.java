package net.edge.content.combat;

import net.edge.GameServer;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.effect.CombatEffect;
import net.edge.content.combat.effect.CombatEffectType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.magic.CombatSpells;
import net.edge.content.combat.magic.CombatWeaken;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.content.combat.strategy.base.MagicCombatStrategy;
import net.edge.content.combat.strategy.base.MeleeCombatStrategy;
import net.edge.content.combat.strategy.base.RangedCombatStrategy;
import net.edge.content.combat.weapon.FightStyle;
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
public final class Combat {
	
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
	static void handleExperience(CombatBuilder builder, CombatHit data, int counter) {
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
	public static <E extends Actor> CombatHit[] damageCharactersWithin(Actor attacker, Iterable<E> victims, Position position, int radius, int hits, CombatType type, boolean checkAccuracy, Consumer<E> action) {
		List<CombatHit> combatHits = new ArrayList<>();
		for(E c : victims) {
			if(c == null)
				continue;
			if(!c.getPosition().withinDistance(position, radius) || c.same(attacker) || c.same(attacker.getCombatBuilder().getVictim()) || c.getCurrentHealth() <= 0 || c.isDead())
				continue;
			CombatHit data = new CombatHit(attacker, c, hits, type, checkAccuracy);
			c.getCombatBuilder().getDamageCache().add(attacker, data.attack());
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
	public static CombatHit[] damageCharactersWithin(Actor attacker, Iterable<? extends Actor> victims, Position position, int radius, int hits, CombatType type, boolean checkAccuracy) {
		return damageCharactersWithin(attacker, victims, position, radius, hits, type, checkAccuracy, null);
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
		return damageCharactersWithin(attacker, () -> World.get().getLocalPlayers(attacker), position, radius, hits, type, checkAccuracy, action);
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
	public static CombatHit[] damageNpcsWithin(Actor attacker, Position position, int radius, int hits, CombatType type, boolean checkAccuracy, Consumer<Mob> action) {
		return damageCharactersWithin(attacker, () -> World.get().getLocalNpcs(attacker), position, radius, hits, type, checkAccuracy, action);
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
	public static CombatHit[] damageNpcsWithin(Actor attacker, Position position, int radius, int hits, CombatType type, boolean checkAccuracy) {
		return damageNpcsWithin(attacker, position, radius, hits, type, checkAccuracy, null);
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
		CombatStrategy combat = CombatConstants.DEFAULT_STRATEGIES.get(npc);
		if(combat == null)
			return Combat.newDefaultMeleeStrategy();
		return combat;
	}
	
	/**
	 * Determines which spell {@code mob} will use when they have the
	 * {@link MagicCombatStrategy} combat strategy.
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
		if(character.isNpc() && character.toNpc().getDefinition().getName().contains("Void Knight"))
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
		return character.isNpc() ? character.toNpc().getDefinition().getName().equals("Verac the Defiled") : character.toPlayer().getEquipment().containsAll(4753, 4757, 4759, 4755);
	}
	
	/**
	 * Determines if {@code character} is wearing full dharoks.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full dharoks,
	 * {@code false} otherwise.
	 */
	public static boolean isFullDharoks(Actor character) {
		return character.isNpc() ? character.toNpc().getDefinition().getName().equals("Dharok the Wretched") : character.toPlayer().getEquipment().containsAll(4716, 4720, 4722, 4718);
	}
	
	/**
	 * Determines if {@code character} is wearing full karils.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full karils,
	 * {@code false} otherwise.
	 */
	public static boolean isFullKarils(Actor character) {
		return character.isNpc() ? character.toNpc().getDefinition().getName().equals("Karil the Tainted") : character.toPlayer().getEquipment().containsAll(4732, 4736, 4738, 4734);
	}
	
	/**
	 * Determines if {@code character} is wearing full ahrims.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full ahrims,
	 * {@code false} otherwise.
	 */
	public static boolean isFullAhrims(Actor character) {
		return character.isNpc() ? character.toNpc().getDefinition().getName().equals("Ahrim the Blighted") : character.toPlayer().getEquipment().containsAll(4708, 4712, 4714, 4710);
	}
	
	/**
	 * Determines if {@code character} is wearing full torags.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full torags,
	 * {@code false} otherwise.
	 */
	public static boolean isFullTorags(Actor character) {
		return character.isNpc() ? character.toNpc().getDefinition().getName().equals("Torag the Corrupted") : character.toPlayer().getEquipment().containsAll(4745, 4749, 4751, 4747);
	}
	
	/**
	 * Determines if {@code character} is wearing full guthans.
	 * @param character the character to determine this for.
	 * @return {@code true} if this character is wearing full guthans,
	 * {@code false} otherwise.
	 */
	public static boolean isFullGuthans(Actor character) {
		return character.isNpc() ? character.toNpc().getDefinition().getName().equals("Guthan the Infested") : character.toPlayer().getEquipment().containsAll(4724, 4728, 4730, 4726);
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
		int delay = character.isPlayer() && victim.isNpc() ? 1 : 0;
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
		int max;
		int hit;
		switch(type) {
			case MELEE:
				max = Combat.calculateMaxMeleeHit(character, victim);
				hit = RandomUtils.inclusive(1, max < 1 ? 1 : max);
				if(GameServer.DEBUG && character.isPlayer())
					character.toPlayer().message("[DEBUG]: " + "Maximum hit this turn is [" + hit + "].");
				return calculateSoaking(victim, type, new Hit(hit, ((hit * 100f) / max) > 95 ? Hit.HitType.CRITICAL : Hit.HitType.NORMAL, Hit.HitIcon.MELEE, delay, !checkAccuracy || isAccurate(character, victim, type), character.getSlot()));
			case RANGED:
				max = Combat.calculateMaxRangedHit(character, victim);
				hit = RandomUtils.inclusive(1, max < 1 ? 1 : max);
				if(GameServer.DEBUG && character.isPlayer())
					character.toPlayer().message("[DEBUG]: " + "Maximum hit this turn is [" + hit + "].");
				return calculateSoaking(victim, type, new Hit(hit, ((hit * 100f) / max) > 95 ? Hit.HitType.CRITICAL : Hit.HitType.NORMAL, Hit.HitIcon.RANGED, delay, !checkAccuracy || isAccurate(character, victim, type), character.getSlot()));
			case MAGIC:
				max = character.getCurrentlyCasting().maximumHit();
				if(victim.isPlayer() && character.isNpc()) {
					Player p = victim.toPlayer();
					if(p.isIronMan())//Iron man monsters tougher.
						max *= 1.2;
				}
				hit = RandomUtils.inclusive(1, max < 1 ? 1 : max);
				if(GameServer.DEBUG && character.isPlayer())
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
		if(attacker.isPlayer()) {
			Player player = (Player) attacker;
			
			equipmentBonus = type == CombatType.MAGIC ? player.getEquipment().getBonuses()[CombatConstants.ATTACK_MAGIC] : player.getEquipment().getBonuses()[player.getFightType().getBonus()];
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
				Mob mob = victim.toNpc();
				/* SLAYER */
				if(player.getSlayer().isPresent() && Objects.equals(mob.getDefinition().getSlayerKey(), player.getSlayer().get().getKey())) {
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
		if(victim.isPlayer()) {
			Player player = (Player) victim;
			
			if(bonusType == -1) {
				equipmentBonus = type == CombatType.MAGIC ? player.getEquipment().getBonuses()[CombatConstants.DEFENCE_MAGIC] : player.getSkills()[Skills.DEFENCE].getLevel();
			} else {
				equipmentBonus = type == CombatType.MAGIC ? player.getEquipment().getBonuses()[CombatConstants.DEFENCE_MAGIC] : player.getEquipment().getBonuses()[bonusType];
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
		
		if(attacker.isPlayer() && GameServer.DEBUG) {
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
	private static int calculateMaxMeleeHit(Actor character, Actor victim) {
		int maxHit;
		
		if(character.isNpc()) {
			Mob mob = character.toNpc();
			maxHit = mob.getDefinition().getMaxHit();
			if(victim.isPlayer()) {
				Player p = victim.toPlayer();
				if(p.isIronMan())//Iron man monsters tougher.
					maxHit *= 1.2;
			}
			if(mob.getWeakenedBy() == CombatWeaken.STRENGTH_LOW || mob.getWeakenedBy() == CombatWeaken.STRENGTH_HIGH)
				maxHit -= (int) ((mob.getWeakenedBy().getRate()) * (maxHit));
			if(mob.getId() == 2026) { //Dharok the wretched
				maxHit += (int) ((mob.getMaxHealth() / 10) - (mob.getCurrentHealth() / 10) * 0.15);
			}
			return maxHit;
		}
		
		Player player = (Player) character;
		
		double specialMultiplier = 1;
		// TODO: void melee = 1.2, slayer helm = 1.15, salve amulet = 1.15,
		// salve amulet(e) = 1.2
		int level = player.getSkills()[Skills.STRENGTH].getLevel();
		int bonus = player.getEquipment().getBonuses()[CombatConstants.BONUS_STRENGTH];
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
			cumulativeStr *= 1.0 + (((player.getMaximumHealth() / 10) - (player.getCurrentHealth() / 10)) * 0.01);
		} else if(isFullVoid(player) && player.getEquipment().contains(11665)) {
			cumulativeStr *= 1.1;
		} else {
		    /*int itemId = player.getEquipment().get(Equipment.HEAD_SLOT).getId();
		    if (itemId >= 8901 && itemId <= 8921) {
				if (victim.isNpc() && ((Mob) victim).getId() == player.getSlayer...) {
					return 1.15;
				}
			}*/
		}
		
		if(victim.isNpc()) {
			Mob mob = (Mob) victim;
			if(mob.getWeakenedBy() == CombatWeaken.DEFENCE_LOW) {
				cumulativeStr *= 0.10;
			} else if(mob.getWeakenedBy() == CombatWeaken.DEFENCE_HIGH) {
				cumulativeStr *= 0.20;
			}

			/* SLAYER */
			if(player.getSlayer().isPresent() && Objects.equals(mob.getDefinition().getSlayerKey(), player.getSlayer().get().getKey())) {
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
		
		if(GameServer.DEBUG)
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
	private static int calculateMaxRangedHit(Actor character, Actor victim) {
		int maxHit = 0;
		if(character.isNpc()) {
			Mob mob = (Mob) character;
			maxHit = mob.getDefinition().getMaxHit();
			if(victim.isPlayer()) {
				Player p = victim.toPlayer();
				if(p.isIronMan())//Iron man monsters tougher.
					maxHit *= 1.2;
			}
			return maxHit;
		}
		
		Player player = (Player) character;
		
		double specialMultiplier = 1;
		double prayerMultiplier = 1;
		double otherBonusMultiplier = 1;
		int rangedStrength = player.getEquipment().getBonuses()[CombatConstants.BONUS_RANGED_STRENGTH];
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
		
		if(GameServer.DEBUG)
			player.message("[DEBUG]: Maximum hit this turn " + "is [" + maxHit + "].");
		return maxHit * 10;
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
	public static <E extends Actor> List<E> charactersWithinDistance(Actor node, Iterator<E> target, int radius) {
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
}
