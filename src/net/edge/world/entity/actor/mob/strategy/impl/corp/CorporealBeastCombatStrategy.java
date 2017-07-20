package net.edge.world.entity.actor.mob.strategy.impl.corp;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.combat.CombatHit;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.content.skill.SkillData;
import net.edge.world.entity.region.TraversalMap;
import net.edge.world.locale.Boundary;
import net.edge.world.locale.Position;
import net.edge.world.*;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.impl.corp.CorporealBeast;
import net.edge.world.entity.actor.mob.strategy.DynamicCombatStrategy;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.locale.area.AreaManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The dynamic combat strategy for the {@link CorporealBeast} npc.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CorporealBeastCombatStrategy extends DynamicCombatStrategy<CorporealBeast> {

	/**
	 * Constructs a new {@link CorporealBeastCombatStrategy}.
	 * @param npc the npc which is an instance of {@link CorporealBeast}.
	 */
	public CorporealBeastCombatStrategy(CorporealBeast npc) {
		super(npc);
	}

	@Override
	public boolean canOutgoingAttack(Actor victim) {
		return victim.isPlayer() && AreaManager.get().inArea(victim.getPosition(), "CORPOREAL_BEAST");
	}

	@Override
	public CombatHit outgoingAttack(Actor victim) {
		if(!npc.getTask().isPresent()) {
			npc.setTask();
		}

		npc.devourFamiliar(victim);
		spawnDarkEnergyCore(victim);

		CombatType[] data = npc.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE} : new CombatType[]{CombatType.MAGIC};
		CombatType c = RandomUtils.random(data);

		List<Player> players = npc.getRegion().getPlayers().stream().filter(p -> AreaManager.get().inArea(p.getPosition(), "CORPOREAL_BEAST")).collect(Collectors.toList());
		return type(victim, c, players);
	}

	@Override
	public void incomingAttack(Actor attacker, CombatHit data) {
		if(!attacker.isPlayer()) {
			return;
		}

		Player player = attacker.toPlayer();

		if(!player.getWeapon().equals(WeaponInterface.SPEAR)) {
			Arrays.stream(data.getHits()).filter(Objects::nonNull).forEach(hit -> hit.setDamage(hit.getDamage() / 2));
		}
	}

	private void spawnDarkEnergyCore(Actor victim) {
		if(npc.getDarkEnergyCore().getState().equals(EntityState.ACTIVE) || (npc.getCurrentHealth()) >= (npc.getMaxHealth() / 2) || npc.getDarkEnergyCore().isDead()) {
			return;
		}

		if(RandomUtils.inclusive(100) < 10) {
			return;
		}

		Position pos = victim.getPosition();

		new Projectile(npc.getCenterPosition(), pos, 0, 1828, 44, 4, 60, 43, 0, npc.getInstance(), CombatType.RANGED).sendProjectile();
		World.get().submit(new Task(2) {

			@Override
			protected void execute() {
				this.cancel();
				npc.getDarkEnergyCore().setPosition(pos);
				World.get().getNpcs().add(npc.getDarkEnergyCore());
			}

		});
	}

	private CombatHit melee(Actor victim, List<Player> players) {
		List<Player> stompables = players.stream().filter(p -> new Boundary(p.getPosition(), p.size()).inside(npc.getPosition(), npc.size() + 1)).collect(Collectors.toList());

		if(!stompables.isEmpty()) {

			return new CombatHit(npc, stompables.get(0), 1, CombatType.MELEE, true) {

				@Override
				public CombatHit preAttack() {
					Arrays.fill(this.getHits(), null);

					this.getHits()[0] = new Hit(RandomUtils.inclusive(200, 450), Hit.HitType.NORMAL, Hit.HitIcon.MELEE, victim.getSlot());
					this.setAccurate(true);
					npc.animation(new Animation(10496));
					npc.graphic(new Graphic(1834));
					return this;
				}

				@Override
				public void postAttack(int counter) {
					stompables.stream().filter(s -> !s.getFormatUsername().equals(stompables.get(0).getFormatUsername())).forEach(player -> {
						Arrays.stream(getHits()).forEach(hit -> player.getCombatBuilder().getDamageCache().add(npc, hit.getDamage()));
						player.damage(getHits());
					});
				}
			};
		}

		int[] id = new int[]{10057, 10058};

		Animation animation = new Animation(RandomUtils.random(id));
		npc.animation(animation);
		return new CombatHit(npc, victim, 1, CombatType.MELEE, true);
	}

	private CombatNormalSpell getSpell() {
		ImmutableList<CombatNormalSpell> spells = ImmutableList.of(TRANSCULENT_BALL_OF_ENERGY, HIT_THROUGH_PRAYER_SPELL);
		return RandomUtils.random(spells);
	}

	private CombatHit magic(Actor victim, List<Player> players) {
		CombatNormalSpell spell = getSpell();

		npc.animation(new Animation(10410));

		Position originalVictimPosition = victim.getPosition();
		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(npc.getState() != EntityState.ACTIVE || victim.getState() != EntityState.ACTIVE || npc.isDead() || victim.isDead()) {
					return;
				}
				if(spell.equals(TRANSCULENT_BALL_OF_ENERGY)) {
					new Projectile(npc.getCenterPosition(), originalVictimPosition, 0, 1824, 44, 3, 60, 36, 0, npc.getInstance(), CombatType.MAGIC).sendProjectile();
				} else {
					spell.projectile(npc, victim).get().sendProjectile();
				}
			}
		});
		npc.setCurrentlyCasting(spell);
		
		return new CombatHit(npc, victim, 1, CombatType.MAGIC, false) {
			@Override
			public CombatHit preAttack() {
				CombatHit data = this;

				if(spell.equals(TRANSCULENT_BALL_OF_ENERGY) && !victim.getPosition().equals(originalVictimPosition)) {
					data.ignore();
				}
				
				return data;
			}

			@Override
			public void postAttack(int counter) {
				if(!spell.equals(TRANSCULENT_BALL_OF_ENERGY)) {
					return;
				}

				ObjectList<Position> positions = TraversalMap.getNonDiagonalNearbyTraversableTiles(originalVictimPosition, 3);
				positions.forEach(p -> new Projectile(originalVictimPosition, p, 0, 1824, 44, 4, 60, 43, 0, npc.getInstance(), CombatType.MAGIC).sendProjectile());
				World.get().submit(new Task(1, false) {
					@Override
					protected void execute() {
						this.cancel();
						for(Player player : npc.getRegion().getPlayers()) {
							if(player == null) {
								continue;
							}
							if(positions.stream().anyMatch(p -> p.equals(player.getPosition()))) {
								player.damage(new Hit(RandomUtils.inclusive(40), Hit.HitType.NORMAL, Hit.HitIcon.MAGIC, player.getSlot()));
							}
						}
					}

				});
			}
		};
	}

	private CombatHit ranged(Actor victim, List<Player> players) {
		npc.animation(new Animation(10410));

		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(npc.getState() != EntityState.ACTIVE || victim.getState() != EntityState.ACTIVE || npc.isDead() || victim.isDead())
					return;
				new Projectile(npc, victim, 1823, 44, 3, 60, 36, 0).sendProjectile();
			}
		});

		return new CombatHit(npc, victim, 1, CombatType.RANGED, false);
	}

	private CombatHit type(Actor victim, CombatType type, List<Player> players) {
		switch(type) {
			case MELEE:
				return melee(victim, players);
			case RANGED:
				return ranged(victim, players);
			case MAGIC:
				return magic(victim, players);
			default:
				return magic(victim, players);
		}
	}

	@Override
	public int attackDelay() {
		return npc.getAttackSpeed() - 2;
	}

	@Override
	public int attackDistance() {
		return 8;
	}

	/**
	 * The magic drain spell.
	 */
	private static final CombatNormalSpell TRANSCULENT_BALL_OF_ENERGY = new CombatNormalSpell() {

		@Override
		public int spellId() {
			return 0;
		}

		@Override
		public int maximumHit() {
			return 550;
		}

		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(10410));
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
		public void executeOnHit(Actor cast, Actor castOn, boolean accurate, int damage) {
			if(!castOn.isPlayer()) {
				return;
			}

			if(RandomUtils.inclusive(100) <= 70) {
				return;
			}

			Player player = castOn.toPlayer();

			String name = RandomUtils.random(new String[]{"ATTACK", "STRENGTH", "DEFENCE", "RANGED", "MAGIC"});

			SkillData skill = SkillData.valueOf(name);

			player.getSkills()[skill.getId()].decreaseLevel(1 + RandomUtils.inclusive(4));
			player.message("@red@Your " + skill.toString() + " has been slightly drained!");
		}

		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.empty();
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

	/**
	 * The hit through prayer spell.
	 */
	private static final CombatNormalSpell HIT_THROUGH_PRAYER_SPELL = new CombatNormalSpell() {

		@Override
		public int spellId() {
			return 0;
		}

		@Override
		public int maximumHit() {
			return 650;
		}

		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(10410));
		}

		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}

		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 1825, 44, 3, 60, 36, 0));
		}

		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.empty();
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
