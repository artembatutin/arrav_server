package net.edge.world.model.node.entity.npc.impl.gwd;

import com.google.common.collect.ImmutableSet;
import net.edge.world.World;
import net.edge.world.model.locale.Position;
import net.edge.world.model.locale.SquareLocation;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.npc.NpcAggression;
import net.edge.world.model.node.entity.npc.impl.DefaultNpc;
import net.edge.world.model.node.entity.npc.strategy.impl.gwd.KreeArraCombatStrategy;

import java.util.Optional;

/**
 * The class which represents a single kree'arra boss with it's minions.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class KreeArra extends Npc {
	
	/**
	 * The chamber of this boss.
	 */
	public static final SquareLocation CHAMBER = new SquareLocation(2824, 5296, 2842, 5308, 2);
	
	/**
	 * The skree minion.
	 */
	private static final Npc WINGMAN_SKREE = new DefaultNpc(6223, new Position(0, 0, 0));
	
	/**
	 * The gerin minion.
	 */
	private static final Npc FLOCKLEADER_GERIN = new DefaultNpc(6225, new Position(0, 0, 0));
	
	/**
	 * The kilisa minion.
	 */
	private static final Npc FLIGHT_KILISA = new DefaultNpc(6227, new Position(0, 0, 0));
	
	/**
	 * A collection of all three minions.
	 */
	public static final ImmutableSet<Npc> AVIANTESES = ImmutableSet.of(WINGMAN_SKREE, FLOCKLEADER_GERIN, FLIGHT_KILISA);
	
	/**
	 * Constructs a new {@link KreeArra}.
	 */
	public KreeArra() {
		super(6222, new Position(2832, 5302, 2));
		setStrategy(Optional.of(new KreeArraCombatStrategy(this)));
		this.setOriginalRandomWalk(true);
		this.getMovementCoordinator().setCoordinate(true);
		this.getMovementCoordinator().setRadius(3);
	}
	
	@Override
	public Npc create() {
		return new KreeArra();
	}
	
	@Override
	public void register() {
		WINGMAN_SKREE.setPosition(new Position(2831, 5298, 2));
		FLOCKLEADER_GERIN.setPosition(new Position(2840, 5299, 2));
		FLIGHT_KILISA.setPosition(new Position(2834, 5305, 2));
		AVIANTESES.forEach(aviantese -> {
			aviantese.setRespawn(false);
			aviantese.setOriginalRandomWalk(true);
			aviantese.getMovementCoordinator().setCoordinate(true);
			aviantese.getMovementCoordinator().setRadius(3);
			NpcAggression.AGGRESSIVE.add(aviantese.getId());
			World.getNpcs().add(aviantese);
		});
		NpcAggression.AGGRESSIVE.add(this.getId());
	}
	
	@Override
	public void appendDeath() {
		AVIANTESES.stream().filter(a -> !a.isDead()).forEach(Npc::appendDeath);
		super.appendDeath();
	}
}
