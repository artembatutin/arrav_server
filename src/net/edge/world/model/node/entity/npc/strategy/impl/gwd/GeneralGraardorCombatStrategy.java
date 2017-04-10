package net.edge.world.model.node.entity.npc.strategy.impl.gwd;

import net.edge.utils.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.content.combat.CombatSessionData;
import net.edge.world.content.combat.CombatType;
import net.edge.world.model.node.NodeState;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.model.Projectile;
import net.edge.world.model.node.entity.npc.impl.gwd.GeneralGraardor;
import net.edge.world.model.node.entity.npc.strategy.DynamicCombatStrategy;
import net.edge.world.model.node.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The dynamic combat strategy for the general graardor boss.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class GeneralGraardorCombatStrategy extends DynamicCombatStrategy<GeneralGraardor> {

	/**
	 * Creates a new {@link GeneralGraardorCombatStrategy}.
	 * @param npc the npc this strategy is for.
	 */
	public GeneralGraardorCombatStrategy(GeneralGraardor npc) {
		super(npc);
	}

	@Override
	public boolean canOutgoingAttack(EntityNode victim) {
		return victim.isPlayer() && GeneralGraardor.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatSessionData outgoingAttack(EntityNode victim) {
		CombatType[] data = npc.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.RANGED} : new CombatType[]{CombatType.RANGED};
		CombatType c = RandomUtils.random(data);
		GeneralGraardor.SERGEANTS.forEach(sergeant -> {
			if(!sergeant.isDead() && sergeant.getState() == NodeState.ACTIVE) {
				sergeant.getCombatBuilder().attack(victim);
			}
		});
		npc.forceChat(RandomUtils.random(GeneralGraardor.RANDOM_CHAT));
		return type(victim, c);
	}

	private CombatSessionData melee(EntityNode victim) {
		npc.animation(new Animation(7060));
		return new CombatSessionData(npc, victim, 1, CombatType.MELEE, true);
	}

	private CombatSessionData ranged(EntityNode victim) {
		List<Player> toHit = new ArrayList<>();
		npc.animation(new Animation(7063));
		World.getLocalPlayers(victim).forEachRemaining(player -> {
			if(GeneralGraardor.CHAMBER.inLocation(player.getPosition())) {
				toHit.add(player);
			}
		});
		toHit.forEach(player -> new Projectile(npc, player, 1200, 44, 3, 43, 31, 0).sendProjectile());
		return new CombatSessionData(npc, victim, 1, CombatType.RANGED, true) {
			@Override
			public void postAttack(int counter) {
				toHit.forEach(player -> player.damage(this.getHits()));
			}
		};
	}

	private CombatSessionData type(EntityNode victim, CombatType type) {
		switch(type) {
			case MELEE:
				return melee(victim);
			case RANGED:
				return ranged(victim);
			default:
				return ranged(victim);
		}
	}

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
