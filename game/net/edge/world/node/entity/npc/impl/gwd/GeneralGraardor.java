package net.edge.world.node.entity.npc.impl.gwd;

import com.google.common.collect.ImmutableSet;
import net.edge.world.World;
import net.edge.locale.Position;
import net.edge.locale.loc.SquareLocation;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.NpcAggression;
import net.edge.world.node.entity.npc.impl.DefaultNpc;
import net.edge.world.node.entity.npc.strategy.impl.gwd.GeneralGraardorCombatStrategy;

import java.util.Optional;

/**
 * The class which represents a single graardor boss with it's minions.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class GeneralGraardor extends Npc {
	
	/**
	 * The chamber of this boss.
	 */
	public static final SquareLocation CHAMBER = new SquareLocation(2864, 5351, 2876, 5369, 2);
	
	/**
	 * The random forced chats the graardor yells when attacking the victim.
	 */
	public static final String[] RANDOM_CHAT = new String[]{"Death to our enemies!", "Brargh!", "Break their bones!", "For the glory of Bandos!", "Split their skulls!", "We feast on the bones of our enemies tonight!", "CHAAARGE!", "Crush them underfoot!", "All glory to Bandos!", "GRRRAAAAAR!", "FOR THE GLORY OF THE BIG HIGH WAR GOD!"};
	
	/**
	 * The steelwill minion.
	 */
	private static final Npc SERGEANT_STEELWILL = new DefaultNpc(6263, new Position(0, 0));
	
	/**
	 * The grimspike minion.
	 */
	private static final Npc SERGEANT_GRIMSPIKE = new DefaultNpc(6265, new Position(0, 0));
	
	/**
	 * The strongstack minion.
	 */
	private static final Npc SERGEANT_STRONGSTACK = new DefaultNpc(6261, new Position(0, 0));
	
	/**
	 * A collection of all three minions.
	 */
	public static final ImmutableSet<Npc> SERGEANTS = ImmutableSet.of(SERGEANT_STEELWILL, SERGEANT_GRIMSPIKE, SERGEANT_STRONGSTACK);
	
	/**
	 * Constructs a new {@link GeneralGraardor}.
	 */
	public GeneralGraardor() {
		super(6260, new Position(2868, 5360, 2));
		this.setStrategy(Optional.of(new GeneralGraardorCombatStrategy(this)));
		this.setOriginalRandomWalk(true);
		this.getMovementCoordinator().setCoordinate(true);
		this.getMovementCoordinator().setRadius(3);
	}
	
	@Override
	public Npc create() {
		return new GeneralGraardor();
	}
	
	@Override
	public void register() {
		SERGEANT_STEELWILL.setPosition(new Position(2874, 5354, 2));
		SERGEANT_GRIMSPIKE.setPosition(new Position(2866, 5360, 2));
		SERGEANT_STRONGSTACK.setPosition(new Position(2873, 5364, 2));
		SERGEANTS.forEach(sergeant -> {
			sergeant.setRespawn(false);
			sergeant.setOriginalRandomWalk(true);
			sergeant.getMovementCoordinator().setCoordinate(true);
			sergeant.getMovementCoordinator().setRadius(3);
			NpcAggression.AGGRESSIVE.add(sergeant.getId());
			World.get().getNpcs().add(sergeant);
		});
		NpcAggression.AGGRESSIVE.add(this.getId());
	}
	
	@Override
	public void appendDeath() {
		SERGEANTS.stream().filter(s -> !s.isDead()).forEach(Npc::appendDeath);
		super.appendDeath();
	}
}
