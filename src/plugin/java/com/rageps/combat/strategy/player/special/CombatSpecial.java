package com.rageps.combat.strategy.player.special;

import com.rageps.content.skill.Skills;
import com.rageps.net.packet.out.SendInterfaceLayer;
import com.rageps.combat.strategy.player.special.impl.*;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.net.packet.out.SendConfig;
import com.rageps.net.packet.out.SendMessage;
import com.rageps.net.packet.out.SendUpdateSpecial;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.Combat;
import com.rageps.combat.strategy.CombatStrategy;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Equipment;

import java.util.Arrays;
import java.util.Optional;

/**
 * The enumerated type whose elements represent the combat special attacks.
 * @author lare96 <http://github.com/lare96>
 *     todo - make these classes load from reflections
 */
public enum CombatSpecial {
	//int[] ids, int amount, double strength, double accuracy, CombatType combat, WeaponInterface weapon
	ABYSSAL_WHIP(new int[]{4151, 13444, 15441, 15442, 15443, 15444}, 50, new AbyssalWhip()),
	ANCHOR(new int[]{10887}, 50, new Anchor()),
	SARADOMIN_SWORD(new int[]{11730}, 65, new SaradominSword()),
	//		DRAGON_2H_SWORD(new int[]{7158}, 60, 1, 1, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
	//			@Override
	//			public CombatHit container(Player player, Actor target) {
	//				player.animation(new Animation(3157, Animation.AnimationPriority.HIGH));
	//				player.graphic(new Graphic(559));
	//				return new CombatHit(player, target, 1, CombatType.MELEE, false) {
	//					@Override
	//					public void postAttack(int counter) {
	//						if(player.inMulti()) {
	//							Set<? extends Actor> local = null;
	//							if(target.isPlayer()) {
	//								local = player.getLocalPlayers();
	//							} else if(target.isMob()) {
	//								local = player.getLocalMobs();
	//							}
	//							if(local == null)
	//								return;
	//							for(Actor character : local) {
	//								if(character == null)
	//									continue;
	//								if(character.getPosition().withinDistance(target.getPosition(), 1) && !character.same(target) && !character.same(player) && character.getCurrentHealth() > 0 && !character.isDead()) {
	//									Hit hit = CombatUtil.calculateRandomHit(player, target, CombatType.MELEE, 0, true);
	//									character.damage(hit);
	//									character.getCombat().getDamageCache().add(player, hit.getDamage());
	//								}
	//							}
	//						}
	//					}
	//				};
	//			}
	//		},
	DRAGON_BATTLEAXE(new int[]{1377}, 100, null) {
		@Override
		public void enable(Player player) {
			int newStrength = (int) (player.getSkills()[Skills.STRENGTH].getRealLevel() * 0.2);
			int newAttack = (int) (player.getSkills()[Skills.ATTACK].getRealLevel() * 0.1);
			int newDefence = (int) (player.getSkills()[Skills.DEFENCE].getRealLevel() * 0.1);
			int newRanged = (int) (player.getSkills()[Skills.RANGED].getRealLevel() * 0.1);
			int newMagic = (int) (player.getSkills()[Skills.MAGIC].getRealLevel() * 0.1);
			player.graphic(new Graphic(246, -110));
			player.animation(new Animation(1056));
			player.forceChat("Raarrrrrgggggghhhhhhh!");
			player.getSkills()[Skills.STRENGTH].increaseLevelReal(newStrength);
			player.getSkills()[Skills.ATTACK].decreaseLevelReal(newAttack);
			player.getSkills()[Skills.DEFENCE].decreaseLevelReal(newDefence);
			player.getSkills()[Skills.RANGED].decreaseLevelReal(newRanged);
			player.getSkills()[Skills.MAGIC].decreaseLevelReal(newMagic);
			Skills.refresh(player, Skills.STRENGTH);
			Skills.refresh(player, Skills.ATTACK);
			Skills.refresh(player, Skills.DEFENCE);
			Skills.refresh(player, Skills.RANGED);
			Skills.refresh(player, Skills.MAGIC);
			player.getCombat().cooldown(6);
			CombatSpecial.DRAGON_BATTLEAXE.drain(player);
		}
	},
	DRAGON_HALBERD(new int[]{3204, 11716}, 30, new DragonHalberd()),
	//	DRAGON_PICKAXE(new int[]{15259}, 100, 1, 1, CombatType.MELEE, WeaponInterface.PICKAXE) {
	//		@Override
	//		public CombatHit container(Player player, Actor target) {
	//			player.animation(new Animation(12031, Animation.AnimationPriority.HIGH));
	//			player.graphic(new Graphic(2144));
	//			return new CombatHit(player, target, 1, CombatType.MELEE, true) {
	//				@Override
	//				public void postAttack(int counter) {
	//					if(target.isPlayer() && counter != 0) {
	//						Player victim = target.toPlayer();
	//						int newAttack = (int) (victim.getSkills()[Skills.ATTACK].getRealLevel() * 0.5);
	//						int newRanged = (int) (victim.getSkills()[Skills.RANGED].getRealLevel() * 0.5);
	//						int newMagic = (int) (victim.getSkills()[Skills.MAGIC].getRealLevel() * 0.5);
	//						victim.getSkills()[Skills.ATTACK].decreaseLevelReal(newAttack);
	//						victim.getSkills()[Skills.RANGED].decreaseLevelReal(newRanged);
	//						victim.getSkills()[Skills.MAGIC].decreaseLevelReal(newMagic);
	//						Skills.refresh(player, Skills.ATTACK);
	//						Skills.refresh(player, Skills.RANGED);
	//						Skills.refresh(player, Skills.MAGIC);
	//					}
	//				}
	//			};
	//		}
	//	},
	//	DRAGON_SPEAR(new int[]{1249, 1263, 3176, 5716, 5730, 13772, 11716}, 25, 1, 1, CombatType.MELEE, WeaponInterface.SPEAR) {
	//		@Override
	//		public CombatHit container(Player player, Actor target) {
	//			player.animation(new Animation(1064, Animation.AnimationPriority.HIGH));
	//			player.graphic(new Graphic(253));
	//			return new CombatHit(player, target, 1, CombatType.MELEE, true) {
	//				@Override
	//				public void postAttack(int counter) {
	//					Position pos = TraversalMap.getRandomNearby(target.getPosition(), player.getPosition(), target.size());
	//					if(pos != null) {
	//						target.getMovementQueue().walk(pos);
	//						target.getMovementListener().append(() -> target.freeze(4));
	//					} else {
	//						target.stun(4);
	//					}
	//					target.graphic(new Graphic(80, 100));
	//				}
	//			};
	//		}
	//	},
	//	KORASI_SWORD(new int[]{19780}, 60, 1.2, 1.1, CombatType.MAGIC, WeaponInterface.LONGSWORD) {
	//		@Override
	//		public CombatHit container(Player player, Actor target) {
	//			player.animation(new Animation(14788, Animation.AnimationPriority.HIGH));
	//			player.graphic(new Graphic(target.isPlayer() ? 1729 : 1730));
	//			return new CombatHit(player, target, 1, CombatType.MELEE, true) {
	//				@Override
	//				public void postAttack(int counter) {
	//					if(player.inMulti()) {
	//						if(!player.inWilderness()) {
	//							World.get().submit(new KorasiChain(player, target, counter));
	//						}
	//					}
	//				}
	//			};
	//		}
	//	},
	VESTAS_LONGSWORD(new int[]{13899}, 25, new VestasLongsword()),
	STATIUS_WARHAMMER(new int[]{13902}, 30, new StatiusWarhammer()),
	//	DARK_BOW(new int[]{11235, 15701, 15702, 15703, 15704, 13405}, 55, 1.45, 1.22, CombatType.RANGED, WeaponInterface.LONGBOW) {
	//		@Override
	//		public CombatHit container(Player player, Actor target) {
	//			player.animation(new Animation(426));
	//			final int[] delay = {0};
	//			World.get().submit(new Task(1, false) {
	//				int tick = 0;
	//
	//				@Override
	//				public void execute() {
	//					if(tick == 0) {
	//						if (player.getEquipment().contains(11212)) {
	//							delay[0] = new Projectile(player, target, 1099, 44, 3, 43, 31, 0, CombatType.RANGED).sendProjectile().getTravelTime();
	//							new Projectile(player, target, 1099, 60, 3, 43, 31, 0, CombatType.RANGED).sendProjectile();
	//						} else {
	//							delay[0] = new Projectile(player, target, 1101, 44, 3, 43, 31, 0, CombatType.RANGED).sendProjectile().getTravelTime();
	//							new Projectile(player, target, 1101, 60, 3, 43, 31, 0, CombatType.RANGED).sendProjectile();
	//						}
	//					} else if(tick >= 1) {
	//						target.graphic(new Graphic(1100, 100));
	//						this.cancel();
	//					}
	//					tick++;
	//				}
	//			});
	//			return new CombatHit(player, target, 2, CombatType.RANGED, true, delay[0]) {
	//				@Override
	//				public void postAttack(int counter) {
	//					for(Hit h : getHits()) {
	//						if(h.getDamage() < 8) {
	//							h.setDamage(8);
	//						}
	//					}
	//				}
	//			};
	//		}
	//	},
	MAGIC_SHORTBOW(new int[]{861}, 50, new MagicShortbow()),
	//	MAGIC_LONGBOW(new int[]{859}, 35, 1, 5, CombatType.RANGED, WeaponInterface.LONGBOW) {
	//		@Override
	//		public CombatHit container(Player player, Actor target) {
	//			player.animation(new Animation(426, Animation.AnimationPriority.HIGH));
	//			player.graphic(new Graphic(250, 100));
	//			return new CombatHit(player, target, 1, CombatType.RANGED, true, new Projectile(player, target, 249, 44, 3, 43, 31, 0, CombatType.RANGED).sendProjectile().getTravelTime());
	//		}
	//	},
	//	MORRIGANS_JAVELIN(new int[]{13879}, 50, 1.40, 1.30, CombatType.RANGED, WeaponInterface.JAVELIN) {
	//		@Override
	//		public CombatHit container(Player player, Actor target) {
	//			player.animation(new Animation(10501, Animation.AnimationPriority.HIGH));
	//			player.graphic(new Graphic(1836));
	//			return new CombatHit(player, target, 1, CombatType.RANGED, true);
	//		}
	//	},
	//	MORRIGANS_THROWNAXE(new int[]{13883}, 50, 1.38, 1.30, CombatType.RANGED, WeaponInterface.THROWNAXE) {
	//		@Override
	//		public CombatHit container(Player player, Actor target) {
	//			player.animation(new Animation(10504, Animation.AnimationPriority.HIGH));
	//			player.graphic(new Graphic(1838));
	//			return new CombatHit(player, target, 1, CombatType.RANGED, true);
	//		}
	//	},
	//		DARKLIGHT(new int[]{6746}, 50, 1.15, 1, CombatType.MELEE, WeaponInterface.LONGSWORD) {
	//			@Override
	//			public CombatHit container(Player player, Actor target) {
	//				player.animation(new Animation(2890, Animation.AnimationPriority.HIGH));
	//				player.graphic(new Graphic(483, 100));
	//				return new CombatHit(player, target, 1, CombatType.MELEE, true) {
	//					@Override
	//					public void postAttack(int counter) {
	//						if(!target.isPlayer()) {
	//							return;
	//						}
	//						Skill[] skills = target.toPlayer().getSkills();
	//						List<Skill> weaken = Arrays.asList(skills[Skills.ATTACK], skills[Skills.STRENGTH], skills[Skills.DEFENCE]);
	//
	//						weaken.forEach(skill -> {
	//							int modifier = 1 + (skill.getLevel() / 100) * 5;
	//							skill.decreaseLevel(modifier);
	//						});
	//					}
	//				};
	//			}
	//		},
	DARK_BOW(new int[]{11235}, 55, new DarkBow()),
	ZAMORAK_GODSWORD(new int[]{11700}, 50, new ZamorakGodsword()),
	SARADOMIN_GODSWORD(new int[]{11698}, 60, new SaradominGodsword()),
	DRAGON_CLAWS(new int[]{14484, 14486}, 50, new DragonClaws()),
	DRAGON_DAGGER(new int[]{1215, 1231, 5680, 5698}, 25, new DragonDagger()),
	DRAGON_SCIMITAR(new int[]{4587}, 55, new DragonScimitar()),
	DRAGON_LONGSWORD(new int[]{1305}, 25, new DragonLongsword()),
	DRAGON_MACE(new int[]{1434}, 25, new DragonMace()),
	BANDOS_GODSWORD(new int[]{11696}, 50, new BandosGodsword()),
	ARMADYL_GODSWORD(new int[]{11694, 13450}, 50, new ArmadylGodsword()),
	GRANITE_MAUL(new int[]{4153}, 50, new GraniteMaul()) {
		@Override
		public void enable(Player player) {
			if(player.playerData.getSpecialPercentage().intValue() < player.getCombatSpecial().getAmount()) {
				player.message("You do not have enough special energy left!");
				return;
			}
			
			player.setSpecialActivated(true);
			player.out(new SendConfig(301, 1));
			
			Combat<Player> combat = player.getCombat();
			Actor defender = combat.getLastDefender();
			
			if(combat.isAttacking(defender)) {
				combat.submitStrategy(defender, GraniteMaul.get());
			}
		}
	};
	
	/**
	 * The identifiers for the weapons that perform this special.
	 */
	private final int[] ids;
	
	/**
	 * The amount of special energy drained by this attack.
	 */
	private final int amount;
	
	/**
	 * The strength bonus added when performing this special attack.
	 */
	private final CombatStrategy<Player> strategy;
	
	/**
	 * Creates a new {@link CombatSpecial}.
	 * @param ids the identifiers for the weapons that perform this special.
	 * @param amount the amount of special energy drained by this attack.
	 */
	CombatSpecial(int[] ids, int amount, CombatStrategy<Player> strategy) {
		this.ids = ids;
		this.amount = amount;
		this.strategy = strategy;
	}
	
	/**
	 * Drains the special bar for {@code player}.
	 * @param player the player who's special bar will be drained.
	 */
	public void drain(Player player) {
		player.playerData.getSpecialPercentage().decrementAndGet(amount, 0);
		updateSpecialAmount(player);
		disable(player);
	}
	
	/**
	 * Restores the special bar for {@code player}.
	 * @param player the player who's special bar will be restored.
	 * @param amount the amount of energy to restore to the special bar.
	 */
	public static void restore(Player player, int amount) {
		player.playerData.getSpecialPercentage().incrementAndGet(amount, 100);
		updateSpecialAmount(player);
	}
	
	/**
	 * Updates the special bar with the amount of special energy {@code player}
	 * has.
	 * @param player the player who's special bar will be updated.
	 */
	public static void updateSpecialAmount(Player player) {
		if(player.getWeapon().getSpecialBar() == -1 || player.getWeapon().getSpecialMeter() == -1) {
			return;
		}
		
		int specialCheck = 10;
		int specialBar = player.getWeapon().getSpecialMeter();
		int specialAmount = player.playerData.getSpecialPercentage().get() / 10;
		
		for(int i = 0; i < 10; i++) {
			player.out(new SendUpdateSpecial(--specialBar, specialAmount >= specialCheck ? 500 : 0));
			specialCheck--;
		}
	}
	
	/**
	 * Updates the weapon interface with a special bar if needed.
	 * @param player the player to update the interface for.
	 */
	public static void assign(Player player) {
		if(player.getWeapon().getSpecialBar() == -1) {
			if(player.getCombatSpecial() != null) {
				player.getCombatSpecial().disable(player);
			}
			player.setCombatSpecial(null);
			return;
		}
		
		Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
		if(item == null) {
			if(player.getCombatSpecial() != null) {
				player.getCombatSpecial().disable(player);
			}
			player.setCombatSpecial(null);
			return;
		}
		
		Optional<CombatSpecial> special = Arrays.stream(CombatSpecial.values()).filter(c -> Arrays.stream(c.getIds()).anyMatch(id -> item.getId() == id)).findFirst();
		if(special.isPresent()) {
			if(player.getCombatSpecial() != null) {
				player.getCombatSpecial().disable(player);
			}
			
			player.out(new SendInterfaceLayer(player.getWeapon().getSpecialBar(), false));
			player.setCombatSpecial(special.get());
			return;
		}
		
		player.out(new SendInterfaceLayer(player.getWeapon().getSpecialBar(), true));
		player.setCombatSpecial(null);
		
		if(player.getCombatSpecial() != null) {
			player.getCombatSpecial().disable(player);
		}
	}
	
	public void enable(Player player) {
		if(!player.isSpecialActivated()) {
			if(player.playerData.getSpecialPercentage().intValue() < player.getCombatSpecial().getAmount()) {
				player.message("You do not have enough special energy left!");
				return;
			}
			
			player.out(new SendConfig(301, 1));
			player.setSpecialActivated(true);
		}
	}
	
	public void disable(Player player) {
		if(player.isSpecialActivated()) {
			player.out(new SendConfig(301, 0));
			player.setSpecialActivated(false);
		}
	}
	
	/**
	 * Gets the identifiers for the weapons that perform this special.
	 * @return the identifiers for the weapons.
	 */
	public final int[] getIds() {
		return ids;
	}
	
	/**
	 * Gets the amount of special energy drained by this attack.
	 * @return the amount of special energy drained.
	 */
	public final int getAmount() {
		return amount;
	}
	
	public CombatStrategy<Player> getStrategy() {
		return strategy;
	}
	
}
