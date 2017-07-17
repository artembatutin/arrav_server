package net.edge.content.combat.special;

import net.edge.content.combat.CombatHit;
import net.edge.net.packet.out.SendConfig;
import net.edge.net.packet.out.SendInterfaceLayer;
import net.edge.net.packet.out.SendUpdateSpecial;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.Combat;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.item.container.impl.Equipment;
import net.edge.content.skill.Skill;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.world.locale.loc.Location;
import net.edge.world.locale.Position;
import net.edge.world.*;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The enumerated type whose elements represent the combat special attacks.
 * @author lare96 <http://github.com/lare96>
 */
public enum CombatSpecial {
	//int[] ids, int amount, double strength, double accuracy, CombatType combat, WeaponInterface weapon
	ABYSSAL_WHIP(new int[]{4151, 13444, 15441, 15442, 15443, 15444}, 50, 1, 1, CombatType.MELEE, WeaponInterface.WHIP) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(1658, Animation.AnimationPriority.HIGH));
			target.graphic(new Graphic(341, 140));
			if(target.isPlayer()) {
				//Gathering the oponent's running energy.
				final Player victimPlayer = target.toPlayer();
				double energy = 0;
				if(victimPlayer.getRunEnergy() != 0)
					energy = victimPlayer.getRunEnergy() / 4;
				
				//Decreasing oponent's energy and increasing the attacker's energy.
				victimPlayer.setRunEnergy(victimPlayer.getRunEnergy() - energy);
				player.setRunEnergy(player.getRunEnergy() + energy);
			}
			return new CombatHit(player, target, 1, CombatType.MELEE, false);
		}
	},
	ANCHOR(new int[]{10887}, 50, 1.1, 1.30, CombatType.MELEE, WeaponInterface.WARHAMMER) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(5870, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(1027, 50));
			return new CombatHit(player, target, 1, CombatType.MELEE, true, 3) {
				@Override
				public void postAttack(int counter) {
					if(target.isPlayer() && counter != 0) {
						//Finalizing with the anchor effect. (decreasing random combat skill).
						int skill = RandomUtils.random(Skills.DEFENCE, Skills.ATTACK, Skills.MAGIC, Skills.RANGED);
						double decreaseValue = (counter / 100.0) * 10.0;
						Player victim = target.toPlayer();
						victim.getSkills()[skill].decreaseLevel((int) decreaseValue);
						Skills.refresh(victim, skill);
					}
				}
			};
		}
	},
	ARMADYL_GODSWORD(new int[]{11694, 13450}, 50, 1.375, 2, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(11989, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(2113));
			return new CombatHit(player, target, 1, CombatType.MELEE, true);
		}
	},
	BANDOS_GODSWORD(new int[]{11696}, 50, 1.21, 1.5, CombatType.MELEE, WeaponInterface.WARHAMMER) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(11991, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(2114));
			return new CombatHit(player, target, 1, CombatType.MELEE, true) {
				@Override
				public void postAttack(int counter) {
					if(target.isPlayer() && counter != 0) {
						Player victim = target.toPlayer();
						int[] skillOrder = {Skills.DEFENCE, Skills.STRENGTH, Skills.ATTACK, Skills.PRAYER, Skills.MAGIC, Skills.RANGED};
						for(int s : skillOrder) {
							//Getting of the loop if the damage is negative of null;
							if(counter <= 0)
								break;
							//Getting the skill value to decrease.
							int removeFromSkill;
							if(counter > victim.getSkills()[s].getLevel()) {
								int difference = counter - victim.getSkills()[s].getLevel();
								removeFromSkill = counter - difference;
							} else
								removeFromSkill = counter;
							//Decreasing the skill.
							victim.getSkills()[s].decreaseLevel(removeFromSkill);
							Skills.refresh(victim, s);
							//Changing the damage left to decrease.
							counter -= removeFromSkill;
							SkillData data = SkillData.forId(s);
							String skill = data.toString();
							player.message("You've drained " + victim.getCredentials().getUsername() + "'s " + skill + " level by " + removeFromSkill + ".");
							victim.message("Your " + skill + " level has been drained.");
						}
					}
				}
			};
		}
	},
	ZAMORAK_GODSWORD(new int[]{11700}, 50, 1.1, 1.4, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(7070, Animation.AnimationPriority.HIGH));
			return new CombatHit(player, target, 1, CombatType.MELEE, true) {
				@Override
				public void postAttack(int counter) {
					if(counter > 0) {
						player.graphic(new Graphic(1221));
						target.graphic(new Graphic((2104)));
						if(!target.isFrozen()) {
							if(target.size() == 1) {
								target.freeze(15);
							}
						}
					} else {
						target.graphic(new Graphic(339, 10));
					}
				}
			};
		}
	},
	SARADOMIN_GODSWORD(new int[]{11698}, 60, 1.1, 1.8, CombatType.MELEE, WeaponInterface.WARHAMMER) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(7071, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(1220));
			return new CombatHit(player, target, 1, CombatType.MELEE, true) {
				@Override
				public void postAttack(int counter) {
					//Healing blade effect
					if(counter != 0) {
						player.getSkills()[Skills.PRAYER].increaseLevel(player.getSkills()[Skills.PRAYER].getRealLevel() / 4, 990);
						player.getSkills()[Skills.HITPOINTS].increaseLevel(player.getSkills()[Skills.HITPOINTS].getRealLevel() / 2, 990);
					}
				}
			};
		}
	},
	SARADOMIN_SWORD(new int[]{11730}, 65, 1.1, 1, CombatType.MELEE, WeaponInterface.WARHAMMER) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(11993, Animation.AnimationPriority.HIGH));
			
			return new CombatHit(player, target, 2, CombatType.MELEE, true) {
				@Override
				public void postAttack(int counter) {
					getHits()[1].setIcon(Hit.HitIcon.MAGIC);
					target.graphic(new Graphic(1194));
					if(counter != 0) {
						player.getSkills()[Skills.PRAYER].increaseLevel(player.getSkills()[Skills.PRAYER].getRealLevel() / 4, 990);
						player.getSkills()[Skills.HITPOINTS].increaseLevel(player.getSkills()[Skills.HITPOINTS].getRealLevel() / 2, 990);
					}
				}
			};
		}
	},
	DRAGON_2H_SWORD(new int[]{7158}, 60, 1, 1, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(3157, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(559));
			return new CombatHit(player, target, 1, CombatType.MELEE, false) {
				@Override
				public void postAttack(int counter) {
					if(player.inMulti()) {
						Set<? extends Actor> local = null;
						if(target.isPlayer()) {
							local = player.getLocalPlayers();
						} else if(target.isNpc()) {
							local = player.getLocalMobs();
						}
						if(local == null)
							return;
						for(Actor character : local) {
							if(character == null)
								continue;
							if(character.getPosition().withinDistance(target.getPosition(), 1) && !character.same(target) && !character.same(player) && character.getCurrentHealth() > 0 && !character.isDead()) {
								Hit hit = Combat.calculateRandomHit(player, target, CombatType.MELEE, 0, true);
								character.damage(hit);
								character.getCombatBuilder().getDamageCache().add(player, hit.getDamage());
							}
						}
					}
				}
			};
		}
	},
	DRAGON_BATTLEAXE(new int[]{1377}, 100, 1, 1, CombatType.MELEE, WeaponInterface.BATTLEAXE) {
		@Override
		public void onActivation(Player player, Actor target) {
			int newStrength = (int) (player.getSkills()[Skills.STRENGTH].getRealLevel() * 0.2);
			int newAttack = (int) (player.getSkills()[Skills.ATTACK].getRealLevel() * 0.1);
			int newDefence = (int) (player.getSkills()[Skills.DEFENCE].getRealLevel() * 0.1);
			int newRanged = (int) (player.getSkills()[Skills.RANGED].getRealLevel() * 0.1);
			int newMagic = (int) (player.getSkills()[Skills.MAGIC].getRealLevel() * 0.1);
			player.graphic(new Graphic(246, -100));
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
			player.getCombatBuilder().cooldown(true);
			CombatSpecial.drain(player, DRAGON_BATTLEAXE.amount);
		}
		
		@Override
		public CombatHit container(Player player, Actor target) {
			throw new UnsupportedOperationException("Dragon battleaxe does not have a special attack!");
		}
	},
	DRAGON_CLAWS(new int[]{14484, 14486}, 50, 1.05, 6, CombatType.MELEE, WeaponInterface.CLAWS) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(10961, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(1950, 50));
			return new CombatHit(player, target, 4, CombatType.MELEE, true);
		}
	},
	DRAGON_DAGGER(new int[]{1215, 1231, 5680, 5698}, 25, 1.15, 1.25, CombatType.MELEE, WeaponInterface.DAGGER) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(1062, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(252, 100));
			return new CombatHit(player, target, 2, CombatType.MELEE, true, 2);
		}
	},
	DRAGON_HALBERD(new int[]{3204, 11716}, 30, 1, 1, CombatType.MELEE, WeaponInterface.HALBERD) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(1203, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(282, 100));
			return new CombatHit(player, target, 2, CombatType.MELEE, true);
		}
	},
	DRAGON_LONGSWORD(new int[]{1305}, 25, 1.15, 1, CombatType.MELEE, WeaponInterface.LONGSWORD) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(1058, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(248, 100));
			return new CombatHit(player, target, 1, CombatType.MELEE, true);
		}
	},
	DRAGON_MACE(new int[]{1434}, 25, 1.50, 1.25, CombatType.MELEE, WeaponInterface.MACE) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(1060, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(251, 100));
			return new CombatHit(player, target, 1, CombatType.MELEE, true);
		}
	},
	DRAGON_PICKAXE(new int[]{15259}, 100, 1, 1, CombatType.MELEE, WeaponInterface.PICKAXE) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(12031, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(2144));
			return new CombatHit(player, target, 1, CombatType.MELEE, true) {
				@Override
				public void postAttack(int counter) {
					if(target.isPlayer() && counter != 0) {
						Player victim = target.toPlayer();
						int newAttack = (int) (victim.getSkills()[Skills.ATTACK].getRealLevel() * 0.5);
						int newRanged = (int) (victim.getSkills()[Skills.RANGED].getRealLevel() * 0.5);
						int newMagic = (int) (victim.getSkills()[Skills.MAGIC].getRealLevel() * 0.5);
						victim.getSkills()[Skills.ATTACK].decreaseLevelReal(newAttack);
						victim.getSkills()[Skills.RANGED].decreaseLevelReal(newRanged);
						victim.getSkills()[Skills.MAGIC].decreaseLevelReal(newMagic);
						Skills.refresh(player, Skills.ATTACK);
						Skills.refresh(player, Skills.RANGED);
						Skills.refresh(player, Skills.MAGIC);
					}
				}
			};
		}
	},
	DRAGON_SCIMITAR(new int[]{4587}, 55, 1, 1, CombatType.MELEE, WeaponInterface.SCIMITAR) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(12031, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(2118, 100));
			return new CombatHit(player, target, 1, CombatType.MELEE, true) {
				@Override
				public void postAttack(int counter) {
					if(target.isPlayer() && counter != 0) {
						Player victim = target.toPlayer();
						Prayer.PROTECT_FROM_MAGIC.deactivate(victim);
						Prayer.PROTECT_FROM_MELEE.deactivate(victim);
						Prayer.PROTECT_FROM_MISSILES.deactivate(victim);
						player.message("You have been injured");
					}
				}
			};
		}
	},
	DRAGON_SPEAR(new int[]{1249, 1263, 3176, 5716, 5730, 13772, 11716}, 25, 1, 1, CombatType.MELEE, WeaponInterface.SPEAR) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(1064, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(253));
			return new CombatHit(player, target, 1, CombatType.MELEE, true) {
				@Override
				public void postAttack(int counter) {
					Position pos = World.getTraversalMap().getRandomNearby(target.getPosition(), player.getPosition(), target.size());
					if(pos != null) {
						target.getMovementQueue().walk(pos);
						target.getMovementListener().append(() -> target.freeze(4));
					} else {
						target.stun(4);
					}
					target.graphic(new Graphic(80, 100));
				}
			};
		}
	},
	GRANITE_MAUL(new int[]{4153}, 50, 1, 1, CombatType.MELEE, WeaponInterface.WARHAMMER) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(1667, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(340, 40));
			return new CombatHit(player, target, 1, CombatType.MELEE, true);
		}
	},
	KORASI_SWORD(new int[]{19780}, 60, 1.2, 1.1, CombatType.MAGIC, WeaponInterface.LONGSWORD) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(14788, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(target.isPlayer() ? 1729 : 1730));
			return new CombatHit(player, target, 1, CombatType.MELEE, true) {
				@Override
				public void postAttack(int counter) {
					if(player.inMulti()) {
						if(!player.inWilderness()) {
							World.get().submit(new KorasiChain(player, target, counter));
						}
					}
				}
			};
		}
	},
	VESTAS_LONGSWORD(new int[]{13899}, 25, 1.28, 1.25, CombatType.MELEE, WeaponInterface.LONGSWORD) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(10502, Animation.AnimationPriority.HIGH));
			return new CombatHit(player, target, 1, CombatType.MELEE, true);
		}
	},
	STATIUS_WARHAMMER(new int[]{13902}, 30, 1.25, 1.23, CombatType.MELEE, WeaponInterface.WARHAMMER) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(10505, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(1840));
			return new CombatHit(player, target, 1, CombatType.MELEE, true) {
				@Override
				public void postAttack(int counter) {
					if(target.isPlayer() && counter > 0) {
						Player victim = target.toPlayer();
						int currentDef = victim.getSkills()[Skills.DEFENCE].getRealLevel();
						int defDecrease = (int) (currentDef * 0.11);
						if((currentDef - defDecrease) <= 0 || currentDef <= 0)
							return;
						victim.getSkills()[Skills.DEFENCE].decreaseLevelReal(defDecrease);
						victim.message("Your opponent has reduced your Defence level.");
						player.message("Your hammer forces some of your opponent's defences to break.");
					}
				}
			};
		}
	},
	DARK_BOW(new int[]{11235, 15701, 15702, 15703, 15704, 13405}, 55, 1.45, 1.22, CombatType.RANGED, WeaponInterface.LONGBOW) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(426));
			final int[] delay = {0};
			World.get().submit(new Task(1, false) {
				int tick = 0;
				
				@Override
				public void execute() {
					if(tick == 0) {
						delay[0] = new Projectile(player, target, 1099, 44, 3, 43, 31, 0, CombatType.RANGED).sendProjectile().getTravelTime();
						new Projectile(player, target, 1099, 60, 3, 43, 31, 0, CombatType.RANGED).sendProjectile();
					} else if(tick >= 1) {
						target.graphic(new Graphic(1100, 100));
						this.cancel();
					}
					tick++;
				}
			});
			return new CombatHit(player, target, 2, CombatType.RANGED, true, delay[0]) {
				@Override
				public void postAttack(int counter) {
					for(Hit h : getHits()) {
						if(h.getDamage() < 8) {
							h.setDamage(8);
						}
					}
				}
			};
		}
	},
	MAGIC_SHORTBOW(new int[]{861}, 50, 1, 1.1, CombatType.RANGED, WeaponInterface.SHORTBOW) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(426, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(250, 100));
			int delay = new Projectile(player, target, 249, 58, 40, 43, 31, 0, CombatType.RANGED).sendProjectile().getTravelTime();
			World.get().submit(new Task(1, false) {
				@Override
				public void execute() {
					player.animation(new Animation(426, Animation.AnimationPriority.HIGH));
					//player.graphic(new Graphic(250, 100));
					new Projectile(player, target, 249, 48, 30, 43, 31, 0, CombatType.RANGED).sendProjectile();
					this.cancel();
				}
			});
			return new CombatHit(player, target, 2, CombatType.RANGED, true, delay);
		}
	},
	MAGIC_LONGBOW(new int[]{859}, 35, 1, 5, CombatType.RANGED, WeaponInterface.LONGBOW) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(426, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(250, 100));
			return new CombatHit(player, target, 1, CombatType.RANGED, true, new Projectile(player, target, 249, 44, 3, 43, 31, 0, CombatType.RANGED).sendProjectile().getTravelTime());
		}
	},
	MORRIGANS_JAVELIN(new int[]{13879}, 50, 1.40, 1.30, CombatType.RANGED, WeaponInterface.JAVELIN) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(10501, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(1836));
			return new CombatHit(player, target, 1, CombatType.RANGED, true);
		}
	},
	MORRIGANS_THROWNAXE(new int[]{13883}, 50, 1.38, 1.30, CombatType.RANGED, WeaponInterface.THROWNAXE) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(10504, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(1838));
			return new CombatHit(player, target, 1, CombatType.RANGED, true);
		}
	},
	DARKLIGHT(new int[]{6746}, 50, 1.15, 1, CombatType.MELEE, WeaponInterface.LONGSWORD) {
		@Override
		public CombatHit container(Player player, Actor target) {
			player.animation(new Animation(2890, Animation.AnimationPriority.HIGH));
			player.graphic(new Graphic(483, 100));
			return new CombatHit(player, target, 1, CombatType.MELEE, true) {
				@Override
				public void postAttack(int counter) {
					if(!target.isPlayer()) {
						return;
					}
					Skill[] skills = target.toPlayer().getSkills();
					List<Skill> weaken = Arrays.asList(skills[Skills.ATTACK], skills[Skills.STRENGTH], skills[Skills.DEFENCE]);
					
					weaken.forEach(skill -> {
						int modifier = 1 + (skill.getLevel() / 100) * 5;
						skill.decreaseLevel(modifier);
					});
				}
			};
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
	private final double strength;
	
	/**
	 * The accuracy bonus added when performing this special attack.
	 */
	private final double accuracy;
	
	/**
	 * The combat type used when performing this special attack.
	 */
	private final CombatType combat;
	
	/**
	 * The weapon type used when performing this special attack.
	 */
	private final WeaponInterface weapon;
	
	/**
	 * Creates a new {@link CombatSpecial}.
	 * @param ids      the identifiers for the weapons that perform this special.
	 * @param amount   the amount of special energy drained by this attack.
	 * @param strength the strength bonus added when performing this special attack.
	 * @param accuracy the accuracy bonus added when performing this special attack.
	 * @param combat   the combat type used when performing this special attack.
	 * @param weapon   the weapon type used when performing this special attack.
	 */
	CombatSpecial(int[] ids, int amount, double strength, double accuracy, CombatType combat, WeaponInterface weapon) {
		this.ids = ids;
		this.amount = amount;
		this.strength = strength;
		this.accuracy = accuracy;
		this.combat = combat;
		this.weapon = weapon;
	}
	
	/**
	 * Executes exactly when {@code player} activates the special bar.
	 * @param player the player who activated the special bar.
	 * @param target the target when activating the special attack bar, will be
	 *               {@code null} if the player is not in combat.
	 */
	public void onActivation(Player player, Actor target) {
		
	}
	
	/**
	 * The combat data that will be used to make an attack on {@code target}.
	 * @param player the player who is making an attack.
	 * @param target the main target of the attack.
	 * @return the combat data.
	 */
	public abstract CombatHit container(Player player, Actor target);
	
	/**
	 * Drains the special bar for {@code player}.
	 * @param player the player who's special bar will be drained.
	 * @param amount the amount of energy to drain from the special bar.
	 */
	public static void drain(Player player, int amount) {
		player.getSpecialPercentage().decrementAndGet(amount, 0);
		CombatSpecial.updateSpecialAmount(player);
		player.out(new SendConfig(301, 0));
		player.setSpecialActivated(false);
	}
	
	/**
	 * Restores the special bar for {@code player}.
	 * @param player the player who's special bar will be restored.
	 * @param amount the amount of energy to restore to the special bar.
	 */
	public static void restore(Player player, int amount) {
		player.getSpecialPercentage().incrementAndGet(amount, 100);
		CombatSpecial.updateSpecialAmount(player);
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
		int specialAmount = player.getSpecialPercentage().get() / 10;
		
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
			player.setCombatSpecial(null);
			return;
		}
		Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
		if(item == null) {
			return;
		}
		Optional<CombatSpecial> special = Arrays.stream(CombatSpecial.values()).filter(c -> Arrays.stream(c.getIds()).anyMatch(id -> item.getId() == id)).findFirst();
		if(special.isPresent()) {
			player.out(new SendInterfaceLayer(player.getWeapon().getSpecialBar(), false));
			player.setCombatSpecial(special.get());
			return;
		}
		player.out(new SendInterfaceLayer(player.getWeapon().getSpecialBar(), true));
		player.setCombatSpecial(null);
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
	
	/**
	 * Gets the strength bonus added when performing this special attack.
	 * @return the strength bonus.
	 */
	public final double getStrength() {
		return strength;
	}
	
	/**
	 * Gets the accuracy bonus added when performing this special attack.
	 * @return the accuracy bonus.
	 */
	public final double getAccuracy() {
		return accuracy;
	}
	
	/**
	 * Gets the combat type used when performing this special attack.
	 * @return the combat type.
	 */
	public final CombatType getCombat() {
		return combat;
	}
	
	/**
	 * Gets the weapon type used when performing this special attack.
	 * @return the weapon type.
	 */
	public final WeaponInterface getWeapon() {
		return weapon;
	}
}
