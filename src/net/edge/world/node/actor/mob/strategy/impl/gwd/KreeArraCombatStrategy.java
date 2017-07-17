package net.edge.world.node.actor.mob.strategy.impl.gwd;

import net.edge.content.combat.CombatHit;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.locale.Position;
import net.edge.world.World;
import net.edge.world.node.NodeState;
import net.edge.world.node.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.node.actor.mob.impl.gwd.KreeArra;
import net.edge.world.node.actor.mob.strategy.DynamicCombatStrategy;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * The dynamic combat strategy for the kree'arra boss.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class KreeArraCombatStrategy extends DynamicCombatStrategy<KreeArra> {

	/**
	 * Creates a new {@link KreeArraCombatStrategy}.
	 * @param npc the npc this strategy is for.
	 */
	public KreeArraCombatStrategy(KreeArra npc) {
		super(npc);
	}

	@Override
	public boolean canOutgoingAttack(Actor victim) {
		return victim.isPlayer() && KreeArra.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatHit outgoingAttack(Actor victim) {
		npc.animation(new Animation(npc.getDefinition().getAttackAnimation()));
		CombatType[] data = npc.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.MAGIC, CombatType.RANGED} : new CombatType[]{CombatType.MAGIC, CombatType.RANGED};
		CombatType c = RandomUtils.random(data);
		KreeArra.AVIANTESES.forEach(aviantese -> {
			if(!aviantese.isDead() && aviantese.getState() == NodeState.ACTIVE) {
				aviantese.getCombatBuilder().attack(victim);
			}
		});
		return type(victim, c);
	}

	private CombatHit melee(Actor victim) {
		return new CombatHit(npc, victim, 1, CombatType.MELEE, true);
	}

	private CombatHit ranged(Actor victim) {
		new Projectile(npc, victim, 1197, 44, 3, 43, 43, 0).sendProjectile();
		return new CombatHit(npc, victim, 1, CombatType.RANGED, true) {
			@Override
			public void postAttack(int counter) {
				if(RandomUtils.inclusive(100) < 60) {
					return;
				}
				
				Position position = World.getTraversalMap().getRandomNearby(victim.getPosition(), npc.getPosition(), 2);
				victim.getMovementQueue().reset();
				if(position != null)
					victim.move(position);
				victim.graphic(new Graphic(128));
			}
		};
	}

	private CombatHit magic(Actor victim) {
		npc.setCurrentlyCasting(SPELL);
		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(npc.getState() != NodeState.ACTIVE || victim.getState() != NodeState.ACTIVE || npc.isDead() || victim.isDead()) {
					return;
				}
				SPELL.projectile(npc, victim).ifPresent(Projectile::sendProjectile);
			}
		});
		return new CombatHit(npc, victim, 1, CombatType.MAGIC, true);
	}

	private CombatHit type(Actor victim, CombatType c) {
		switch(c) {
			case MAGIC:
				return magic(victim);
			case MELEE:
				return melee(victim);
			case RANGED:
				return ranged(victim);
			default:
				return magic(victim);
		}
	}

	private static final CombatNormalSpell SPELL = new CombatNormalSpell() {

		@Override
		public int spellId() {
			return 0;
		}

		@Override
		public int maximumHit() {
			return 210;
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
			return Optional.of(new Projectile(cast, castOn, 1198, 44, 3, 43, 43, 0));
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

	@Override
	public void incomingAttack(Actor attacker, CombatHit data) {

	}

	@Override
	public int attackDelay() {
		return npc.getAttackSpeed();
	}

	@Override
	public int attackDistance() {
		return 7;
	}
}
