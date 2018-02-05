package net.arrav.world.entity.actor.mob.impl.godwars;

import com.google.common.collect.ImmutableSet;
import net.arrav.world.World;
import net.arrav.world.entity.actor.mob.DefaultMob;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.mob.MobAggression;
import net.arrav.world.locale.Position;
import net.arrav.world.locale.loc.SquareLocation;

/**
 * The class which represents a single graardor boss with it's minions.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CommanderZilyana extends Mob {
	
	/**
	 * The chamber of this boss.
	 */
	public static final SquareLocation CHAMBER = new SquareLocation(2889, 5258, 2889, 5258, 0);
	
	/**
	 * The bree minion.
	 */
	private static final Mob BREE = new DefaultMob(6252, new Position(0, 0));
	
	/**
	 * The starlight minion.
	 */
	private static final Mob STARLIGHT = new DefaultMob(6248, new Position(0, 0));
	
	/**
	 * The growler minion.
	 */
	private static final Mob GROWLER = new DefaultMob(6250, new Position(0, 0));
	
	/**
	 * A collection of all three minions.
	 */
	public static final ImmutableSet<Mob> MINIONS = ImmutableSet.of(BREE, STARLIGHT, GROWLER);
	
	/**
	 * Constructs a new {@link CommanderZilyana}.
	 */
	public CommanderZilyana() {
		super(6247, new Position(2901, 5267));
		this.setOriginalRandomWalk(true);
		this.getMovementCoordinator().setCoordinate(true);
		this.getMovementCoordinator().setRadius(3);
	}
	
	@Override
	public Mob create() {
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
			MobAggression.AGGRESSIVE.add(minion.getId());
			World.get().getMobs().add(minion);
		});
		MobAggression.AGGRESSIVE.add(this.getId());
	}
	
	@Override
	public void appendDeath() {
		MINIONS.stream().filter(m -> !m.isDead()).forEach(Mob::appendDeath);
		super.appendDeath();
	}
}
