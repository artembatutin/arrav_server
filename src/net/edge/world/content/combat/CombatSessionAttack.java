package net.edge.world.content.combat;

import net.edge.utils.rand.RandomUtils;
import net.edge.world.content.container.impl.Equipment;
import net.edge.world.content.minigame.MinigameHandler;
import net.edge.world.content.skill.Skills;
import net.edge.world.model.node.NodeState;
import net.edge.world.model.node.NodeType;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.model.Graphic;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.item.ItemNode;
import net.edge.world.model.node.region.Region;
import net.edge.world.World;
import net.edge.world.content.combat.ranged.CombatRangedAmmoDefinition;
import net.edge.world.content.combat.ranged.CombatRangedDetails.CombatRangedWeapon;
import net.edge.world.content.skill.prayer.Prayer;
import net.edge.world.content.skill.summoning.Summoning;
import net.edge.world.model.locale.Location;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.model.Hit;
import net.edge.task.Task;

/**
 * An attack on the builder's victim that is sent completely separate from the main combat session.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatSessionAttack extends Task {

	/**
	 * The builder this attack is executed for.
	 */
	private final CombatBuilder builder;

	/**
	 * The combat data from the combat session.
	 */
	private CombatSessionData data;

	/**
	 * The total amount of damage dealt during this attack.
	 */
	private int counter;

	/**
	 * Creates a new {@link CombatSessionAttack}.
	 * @param builder the builder this attack is executed for.
	 * @param data    the combat data from the combat session.
	 */
	public CombatSessionAttack(CombatBuilder builder, CombatSessionData data) {
		super(Combat.getDelay(builder.getCharacter(), data.getType()), data.getType() == CombatType.MELEE);
		this.builder = builder;
		this.data = data;
	}

	@Override
	public void execute() {

		EntityNode attacker = builder.getCharacter();
		EntityNode victim = builder.getVictim();

		if(attacker == null || victim == null || attacker.isDead() || attacker.getState() != NodeState.ACTIVE || victim.isDead() || victim.getState() != NodeState.ACTIVE) {
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
			counter = data.attack(data.getType() == CombatType.MAGIC && data.isAccurate());
			victim.getCombatBuilder().getDamageCache().add(attacker, counter);
		}

		if(victim.getType() == NodeType.PLAYER && !data.isIgnored()) {
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

		if(data.getType() == CombatType.RANGED && attacker.isPlayer() && !data.isIgnored()) {
			Player player = attacker.toPlayer();

			CombatRangedWeapon rangedWeapon = player.getRangedDetails().getWeapon().get();
			boolean droppable = !rangedWeapon.getType().isSpecialBow() && CombatRangedAmmoDefinition.NON_DROPPABLE.stream().noneMatch(rangedWeapon.getAmmunition().getDefinition()::equals);

			if(rangedWeapon.getAmmunition().getItem().getAmount() > 0 && droppable) {
				if(RandomUtils.inclusive(10) <= 1) {//attempting to drop ammo.
					int ava = player.getEquipment().getId(Equipment.CAPE_SLOT);
					int chance = ava == 10498 ? 25 : ava == 10499 ? 50 : 75;
					boolean collected = false;
					if(ava == 10498 || ava == 10499 || ava == 20068) {//gathering with accumulator.
						if(RandomUtils.inclusive(100) <= chance) {
							collected = true;
						}
					}
					if(!collected) {//dropping arrow if not gathered.
						Region region = victim.getRegion();
						if(region != null) {
							region.register(new ItemNode(new Item(rangedWeapon.getAmmunition().getItem().getId()), victim.getPosition(), player), true);
						}
					}
				}
			}
		}

		if(victim.isPlayer() && !data.isIgnored()) {
			Player player = (Player) victim;
			int id = player.getShieldAnimation() != null ? player.getShieldAnimation().getBlock() : player.getWeaponAnimation() != null ? player.getWeaponAnimation().getBlocking() : 404;
			player.animation(new Animation(id, Animation.AnimationPriority.LOW));
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
	 * Handles all prayer effects that take place upon a successful attack.
	 */
	private void handlePrayerEffects() {
		if(builder.getVictim().isPlayer() && data.getHits().length != 0) {
			Player victim = (Player) builder.getVictim();

			if(Prayer.isActivated(victim, Prayer.REDEMPTION) && victim.getSkills()[Skills.HITPOINTS].getLevel() <= (victim.getSkills()[Skills.HITPOINTS].getRealLevel() / 10)) {
				int heal = (int) (victim.getSkills()[Skills.HITPOINTS].getRealLevel() * CombatConstants.REDEMPTION_PRAYER_HEAL);
				victim.getSkills()[Skills.HITPOINTS].increaseLevel(RandomUtils.inclusive(1, heal));
				victim.graphic(new Graphic(436));
				victim.getSkills()[Skills.PRAYER].setLevel(0, true);
				victim.message("You've run out of prayer points!");
				Prayer.deactivateAll(victim);
				Skills.refresh(victim, Skills.PRAYER);
				Skills.refresh(victim, Skills.HITPOINTS);
				return;
			}
			if(builder.getCharacter().isPlayer()) {
				boolean inMinigame = MinigameHandler.getMinigame(victim).isPresent();
				if(Prayer.isActivated(victim, Prayer.RETRIBUTION) && victim.getSkills()[Skills.HITPOINTS].getLevel() < 1) {
					victim.graphic(new Graphic(437));

					if(Location.inWilderness(victim) || inMinigame && !Location.inMultiCombat(victim)) {
						if(builder.getCharacter().getPosition().withinDistance(victim.getPosition(), CombatConstants.RETRIBUTION_RADIUS)) {
							builder.getCharacter().damage(new Hit(RandomUtils.inclusive(CombatConstants.MAXIMUM_RETRIBUTION_DAMAGE)));
						}
					} else if(Location.inWilderness(victim) || inMinigame && Location.inMultiCombat(victim)) {
						for(Player player : victim.getLocalPlayers()) {
							if(player == null) {
								continue;
							}

							if(!player.equals(victim) && player.getPosition().withinDistance(victim.getPosition(), CombatConstants.RETRIBUTION_RADIUS)) {
								player.damage(new Hit(RandomUtils.inclusive(CombatConstants.MAXIMUM_RETRIBUTION_DAMAGE)));
							}
						}
					}
				}
				if(Prayer.isActivated(builder.getCharacter().toPlayer(), Prayer.SMITE)) {
					victim.getSkills()[Skills.PRAYER].decreaseLevel(counter / 4);
					Skills.refresh(victim, Skills.PRAYER);
				}
			}
		}
	}
}
