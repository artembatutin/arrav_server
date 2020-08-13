package com.rageps.content.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rageps.GameConstants;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.content.skill.Skill;
import com.rageps.content.skill.Skills;
import com.rageps.world.World;
import com.rageps.action.impl.ItemAction;
import com.rageps.net.packet.out.SendConfig;
import com.rageps.net.packet.out.SendEnergy;
import com.rageps.task.Task;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.combat.CombatUtil;
import com.rageps.world.entity.actor.combat.effect.CombatEffectType;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Inventory;

import java.util.EnumSet;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * The enumerated type managing consumable potion types.
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author lare96 <http://github.com/lare96>
 */
public enum PotionConsumable {
	OVERLOAD_FLASKS(14301, 14299, 14297, 14295, 14293, 14291) {
		@Override
		public void onEffect(Player player) {
			OVERLOAD.onEffect(player);
		}
		
		@Override
		public boolean canDrink(Player player) {
			return OVERLOAD.canDrink(player);
		}
	}, OVERLOAD(15332, 15333, 15334, 15335) {
		@Override
		public boolean canDrink(Player player) {
			if(player.inWilderness()) {
				player.message("You can't drink this potion in the wilderness.");
				return false;
			}
			if(player.getOverloadEffect() != null) {
				player.message("You already have the effects of overload upon you.");
				return false;
			}
			if(player.getCurrentHealth() < (player.getMaximumHealth() / 2)) {
				player.message("You don't have enough health to drink this potion.");
				return false;
			}
			return true;
		}
		
		/**
		 * The method executed when this potion type activated.
		 * @param player the player to execute this effect for.
		 */
		@Override
		public void onEffect(Player player) {
			player.applyOverloadEffect();
		}
	}, COMBAT(9739, 9741, 9743, 9745) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onBasicEffect(player, Skills.STRENGTH, BoostType.MODERATE);
			PotionConsumable.onBasicEffect(player, Skills.ATTACK, BoostType.MODERATE);
		}
	}, EXTREME_STRENGTH_FLASKS(14361, 14359, 14357, 14355, 14353, 14351) {
		@Override
		public boolean canDrink(Player player) {
			return EXTREME_STRENGTH.canDrink(player);
		}
		
		@Override
		public void onEffect(Player player) {
			EXTREME_STRENGTH.onEffect(player);
		}
	}, EXTREME_STRENGTH(15312, 15313, 15314, 15315) {
		@Override
		public boolean canDrink(Player player) {
			if(player.inWilderness()) {
				player.message("You can't drink this potion in the wilderness.");
				return false;
			}
			return true;
		}
		
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onBasicEffect(player, Skills.STRENGTH, BoostType.EXTREME);
		}
	}, EXTREME_ATTACK_FLASKS(14373, 14371, 14369, 14367, 14365, 14363) {
		@Override
		public boolean canDrink(Player player) {
			return EXTREME_ATTACK.canDrink(player);
		}
		
		@Override
		public void onEffect(Player player) {
			EXTREME_ATTACK_FLASKS.onEffect(player);
		}
	}, EXTREME_ATTACK(15308, 15309, 15310, 15311) {
		@Override
		public boolean canDrink(Player player) {
			if(player.inWilderness()) {
				player.message("You can't drink this potion in the wilderness.");
				return false;
			}
			return true;
		}
		
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onBasicEffect(player, Skills.ATTACK, BoostType.EXTREME);
		}
	}, EXTREME_DEFENCE_FLASKS(14349, 14347, 14345, 14343, 14341, 14339) {
		@Override
		public boolean canDrink(Player player) {
			return EXTREME_DEFENCE.canDrink(player);
		}
		
		@Override
		public void onEffect(Player player) {
			EXTREME_DEFENCE.onEffect(player);
		}
	}, EXTREME_DEFENCE(15316, 15317, 15318, 15319) {
		@Override
		public boolean canDrink(Player player) {
			if(player.inWilderness()) {
				player.message("You can't drink this potion in the wilderness.");
				return false;
			}
			return true;
		}
		
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onBasicEffect(player, Skills.DEFENCE, BoostType.EXTREME);
		}
	}, EXTREME_RANGING_FLASKS(14325, 14323, 14321, 14319, 14317, 14315) {
		@Override
		public void onEffect(Player player) {
			EXTREME_RANGING.onEffect(player);
		}
		
		@Override
		public boolean canDrink(Player player) {
			return EXTREME_RANGING.canDrink(player);
		}
	}, EXTREME_RANGING(15324, 15325, 15326, 15327) {
		@Override
		public boolean canDrink(Player player) {
			if(player.inWilderness()) {
				player.message("You can't drink this potion in the wilderness.");
				return false;
			}
			return true;
		}
		
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onBasicEffect(player, Skills.RANGED, BoostType.MODERATE_EXTREME);
		}
	}, EXTREME_MAGIC_FLASKS(14337, 14335, 14333, 14331, 14329, 14327) {
		@Override
		public void onEffect(Player player) {
			EXTREME_MAGIC.onEffect(player);
		}
		
		@Override
		public boolean canDrink(Player player) {
			return EXTREME_MAGIC.canDrink(player);
		}
	}, EXTREME_MAGIC(15320, 15321, 15322, 15323) {
		@Override
		public boolean canDrink(Player player) {
			if(player.inWilderness()) {
				player.message("You can't drink this potion in the wilderness.");
				return false;
			}
			return true;
		}
		
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onBasicEffect(player, Skills.MAGIC, BoostType.EXTREME_MAGIC);
		}
	}, RECOVER_SPECIAL_FLASKS(14385, 14383, 14381, 14379, 14377, 14375) {
		@Override
		public boolean canDrink(Player player) {
			return RECOVER_SPECIAL.canDrink(player);
		}
		
		@Override
		public void onEffect(Player player) {
			RECOVER_SPECIAL.onEffect(player);
		}
	}, RECOVER_SPECIAL(15300, 15301, 15302, 15303) {
		@Override
		public boolean canDrink(Player player) {
			if(player.inWilderness()) {
				player.message("You can't drink this potion in the wilderness.");
				return false;
			}
			if(!player.getSpecialAttackRestoreTimer().elapsed(30, TimeUnit.SECONDS)) {
				return false;
			}
			return true;
		}
		
		@Override
		public void onEffect(Player player) {
			int value = 25 + player.playerData.getSpecialPercentage().get();
			
			if(value > 100) {
				value = 100;
			}
			
			player.playerData.getSpecialPercentage().set(value);
			player.getSpecialAttackRestoreTimer().reset();
		}
	}, ZAMORAK_BREW(2450, 189, 191, 193) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onZamorakEffect(player);
		}
	}, SARADOMIN_BREW_FLASKS(14128, 14126, 14124, 14122, 14419, 14417) {
		@Override
		public void onEffect(Player player) {
			SARADOMIN_BREW.onEffect(player);
		}
	}, SARADOMIN_BREW(6685, 6687, 6689, 6691) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onSaradominEffect(player);
		}
	}, ANTIDOTE_PLUS(5943, 5945, 5947, 5949) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onAntiPoisonEffect(player, true, 1000);
		}
	}, ANTIDOTE_PLUS_PLUS(5952, 5954, 5956, 5958) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onAntiPoisonEffect(player, true, 1200);
		}
	}, AGILITY_POTION(3032, 3034, 3036, 3038) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onAgilityEffect(player);
		}
	}, FISHING_POTION(2438, 151, 153, 155) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onFishingEffect(player);
		}
	}, RANGE_FLASKS(14152, 14150, 14148, 14146, 14144, 14142) {
		@Override
		public void onEffect(Player player) {
			RANGE_POTIONS.onEffect(player);
		}
	}, RANGE_POTIONS(2444, 169, 171, 173) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onBasicEffect(player, Skills.RANGED, BoostType.MODERATE);
		}
	}, ENERGY_POTIONS(3008, 3010, 3012, 3014) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onEnergyEffect(player, false);
		}
	}, SUPER_ENERGY_POTIONS(3016, 3018, 3020, 3022) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onEnergyEffect(player, true);
		}
	}, MAGIC_FLASKS(14403, 14401, 14399, 14397, 14395, 14393) {
		@Override
		public void onEffect(Player player) {
			MAGIC_POTIONS.onEffect(player);
		}
	}, MAGIC_POTIONS(3040, 3042, 3044, 3046) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onBasicEffect(player, Skills.MAGIC, BoostType.NORMAL);
		}
	}, DEFENCE_POTIONS(2432, 133, 135, 137) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onBasicEffect(player, Skills.DEFENCE, BoostType.NORMAL);
		}
	}, STRENGTH_POTIONS(113, 115, 117, 119) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onBasicEffect(player, Skills.STRENGTH, BoostType.NORMAL);
		}
	}, ATTACK_POTIONS(2428, 121, 123, 125) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onBasicEffect(player, Skills.ATTACK, BoostType.NORMAL);
		}
	}, SUPER_DEFENCE_FLASKS(14164, 14162, 14160, 14158, 14156, 14154) {
		@Override
		public void onEffect(Player player) {
			SUPER_DEFENCE_POTIONS.onEffect(player);
		}
	}, SUPER_DEFENCE_POTIONS(2442, 163, 165, 167) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onBasicEffect(player, Skills.DEFENCE, BoostType.SUPER);
		}
	}, SUPER_ATTACK_FLASKS(14188, 14186, 14184, 14182, 14180, 14178) {
		@Override
		public void onEffect(Player player) {
			SUPER_ATTACK_POTIONS.onEffect(player);
		}
	}, SUPER_ATTACK_POTIONS(2436, 145, 147, 149) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onBasicEffect(player, Skills.ATTACK, BoostType.SUPER);
		}
	}, SUPER_STRENGTH_FLASKS(14176, 14174, 14172, 14170, 14168, 14166) {
		@Override
		public void onEffect(Player player) {
			SUPER_STRENGTH_POTIONS.onEffect(player);
		}
	}, SUPER_STRENGTH_POTIONS(2440, 157, 159, 161) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onBasicEffect(player, Skills.STRENGTH, BoostType.SUPER);
		}
	}, RESTORE_POTIONS(2430, 127, 129, 131) {
		@Override
		public void onEffect(Player player) {
			onRestoreEffect(player, false);
		}
	}, SUPER_RESTORE_FLASKS(14415, 14413, 14411, 14409, 14407, 14405) {
		@Override
		public void onEffect(Player player) {
			SUPER_RESTORE_POTIONS.onEffect(player);
		}
	}, SUPER_RESTORE_POTIONS(3024, 3026, 3028, 3030) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onRestoreEffect(player, true);
			
			Skill skill = player.getSkills()[Skills.PRAYER];
			int realLevel = skill.getRealLevel();
			
			skill.increaseLevel((int) Math.floor(8 + (realLevel * 0.25)), realLevel);
			
			Skills.refresh(player, Skills.PRAYER);
		}
	}, PRAYER_FLASKS(14200, 14198, 14196, 14194, 14192, 14190) {
		@Override
		public void onEffect(Player player) {
			PRAYER_POTIONS.onEffect(player);
		}
	}, PRAYER_POTIONS(2434, 139, 141, 143) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onPrayerEffect(player, false);
		}
	}, SUPER_PRAYER_FLASKS(14313, 14311, 14309, 14307, 14305, 14303) {
		@Override
		public void onEffect(Player player) {
			SUPER_PRAYER_POTIONS.onEffect(player);
		}
	}, SUPER_PRAYER_POTIONS(15328, 15329, 15330, 15331) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onPrayerEffect(player, true);
		}
	}, ANTIFIRE_POTIONS(2452, 2454, 2456, 2458) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onAntiFireEffect(player, false);
		}
	}, SUPER_ANTIFIRE_POTIONS(15304, 15305, 15306, 15307) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onAntiFireEffect(player, true);
		}
	}, ANTIPOISON_POTIONS(2446, 175, 177, 179) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onAntiPoisonEffect(player, false, 0);
		}
	}, SUPER_ANTIPOISON_FLASKS(14140, 14138, 14136, 14134, 14132, 14130) {
		@Override
		public void onEffect(Player player) {
			SUPER_ANTIFIRE_POTIONS.onEffect(player);
		}
	}, SUPER_ANTIPOISON_POTIONS(2448, 181, 183, 185) {
		@Override
		public void onEffect(Player player) {
			PotionConsumable.onAntiPoisonEffect(player, true, 500);
		}
	};
	
	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<PotionConsumable> VALUES = Sets.immutableEnumSet(EnumSet.allOf(PotionConsumable.class));
	
	/**
	 * The default item representing the final potion dose.
	 */
	private static final Item VIAL = new Item(229);
	
	/**
	 * The default item representing the final flask dose.
	 */
	private static final Item FLASK = new Item(14207);
	
	/**
	 * The identifiers which represent this potion type.
	 */
	private final int[] ids;
	
	/**
	 * Create a new {@link PotionConsumable}.
	 * @param ids the identifiers which represent this potion type.
	 */
	PotionConsumable(int... ids) {
		this.ids = ids;
	}
	
	public static void action() {
		for(PotionConsumable potion : PotionConsumable.values()) {
			ItemAction e = new ItemAction() {
				@Override
				public boolean click(Player player, Item item, int container, int slot, int click) {
					if(container != Inventory.INVENTORY_DISPLAY_ID)
						return true;
					if(player.isDead() || !player.consumeDelay.get("DRINKS").elapsed(GameConstants.CONSUME_DELAY))
						return false;
					if(!MinigameHandler.execute(player, m -> m.canPot(player, potion))) {
						return false;
					}
					if(!potion.canDrink(player)) {
						return false;
					}
					player.animation(new Animation(829));
					player.consumeDelay.get("DRINKS").reset();
					boolean removed = player.getInventory().remove(item, slot) == 1;
					if(removed) {
						int length = potion.getIds().length;
						Item replace = length > 4 ? FLASK : VIAL;
						for(int index = 0; index < length; index++) {
							if(potion.getIds()[index] == item.getId() && index + 1 < length) {
								replace = new Item(potion.getIds()[index + 1]);
							}
						}
						player.getInventory().add(replace, slot, true);
						potion.onEffect(player);
					}
					return true;
				}
			};
			for(int p : potion.getIds())
				e.register(p);
		}
	}
	
	/**
	 * The method that executes the fishing potion action.
	 * @param player the player to do this action for.
	 */
	private static void onFishingEffect(Player player) {
		Skill fishing = player.getSkills()[Skills.FISHING];
		fishing.increaseLevelReal(3);
		Skills.refresh(player, Skills.FISHING);
	}
	
	/**
	 * The method that executes the agility potion action.
	 * @param player the player to do this action for.
	 */
	private static void onAgilityEffect(Player player) {
		Skill agility = player.getSkills()[Skills.AGILITY];
		agility.increaseLevelReal(3);
		Skills.refresh(player, Skills.AGILITY);
	}
	
	/**
	 * The method that executes the Saradomin brew action.
	 * @param player the player to do this action for.
	 */
	private static void onSaradominEffect(Player player) {
		Skill attack = player.getSkills()[Skills.ATTACK];
		Skill strength = player.getSkills()[Skills.STRENGTH];
		Skill defence = player.getSkills()[Skills.DEFENCE];
		Skill hp = player.getSkills()[Skills.HITPOINTS];
		Skill ranged = player.getSkills()[Skills.RANGED];
		Skill magic = player.getSkills()[Skills.MAGIC];
		defence.increaseLevelReal((int) Math.floor(2 + (0.20 * defence.getRealLevel())));
		int hpCalc = (int) Math.floor(20 + (0.15 * (hp.getRealLevel() * 10)));
		hp.increaseLevel(hpCalc, (hp.getRealLevel() * 10) + hpCalc);
		attack.decreaseLevel((int) Math.floor(0.10 * attack.getCurrentLevel()));
		strength.decreaseLevel((int) Math.floor(0.10 * strength.getCurrentLevel()));
		magic.decreaseLevel((int) Math.floor(0.10 * magic.getCurrentLevel()));
		ranged.decreaseLevel((int) Math.floor(0.10 * ranged.getCurrentLevel()));
		Skills.refresh(player, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.HITPOINTS, Skills.RANGED, Skills.MAGIC);
	}
	
	/**
	 * The method that executes the Zamorak brew action.
	 * @param player the player to do this action for.
	 */
	private static void onZamorakEffect(Player player) {
		Skill attack = player.getSkills()[Skills.ATTACK];
		Skill strength = player.getSkills()[Skills.STRENGTH];
		Skill defence = player.getSkills()[Skills.DEFENCE];
		Skill hp = player.getSkills()[Skills.HITPOINTS];
		Skill prayer = player.getSkills()[Skills.PRAYER];
		attack.increaseLevelReal((int) Math.floor(2 + (0.20 * attack.getRealLevel())));
		strength.increaseLevelReal((int) Math.floor(2 + (0.12 * strength.getRealLevel())));
		defence.decreaseLevelReal((int) Math.floor(2 + (0.10 * defence.getRealLevel())));
		hp.decreaseLevelReal((int) Math.floor(20 + (0.10 * hp.getRealLevel())));
		prayer.increaseLevel((int) Math.floor(0.10 * prayer.getRealLevel()), prayer.getRealLevel());
		Skills.refresh(player, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.HITPOINTS, Skills.PRAYER);
	}
	
	/**
	 * The method that executes the prayer potion action.
	 * @param player the player to do this action for.
	 * @param superPrayer determines if this potion is a super prayer potion.
	 */
	private static void onPrayerEffect(Player player, boolean superPrayer) {
		Skill skill = player.getSkills()[Skills.PRAYER];
		int realLevel = skill.getRealLevel();
		
		skill.increaseLevel((int) Math.floor(7 + (realLevel * (superPrayer ? 0.35 : 0.25))), realLevel);
		
		Skills.refresh(player, Skills.PRAYER);
	}
	
	/**
	 * The method that executes the anti-poison potion action.
	 * @param player the player to do this action for.
	 * @param superPotion {@code true} if this potion is a super potion, {@code false}
	 * otherwise.
	 * @param length the length that the effect lingers for.
	 */
	public static void onAntiPoisonEffect(Player player, boolean superPotion, int length) {
		if(player.isPoisoned()) {
			player.getPoisonDamage().set(0);
			player.out(new SendConfig(174, 0));
			player.message("You have been cured of your poison!");
		}
		if(superPotion) {
			if(player.playerData.getPoisonImmunity().get() <= 0) {
				player.message("You have been granted immunity against poison.");
				player.playerData.getPoisonImmunity().incrementAndGet(length);
				World.get().submit(new Task(50, false) {
					@Override
					public void execute() {
						if(player.playerData.getPoisonImmunity().get() <= 0)
							this.cancel();
						if(player.playerData.getPoisonImmunity().decrementAndGet(50) <= 50)
							player.message("Your resistance to poison is about to wear off!");
						if(player.playerData.getPoisonImmunity().get() <= 0)
							this.cancel();
					}
					
					@Override
					public void onCancel() {
						player.message("Your resistance to poison has worn off!");
						player.playerData.getPoisonImmunity().set(0);
					}
				}.attach(player));
			} else if(player.playerData.getPoisonImmunity().get() > 0) {
				player.message("Your immunity against poison has been restored!");
				player.playerData.getPoisonImmunity().set(length);
			}
		}
	}
	
	/**
	 * The method that executes the energy potion action.
	 * @param player the player to do this action for.
	 * @param superPotion {@code true} if this potion is a super potion, {@code false}
	 * otherwise.
	 */
	private static void onEnergyEffect(Player player, boolean superPotion) {
		int amount = superPotion ? 100 : 50;
		player.setRunEnergy(player.playerData.getRunEnergy() + amount);
		player.out(new SendEnergy());
	}
	
	/**
	 * The method that executes the restore potion action.
	 * @param player the player to do this action for.
	 */
	private static void onRestoreEffect(Player player, boolean superRestore) {
		for(int index = 0; index <= 6; index++) {
			if((index == Skills.PRAYER) || (index == Skills.HITPOINTS)) {
				continue;
			}
			
			Skill skill = player.getSkills()[index];
			int realLevel = skill.getRealLevel();
			
			if(skill.getCurrentLevel() >= realLevel) {
				continue;
			}
			
			int formula = superRestore ? (int) Math.floor(8 + (realLevel * 0.25)) : (int) Math.floor(10 + (realLevel * 0.30));
			skill.increaseLevel(formula, realLevel);
			Skills.refresh(player, index);
		}
	}
	
	/**
	 * The method that executes the anti-fire potion action.
	 * @param player the player to do this action for.
	 * @param superVariant determines if this potion is the super variant.
	 */
	private static void onAntiFireEffect(Player player, boolean superVariant) {
		player.message("You take a sip of the" + (superVariant ? " super" : "") + " antifire potion.");
		CombatUtil.effect(player, superVariant ? CombatEffectType.SUPER_ANTIFIRE_POTION : CombatEffectType.ANTIFIRE_POTION);
	}
	
	/**
	 * The method that executes the basic effect potion action that will
	 * increment the level of {@code skill}.
	 * @param player the player to do this action for.
	 */
	private static void onBasicEffect(Player player, int skill, BoostType type) {
		Skill s = player.getSkills()[skill];
		int realLevel = s.getRealLevel();
		
		if(skill == Skills.HITPOINTS)
			realLevel *= 10;//constitution check.
		
		int boostLevel = (int) Math.floor(type.getBase() + (realLevel * type.getAmount()));
		int cap = realLevel + boostLevel;
		
		if((s.getCurrentLevel() + boostLevel) > (realLevel + boostLevel + 1)) {
			boostLevel = (realLevel + boostLevel) - s.getCurrentLevel();
		}
		s.increaseLevel(boostLevel, cap);
		Skills.refresh(player, skill);
	}
	
	/**
	 * Applies the overload effect on specified {@code player}.
	 * @param player the player to apply this effect for.
	 */
	public static void onOverloadEffect(Player player) {
		for(int i = 0; i <= 6; i++) {
			switch(i) {
				case Skills.ATTACK:
				case Skills.DEFENCE:
				case Skills.STRENGTH:
					PotionConsumable.onBasicEffect(player, i, BoostType.OVERLOAD);
					break;
				case Skills.MAGIC:
					PotionConsumable.onBasicEffect(player, i, BoostType.EXTREME_MAGIC);
					break;
				case Skills.RANGED:
					PotionConsumable.onBasicEffect(player, i, BoostType.MODERATE_EXTREME);
					break;
			}
		}
	}
	
	/**
	 * Retrieves the potion consumable element for {@code id}.
	 * @param id the id that the potion consumable is attached to.
	 * @return the potion consumable wrapped in an optional, or an empty
	 * optional if no potion consumable was found.
	 */
	public static Optional<PotionConsumable> forId(int id) {
		for(PotionConsumable potion : VALUES) {
			for(int potionId : potion.getIds()) {
				if(id == potionId) {
					return Optional.of(potion);
				}
			}
		}
		return Optional.empty();
	}
	
	/**
	 * The method executed when this potion type activated.
	 * @param player the player to execute this effect for.
	 */
	public abstract void onEffect(Player player);
	
	/**
	 * The method which determines if the {@code player} can drink the potion.
	 * @param player the player to determine this for.
	 */
	public boolean canDrink(Player player) {
		return true;
	}
	
	/**
	 * Gets the identifiers which represent this potion type.
	 * @return the identifiers for this potion.
	 */
	public final int[] getIds() {
		return ids;
	}
	
	/**
	 * Gets the item id for the specified dose.
	 * @param dose the dose to get the item id from.
	 * @return the item id.
	 */
	public int getIdForDose(int dose) {
		return ids[ids.length - dose];
	}
	
	/**
	 * The enumerated type whose elements represent the boost types for potions.
	 * @author Ryley Kimmel <ryley.kimmel@live.com>
	 * @author lare96 <http://github.com/lare96>
	 */
	public enum BoostType {
		NORMAL(3, .10F), MODERATE(4, 0.10F), //combat potion && Ranging Potion
		SUPER(5, .15F), MODERATE_EXTREME(3, .15F), //Ranging
		EXTREME_MAGIC(7, 0), EXTREME(5, .22F), OVERLOAD(5, .22F);
		
		/**
		 * The base which we will increment by.
		 */
		private final int base;
		
		/**
		 * The amount this type will boost by.
		 */
		private final float amount;
		
		/**
		 * Creates a new {@link BoostType}.
		 * @param boostAmount the amount this type will boost by.
		 */
		BoostType(int base, float boostAmount) {
			this.base = base;
			this.amount = boostAmount;
		}
		
		/**
		 * Gets the base this type will boost by.
		 * @return the base amount.
		 */
		public final int getBase() {
			return base;
		}
		
		/**
		 * Gets the amount this type will boost by.
		 * @return the boost amount.
		 */
		public final float getAmount() {
			return amount;
		}
	}
}