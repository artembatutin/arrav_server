package net.arrav.content.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.arrav.GameConstants;
import net.arrav.action.impl.ItemAction;
import net.arrav.content.minigame.MinigameHandler;
import net.arrav.content.skill.Skill;
import net.arrav.content.skill.SkillData;
import net.arrav.content.skill.Skills;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.impl.Inventory;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The enumerated type managing consumable food types.
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author lare96 <http://github.com/lare96>
 */
public enum FoodConsumable {
	SHRIMP(30, 315),
	LOBSTER(120, 379),
	MANTA_RAY(220, 391),
	MONKFISH(160, 7946),
	MACKREL(60, 355),
	SALMON(90, 329),
	SEA_TURTLE(220, 397),
	SHARK(200, 385),
	FURY_SHARK(230, 20429),
	CAVEFISH(200, 15266),
	SWORDFISH(140, 373),
	TROUT(70, 333),
	TUNA(100, 361),
	TUNA_POTATO(220, 7060),
	ANCHOVIES(20, 319),
	CABBAGE(20, 1965),
	CRAYFISH(20, 13432),
	EQUA_LEAVES(20, 2128),
	ONION(20, 1957),
	BANANA(20, 1957),
	CHEESE(20, 1985),
	DWELLBERRIES(20, 2126),
	JANGERBERRIES(20, 247),
	LIME(20, 2120),
	LEMON(20, 2102),
	ORANGE(20, 2108),
	SPICY_TOMATO(20, 9994),
	SPINACH_ROLL(20, 1969),
	TOMATO(20, 1982),
	ROAST_BIRD_MEAT(20, 9980),
	COOKED_MEAT(20, 2142),
	UGTHANKI_MEAT(20, 1861),
	SARDINE(20, 325),
	BREAD(20, 2309),
	COOKED_RABBIT(20, 3228),
	FROGSPAWN_GUMBO(20, 10961),
	COOKED_CHICKEN(20, 2140),
	KARAMBWANJI(20, 3151),
	CHOCOLATE_BAR(20, 1973),
	SPICY_MINCED_MEAT(20, 9996),
	ROE(20, 11324),
	WHITE_TREE_FRUIT(20, 6469),
	HERRING(20, 347),
	BAKED_POTATO(20, 6701),
	RED_BANANA(20, 7572),
	SLICED_RED_BANANA(20, 7574),
	TCHIKI_MONKEY_NUTS(20, 7573),
	TCHIKI_NUT_PASTE(20, 7575),
	SPICY_SUACE(20, 7072),
	MINCED_MEAT(20, 7070),
	FROG_SPAWN(20, 5004),
	TOAD_CRUNCHIES(20, 9538),
	BAGUETTE(20, 6961),
	FILLETS(20, 10969),
	EEL_SUSHI(20, 10971),
	GRUBS_A_LA_MODE(20, 10966),
	MUSHROOMS(20, 10968),
	LOACH(20, 10970),
	ROST_FROG(20, 10967),
	GIANT_CARP(20, 337),
	GIANT_FROG_LEGS(20, 4517),
	COD(40, 339),
	PIKE(40, 351),
	BASS(90, 365),
	STRAWBERRY(30, 5323),
	COOKED_SWEETCORN(60, 5986),
	WATERMELON_SLICE(50, 5984),
	CAKE(40, 1891, 1893, 1895),
	CHOCOLATE_CAKE(41, 1897, 1899, 1901),
	PLAIN_PIZZA(35, 2289, 2291),
	MEAT_PIZZA(45, 2293, 2295),
	ANCHOVY_PIZZA(68, 2297, 2299),
	PINEAPPLE_PIZZA(81, 2301, 2303),
	PINEAPPLE_CHUNKS(20, 2116),
	PINEAPPLE_RING(20, 2118),
	ROCKTAIL(230, 15272) {
		@Override
		public int getHealAmount(Player player) {
			int hp = player.getSkills()[Skills.HITPOINTS].getRealLevel() * 10;
			if(hp < 120) {
				return 30;
			} else if(hp > 92) {
				return 230;
			} else {
				return (int) (hp * 2.5);
			}
		}
		
		@Override
		public int maximumCap(Player player) {
			return (int) (player.getMaximumHealth() * 1.10);
		}
	},
	REDBERRY_PIE(12, 2325, 2333) {
		@Override
		public boolean special() {
			return true;
		}
	},
	MEAT_PIE(25, 2327, 2331) {
		@Override
		public boolean special() {
			return true;
		}
	},
	APPLE_PIE(37, 2323, 2335) {
		@Override
		public boolean special() {
			return true;
		}
	},
	GARDEN_PIE(42, 7178, 7180) {
		@Override
		public void onEffect(Player player) {
			super.onEffect(player);
			
			Skill skill = player.getSkills()[Skills.FARMING];
			
			if(skill.getLevel() >= (skill.getRealLevel() + 3)) {
				return;
			}
			
			skill.increaseLevel(3);
			Skills.refresh(player, Skills.FARMING);
		}
		
		@Override
		public boolean special() {
			return true;
		}
	},
	FISH_PIE(58, 7188, 7190) {
		@Override
		public void onEffect(Player player) {
			super.onEffect(player);
			
			Skill skill = player.getSkills()[Skills.FISHING];
			
			if(skill.getLevel() >= (skill.getRealLevel() + 3)) {
				return;
			}
			
			skill.increaseLevel(3);
			Skills.refresh(player, Skills.FISHING);
		}
	},
	ADMIRAL_PIE(87, 7198, 7200) {
		@Override
		public void onEffect(Player player) {
			super.onEffect(player);
			
			Skill skill = player.getSkills()[Skills.FISHING];
			
			if(skill.getLevel() >= (skill.getRealLevel() + 5)) {
				return;
			}
			
			skill.increaseLevel(5);
			Skills.refresh(player, Skills.FISHING);
		}
		
		@Override
		public boolean special() {
			return true;
		}
	},
	WILD_PIE(10, 7208, 7210) {
		@Override
		public void onEffect(Player player) {
			super.onEffect(player);
			
			Skill[] skill = new Skill[]{player.getSkills()[Skills.RANGED], player.getSkills()[Skills.SLAYER]};
			
			if(skill[0].getLevel() >= (skill[0].getRealLevel() + 4)) {
				return;
			}
			
			skill[0].increaseLevel(4);
			Skills.refresh(player, Skills.RANGED);
			
			if(skill[1].getLevel() >= (skill[1].getRealLevel() + 5)) {
				return;
			}
			
			skill[1].increaseLevel(5);
			Skills.refresh(player, Skills.SLAYER);
		}
		
		@Override
		public boolean special() {
			return true;
		}
	},
	SUMMER_PIE(11, 7218, 7220) {
		@Override
		public void onEffect(Player player) {
			super.onEffect(player);
			player.setRunEnergy(player.getRunEnergy() * 0.20);
			Skill skill = player.getSkills()[Skills.AGILITY];
			if(skill.getLevel() >= (skill.getRealLevel() + 5)) {
				return;
			}
			skill.increaseLevel(5);
			Skills.refresh(player, Skills.AGILITY);
		}
		
		@Override
		public boolean special() {
			return true;
		}
	},
	KARAMBWAN(180, 3144) {
		@Override
		public boolean special() {
			return true;
		}
	},
	KEBAB(-1, 1971) {
		@Override
		public void onEffect(Player player) {
			Skill skill = player.getSkills()[Skills.HITPOINTS];
			int realLevel = player.getMaximumHealth();
			if(RandomUtils.floatRandom(100F) >= 61.24F) {
				int healAmount = Math.round((10 * 100F) / realLevel);
				skill.increaseLevel(healAmount, realLevel);
				player.message("It restores some life points" + ".");
				return;
			}
			if(RandomUtils.floatRandom(100F) >= 21.12F) {
				skill.increaseLevel(RandomUtils.inclusive(10, 20), realLevel);
				player.message("That was a good kebab. You " + "feel a lot better.");
				return;
			}
			if(RandomUtils.floatRandom(100F) >= 8.71F) {
				player.message("The kebab didn't seem to do " + "a lot.");
				return;
			}
			if(RandomUtils.floatRandom(100F) >= 3.65F) {
				skill.increaseLevel(30, realLevel);
				player.getSkills()[Skills.ATTACK].increaseLevel(RandomUtils.inclusive(3));
				player.getSkills()[Skills.STRENGTH].increaseLevel(RandomUtils.inclusive(3));
				player.getSkills()[Skills.DEFENCE].increaseLevel(RandomUtils.inclusive(3));
				player.message("Wow, that was an amazing kebab! You feel really invigorated.");
				return;
			}
			if(RandomUtils.floatRandom(100F) >= 3.28F) {
				player.getSkills()[Skills.ATTACK].decreaseLevel(RandomUtils.inclusive(3));
				player.getSkills()[Skills.STRENGTH].decreaseLevel(RandomUtils.inclusive(3));
				player.getSkills()[Skills.DEFENCE].decreaseLevel(RandomUtils.inclusive(3));
				player.message("That tasted a bit dodgy. You" + " feel a bit ill.");
				return;
			}
			if(RandomUtils.floatRandom(100F) >= 2.00F) {
				int id = RandomUtils.inclusiveExcludes(0, player.getSkills().length, Skills.HITPOINTS);
				Skill randomSkill = player.getSkills()[id];
				randomSkill.decreaseLevel(RandomUtils.inclusive(3));
				player.message("Eating the kebab has damaged" + " your " + SkillData.values()[id] + " stat.");
			}
		}
	};
	
	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<FoodConsumable> VALUES = Sets.immutableEnumSet(EnumSet.allOf(FoodConsumable.class));
	
	/**
	 * The amount of hit points this food heals.
	 */
	private final int healAmount;
	
	/**
	 * The identifiers which represent this food type.
	 */
	private final int[] ids;
	
	/**
	 * Creates a new {@link FoodConsumable}.
	 * @param healAmount the amount of hit points this food heals.
	 * @param ids        the identifiers which represent this food type.
	 */
	FoodConsumable(int healAmount, int... ids) {
		this.ids = ids;
		this.healAmount = healAmount;
	}
	
	@Override
	public final String toString() {
		return name().toLowerCase().replace("_", " ");
	}
	
	public static void action() {
		for(FoodConsumable food : FoodConsumable.values()) {
			ItemAction e = new ItemAction() {
				@Override
				public boolean click(Player player, Item item, int container, int slot, int click) {
					if(container != Inventory.INVENTORY_DISPLAY_ID) {
						return true;
					}
					if(player.isDead()) {
						return false;
					}
					if(!player.consumeDelay.get("SPECIAL").elapsed(GameConstants.CONSUME_DELAY)) {
						return false;
					}
					if(!player.consumeDelay.get(food.special() ? "SPECIAL" : "FOOD").elapsed(GameConstants.CONSUME_DELAY)) {
						return false;
					}
					if(!MinigameHandler.execute(player, m -> m.canEat(player, food))) {
						return false;
					}
					player.getInventory().remove(item, slot);
					player.animation(new Animation(829));
					player.consumeDelay.get(food.special() ? "SPECIAL" : "FOOD").reset();
					int rempSize = food.getIds().length;
					if(rempSize > 0) {
						Optional<Item> replacement = Optional.empty();
						for(int index = 0; index < rempSize; index++) {
							if(food.getIds()[index] == item.getId() && index + 1 < rempSize) {
								replacement = Optional.of(new Item(food.getIds()[index + 1]));
							}
						}
						replacement.ifPresent(item1 -> player.getInventory().set(slot, item1, true));
					}
					food.onEffect(player);
					player.message(food.getMessage());
					Skills.refresh(player, Skills.HITPOINTS);
					return true;
				}
			};
			for(int f : food.getIds())
				e.register(f);
		}
	}
	
	/**
	 * The method executed after the player has successfully consumed this food.
	 * This method may be overridden to provide a different functionality for
	 * such foods as kebabs.
	 * @param player the player that has consumed the food.
	 */
	public void onEffect(Player player) {
		player.getSkills()[Skills.HITPOINTS].increaseLevel(getHealAmount(player), maximumCap(player));
	}
	
	/**
	 * Retrieves the food consumable element for {@code id}.
	 * @param id the id that the food consumable is attached to.
	 * @return the food consumable wrapped in an optional, or an empty optional
	 * if no food consumable was found.
	 */
	public static Optional<FoodConsumable> forId(int id) {
		for(FoodConsumable food : VALUES) {
			for(int foodId : food.getIds()) {
				if(id == foodId) {
					return Optional.of(food);
				}
			}
		}
		return Optional.empty();
	}
	
	/**
	 * The max cap to heal hitpoints upon to.
	 * @param player player eating.
	 * @return hitpoints max cap.
	 */
	public int maximumCap(Player player) {
		return player.getMaximumHealth();
	}
	
	/**
	 * Retrieves the chatbox message printed when a food is consumed. This
	 * method may be overridden to provide a different functionality for foods
	 * which have a different chatbox message.
	 * @return the chatbox message printed when a food is consumed.
	 */
	public String getMessage() {
		return (ids.length > 1 ? "You eat a slice of the " : "You eat the ") + toString() + ".";
	}
	
	/**
	 * Gets the amount of hit points this food heals.
	 * @return the amount this food heals.
	 */
	public int getHealAmount(Player player) {
		return healAmount;
	}
	
	/**
	 * Gets the identifiers which represent this food type.
	 * @return the identifiers for this food.
	 */
	public final int[] getIds() {
		return ids;
	}
	
	/**
	 * Determines if this food is a special food type which can be used
	 * as combo food.
	 * @return {@code true} if this food is combo, {@code false} otherwise.
	 */
	public boolean special() {
		return false;
	}
}