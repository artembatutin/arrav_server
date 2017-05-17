package net.edge.world.model.node.entity.npc.impl.gwd;

import com.google.common.collect.ImmutableSet;
import net.edge.world.World;
import net.edge.world.model.locale.Position;
import net.edge.world.model.locale.SquareLocation;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.npc.NpcAggression;
import net.edge.world.model.node.entity.npc.impl.DefaultNpc;
import net.edge.world.model.node.entity.npc.strategy.impl.gwd.CommanderZilyanaCombatStrategy;

import java.util.Optional;

/**
 * The class which represents a single graardor boss with it's minions.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CommanderZilyana extends Npc {
	
	/**
	 * The chamber of this boss.
	 */
	public static final SquareLocation CHAMBER = new SquareLocation(2889, 5258, 2889, 5258, 0);
	
	/**
	 * The bree minion.
	 */
	private static final Npc BREE = new DefaultNpc(6252, new Position(0, 0));
	
	/**
	 * The starlight minion.
	 */
	private static final Npc STARLIGHT = new DefaultNpc(6248, new Position(0, 0));
	
	/**
	 * The growler minion.
	 */
	private static final Npc GROWLER = new DefaultNpc(6250, new Position(0, 0));
	
	/**
	 * A collection of all three minions.
	 */
	public static final ImmutableSet<Npc> MINIONS = ImmutableSet.of(BREE, STARLIGHT, GROWLER);
	
	/**
	 * Constructs a new {@link CommanderZilyana}.
	 */
	public CommanderZilyana() {
		super(6247, new Position(2901, 5267));
		this.setStrategy(Optional.of(new CommanderZilyanaCombatStrategy(this)));
		this.setOriginalRandomWalk(true);
		this.getMovementCoordinator().setCoordinate(true);
		this.getMovementCoordinator().setRadius(3);
	}
	
	@Override
	public Npc create() {
		return new CommanderZilyana();
	}
	
	@Override
	public void register() {
		BREE.setPosition(new Position(2902, 5275));
		STARLIGHT.setPosition(new Position(2904, 5267));
		GROWLER.setPosition(new Position(2898, 5263));
		MINIONS.forEach(minion -> {
			minion.setRespawn(false);
			minion.setOriginalRandomWalk(true);
			minion.getMovementCoordinator().setCoordinate(true);
			minion.getMovementCoordinator().setRadius(3);
			NpcAggression.AGGRESSIVE.add(minion.getId());
			World.getNpcs().add(minion);
		});
		NpcAggression.AGGRESSIVE.add(this.getId());
	}
	
	@Override
	public void appendDeath() {
		MINIONS.stream().filter(m -> !m.isDead()).forEach(Npc::appendDeath);
		super.appendDeath();
	}
}
