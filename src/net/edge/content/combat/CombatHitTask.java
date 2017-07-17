package net.edge.content.combat;

import net.edge.Server;
import net.edge.task.LinkedTaskSequence;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.content.skill.summoning.Summoning;
import net.edge.world.*;
import net.edge.world.node.EntityState;
import net.edge.world.node.EntityType;
import net.edge.world.node.actor.Actor;
import net.edge.world.node.actor.player.Player;

import java.util.Arrays;
import java.util.Objects;

/**
 * An attack on the builder's victim that is sent completely separate from the main combat session.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatHitTask extends Task {

	/**
	 * The builder this attack is executed for.
	 */
	private final CombatBuilder builder;

	/**
	 * The combat data from the combat session.
	 */
	private CombatHit data;

	/**
	 * The total amount of damage dealt during this attack.
	 */
	private int counter;

	/**
	 * Creates a new {@link CombatHitTask}.
	 * @param builder the builder this attack is executed for.
	 * @param hit    the combat hit from the combat session.
	 */
	public CombatHitTask(CombatBuilder builder, CombatHit hit) {
		super(hit.delay.orElse(Combat.getDelay(builder.getCharacter(), builder.getVictim(), hit.getType())), false);
		this.builder = builder;
		this.data = hit;
	}

	@Override
	public void execute() {

		Actor attacker = builder.getCharacter();
		Actor victim = builder.getVictim();

		if(attacker == null || victim == null || attacker.isDead() || attacker.getState() != EntityState.ACTIVE || victim.isDead() || victim.getState() != EntityState.ACTIVE) {
			this.cancel();
			return;
		}

		data = data.preAttack();

		if(victim.getCombatBuilder().getStrategy() != null && !data.isIgnored()) {
			victim.getCombatBuilder().getStrategy().incomingAttack(victim, attacker, data);
		} else if(!data.isIgnored()) {
			victim.getCombatBuilder().determineStrategy();
			victim.getCombatBuilder().getStrategy().incomingAttack(victim, attacker, data);
			victim.getCombatBuilder().resetStrategy();
		}

		if(data.getHits().length != 0 && !data.isIgnored()) {
			counter = data.attack();
			victim.getCombatBuilder().getDamageCache().add(attacker, counter);
		}

		if(victim.getType() == EntityType.PLAYER && !data.isIgnored()) {
			Player player = (Player) victim;
			Summoning.combatTeleport(player);
		}

		if(!data.isAccurate()) {
			if(data.getType() == CombatType.MAGIC) {
				if(!data.isIgnored()) {
					victim.graphic(new Graphic(85));
				}
				attacker.getCurrentlyCasting().executeOnHit(attacker, victim, false, 0);
				attacker.setCurrentlyCasting(null);
			}
		} else if(data.isAccurate() && !data.isIgnored()) {
			handleSpellEffects();
			handleArmorEffects();
			handlePrayerEffects();
			attacker.onSuccessfulHit(victim, data.getType());
			if(data.getType() == CombatType.MAGIC) {
				attacker.getCurrentlyCasting().endGraphic().ifPresent(victim::graphic);
				attacker.getCurrentlyCasting().executeOnHit(attacker, victim, true, counter);
				attacker.setCurrentlyCasting(null);
			}
		}

		if(victim.isPlayer() && !data.isIgnored()) {
			Player player = (Player) victim;

			boolean activated = false;

			switch(data.getType()) {
				case MELEE:
					if(Prayer.isActivated(player, Prayer.DEFLECT_MELEE)) {
						player.graphic(new Graphic(2230));
						activated = true;
					}
					break;
				case RANGED:
					if(Prayer.isActivated(player, Prayer.DEFLECT_MISSILES)) {
						player.graphic(new Graphic(2229));
						activated = true;
					}
					break;
				case MAGIC:
					if(Prayer.isActivated(player, Prayer.DEFLECT_MAGIC)) {
						player.graphic(new Graphic(2228));
						activated = true;
					}
					break;
			}

			int id = activated ? 12573 : player.getShieldAnimation() != null ? player.getShieldAnimation().getBlock() : player.getWeaponAnimation() != null ? player.getWeaponAnimation().getBlocking() : 404;
			player.animation(new Animation(id, Animation.AnimationPriority.LOW));

			if(activated) {
				World.get().submit(new Task(1, false) {
					@Override
					protected void execute() {
						Arrays.stream(data.getHits()).forEach(hit -> {
							int damage = (int) (hit.getDamage() * 0.10);
							victim.getCombatBuilder().getDamageCache().add(attacker, damage);
							attacker.damage(new Hit(damage, Hit.HitType.NORMAL, Hit.HitIcon.DEFLECT, victim.getSlot()));
						});
						this.cancel();
					}
				});
			}
		} else if(victim.isNpc() && !data.isIgnored()) {
			victim.animation(new Animation(victim.toNpc().getDefinition().getDefenceAnimation(), Animation.AnimationPriority.LOW));
		}

		data.postAttack(counter);

		if(victim.isAutoRetaliate() && !victim.getCombatBuilder().isAttacking() && !data.isIgnored()) {
			victim.getCombatBuilder().attack(attacker);
		}
		this.cancel();
	}

	/**
	 * Handles all spell effects that take place upon a successful attack.
	 */
	private void handleSpellEffects() {
		/* VENGEANCE */
		builder.getVictim().ifPlayer(player -> {
			if(!player.isVenged()) {
				return;
			}

			int amount = (int) (data.getHits()[0].getDamage() * 0.75D);

			builder.getCharacter().damage(new Hit(amount));
			builder.getCharacter().getCombatBuilder().getDamageCache().add(player, amount);
			player.forceChat("Taste Vengeance!");
			player.setVenged(false);
		});
	}

	/**
	 * Handles all armor effects that take place upon a successful attack.
	 */
	private void handleArmorEffects() {
		/* RECOIL **/
		builder.getVictim().ifPlayer(player -> {
			if(!player.getEquipment().contains(2550)) {
				return;
			}

			int hit = data.getHits()[0].getDamage();

			if(hit < 1) {
				return;
			}

			int damage = hit < 10 ? 10 : (int) (hit * 0.10);

			builder.getCharacter().damage(new Hit(damage));
			builder.getDamageCache().add(builder.getVictim(), damage);
		});
		/* PHOENIX NECKLACE */
		builder.getVictim().ifPlayer(player -> {
			if(!player.getEquipment().contains(11090)) {
				return;
			}

			if(World.getAreaManager().inArea(player, "DUEL_ARENA") && player.getMinigame().isPresent()) {
				return;
			}

			int cap = (int) (player.getMaximumHealth() * 0.20);

			if(player.getCurrentHealth() >= cap) {
				return;
			}

			if(player.isDead()) {
				return;
			}

			int restore = (int) (player.getMaximumHealth() * 0.30);

			player.getEquipment().unequip(player.getEquipment().computeIndexForId(11090));
			player.healEntity(restore);
			player.message("Your phoenix necklace restored your constitution, but was destroyed in the process.");
		});

		if(RandomUtils.inclusive(4) == 0) {
			if(Combat.isFullGuthans(builder.getCharacter())) {
				builder.getVictim().graphic(new Graphic(398));
				builder.getCharacter().healEntity(counter);
				return;
			}
			if(builder.getVictim().isPlayer()) {
				Player victim = (Player) builder.getVictim();

				if(Combat.isFullTorags(builder.getCharacter())) {
					victim.setRunEnergy(RandomUtils.inclusive(1, (int) victim.getRunEnergy()));
					victim.graphic(new Graphic(399));
				} else if(Combat.isFullAhrims(builder.getCharacter()) && victim.getSkills()[Skills.STRENGTH].getLevel() >= victim.getSkills()[Skills.STRENGTH].getRealLevel()) {
					victim.getSkills()[Skills.STRENGTH].decreaseLevel(RandomUtils.inclusive(1, 10));
					Skills.refresh(victim, Skills.STRENGTH);
					victim.graphic(new Graphic(400));
				} else if(Combat.isFullKarils(builder.getCharacter()) && victim.getSkills()[Skills.AGILITY].getLevel() >= victim.getSkills()[Skills.AGILITY].getRealLevel()) {
					victim.graphic(new Graphic(401));
					victim.getSkills()[Skills.AGILITY].decreaseLevel(RandomUtils.inclusive(1, 10));
					Skills.refresh(victim, Skills.AGILITY);
				}
			}
		}
	}

	/**
	 * Applies combat prayer accuracy and damage reductions before executing the
	 * {@link CombatHitTask}, this method shouldn't be confused with #handlePrayerEffects
	 * which handles the prayer effects after the damage is dealt.
	 */
	public static void applyPrayerEffects(CombatHit data) {
		if(data.getHits().length != 0) {
			Actor victim = data.getVictim();
			Actor attacker = data.getAttacker();

			//PROTECTION PRAYERS
			if(victim.isPlayer() && Prayer.isAnyActivated(victim.toPlayer(), Combat.getProtectingPrayer(data.getType()))) {
				switch(attacker.getType()) {
					case PLAYER:
						for(Hit h : data.getHits()) {
							int hit = h.getDamage();
							double mod = Math.abs(1 - CombatConstants.PRAYER_DAMAGE_REDUCTION);
							h.setDamage((int) (hit * mod));
							if(Server.DEBUG)
								victim.toPlayer().message("[DEBUG]: Damage " + "reduced by opponents prayer [" + (hit - h.getDamage()) + "]");
							mod = Math.round(RandomUtils.nextDouble() * 100.0) / 100.0;
							if(Server.DEBUG)
								victim.toPlayer().message("[DEBUG]: Chance " + "of opponents prayer cancelling hit [" + mod + "/" + CombatConstants.PRAYER_ACCURACY_REDUCTION + "]");
							if(mod <= CombatConstants.PRAYER_ACCURACY_REDUCTION) {
								h.setAccurate(false);
							}
						}
						break;
					case NPC:
						Arrays.stream(data.getHits()).filter(Objects::nonNull).forEach(h -> h.setAccurate(false));
						break;
					default:
						throw new IllegalStateException("Invalid character node " + "type!");
				}
			}
		}
	}
	/**
	 * Handles all prayer effects that take place upon a successful attack.
	 */
	private void handlePrayerEffects() {
		if(data.getHits().length != 0) {

			Actor victim = data.getVictim();
			Actor attacker = data.getAttacker();

			//REDEMPTION
			if(victim.isPlayer() && Prayer.isActivated(victim.toPlayer(), Prayer.REDEMPTION) && victim.toPlayer().getSkills()[Skills.HITPOINTS].getLevel() <= (victim.toPlayer().getSkills()[Skills.HITPOINTS].getRealLevel() / 10)) {
				int heal = (int) (victim.toPlayer().getSkills()[Skills.HITPOINTS].getRealLevel() * CombatConstants.REDEMPTION_PRAYER_HEAL);
				victim.toPlayer().getSkills()[Skills.HITPOINTS].increaseLevel(RandomUtils.inclusive(1, heal));
				victim.graphic(new Graphic(436));
				victim.toPlayer().getSkills()[Skills.PRAYER].setLevel(0, true);
				victim.toPlayer().message("You've run out of prayer points!");
				Prayer.deactivateAll(victim.toPlayer());
				Skills.refresh(victim.toPlayer(), Skills.PRAYER);
				Skills.refresh(victim.toPlayer(), Skills.HITPOINTS);
			}

			//SMITE
			if(builder.getCharacter().isPlayer() && Prayer.isActivated(builder.getCharacter().toPlayer(), Prayer.SMITE)) {
				victim.toPlayer().getSkills()[Skills.PRAYER].decreaseLevel(counter / 40, true);
				Skills.refresh(victim.toPlayer(), Skills.PRAYER);
			}

			//SOULSPLIT
			if(attacker.isPlayer() && Prayer.isActivated(attacker.toPlayer(), Prayer.SOUL_SPLIT) && data.isAccurate()) {
				new Projectile(attacker, victim, 2263, 44, 0, 11, 7, 0).sendProjectile();

				LinkedTaskSequence seq = new LinkedTaskSequence();
				seq.connect(1, () -> {
					if(attacker.isDead() || victim.isDead()) {
						seq.cancel();
						return;
					}

					victim.graphic(new Graphic(2264));

					if(victim.isPlayer()) {
						final int drain = counter / 50;
						victim.toPlayer().getSkills()[Skills.PRAYER].decreaseLevel(drain, true);
					}

					new Projectile(victim, attacker, 2263, 44, 0, 11, 7, 0).sendProjectile();
				});
				seq.connect(1, () -> {
					if(attacker.isDead() || victim.isDead()) {
						seq.cancel();
						return;
					}

					//heals 40% of damage when hit on player and 20% of damage when hit on npcs
					final int heal = (int) (attacker.isPlayer() ? (counter / 10) * 0.40 : (counter / 10) * 0.20);
					attacker.toPlayer().message("counter = " + counter + ", heal = " + heal);
					attacker.healEntity(heal);
				});
				seq.start();
			}
		}
	}
}
