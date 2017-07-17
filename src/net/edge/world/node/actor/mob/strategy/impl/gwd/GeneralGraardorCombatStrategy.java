package net.edge.world.node.actor.mob.strategy.impl.gwd;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.combat.CombatHit;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.CombatType;
import net.edge.world.World;
import net.edge.world.node.EntityState;
import net.edge.world.node.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Projectile;
import net.edge.world.node.actor.mob.impl.gwd.GeneralGraardor;
import net.edge.world.node.actor.mob.strategy.DynamicCombatStrategy;
import net.edge.world.node.actor.player.Player;

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
	public boolean canOutgoingAttack(Actor victim) {
		return victim.isPlayer() && GeneralGraardor.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatHit outgoingAttack(Actor victim) {
		CombatType[] data = npc.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.RANGED} : new CombatType[]{CombatType.RANGED};
		CombatType c = RandomUtils.random(data);
		GeneralGraardor.SERGEANTS.forEach(sergeant -> {
			if(!sergeant.isDead() && sergeant.getState() == EntityState.ACTIVE) {
				sergeant.getCombatBuilder().attack(victim);
			}
		});
		npc.forceChat(RandomUtils.random(GeneralGraardor.RANDOM_CHAT));
		return type(victim, c);
	}

	private CombatHit melee(Actor victim) {
		npc.animation(new Animation(7060));
		return new CombatHit(npc, victim, 1, CombatType.MELEE, true);
	}

	private CombatHit ranged(Actor victim) {
		ObjectList<Player> toHit = new ObjectArrayList<>();
		npc.animation(new Animation(7063));
		World.get().getLocalPlayers(victim).forEachRemaining(player -> {
			if(GeneralGraardor.CHAMBER.inLocation(player.getPosition())) {
				toHit.add(player);
			}
		});
		toHit.forEach(player -> new Projectile(npc, player, 1200, 44, 3, 43, 31, 0).sendProjectile());
		return new CombatHit(npc, victim, 1, CombatType.RANGED, true) {
			@Override
			public void postAttack(int counter) {
				toHit.forEach(player -> player.damage(this.getHits()));
			}
		};
	}

	private CombatHit type(Actor victim, CombatType type) {
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
