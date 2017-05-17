package net.edge.world.model.node.entity.npc.strategy.impl.gwd;

import net.edge.task.Task;
import net.edge.utils.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.content.combat.CombatSessionData;
import net.edge.world.content.combat.CombatType;
import net.edge.world.content.combat.magic.CombatNormalSpell;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.NodeState;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.model.Graphic;
import net.edge.world.model.node.entity.model.Projectile;
import net.edge.world.model.node.entity.npc.impl.gwd.KreeArra;
import net.edge.world.model.node.entity.npc.strategy.DynamicCombatStrategy;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	public boolean canOutgoingAttack(EntityNode victim) {
		return victim.isPlayer() && KreeArra.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatSessionData outgoingAttack(EntityNode victim) {
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

	private CombatSessionData melee(EntityNode victim) {
		return new CombatSessionData(npc, victim, 1, CombatType.MELEE, true);
	}

	private CombatSessionData ranged(EntityNode victim) {
		new Projectile(npc, victim, 1197, 44, 3, 43, 43, 0).sendProjectile();
		return new CombatSessionData(npc, victim, 1, CombatType.RANGED, true) {
			@Override
			public void postAttack(int counter) {
				if(RandomUtils.inclusive(100) < 60) {
					return;
				}

				List<Position> position = World.getTraversalMap().getNearbyTraversableTiles(victim.getPosition(), 2).stream().filter(KreeArra.CHAMBER::inLocation).collect(Collectors.toList());

				victim.getMovementQueue().reset();
				victim.move(RandomUtils.random(position));
				victim.graphic(new Graphic(128));
			}
		};
	}

	private CombatSessionData magic(EntityNode victim) {
		npc.setCurrentlyCasting(SPELL);
		World.submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(npc.getState() != NodeState.ACTIVE || victim.getState() != NodeState.ACTIVE || npc.isDead() || victim.isDead()) {
					return;
				}
				SPELL.projectile(npc, victim).ifPresent(Projectile::sendProjectile);
			}
		});
		return new CombatSessionData(npc, victim, 1, CombatType.MAGIC, true);
	}

	private CombatSessionData type(EntityNode victim, CombatType c) {
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
		public Optional<Projectile> projectile(EntityNode cast, EntityNode castOn) {
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
	public void incomingAttack(EntityNode attacker, CombatSessionData data) {

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
