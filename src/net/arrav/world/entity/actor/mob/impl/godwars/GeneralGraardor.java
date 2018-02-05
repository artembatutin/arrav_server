package net.arrav.world.entity.actor.mob.impl.godwars;

import com.google.common.collect.ImmutableSet;
import net.arrav.world.World;
import net.arrav.world.entity.actor.combat.strategy.npc.boss.godwars.bandos.GeneralGraardorStrategy;
import net.arrav.world.entity.actor.mob.DefaultMob;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.mob.MobAggression;
import net.arrav.world.locale.Position;
import net.arrav.world.locale.loc.SquareLocation;

import java.util.Optional;

/**
 * The class which represents a single graardor boss with it's minions.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class GeneralGraardor extends Mob {
	
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
	private static final Mob SERGEANT_STEELWILL = new DefaultMob(6263, new Position(0, 0));
	
	/**
	 * The grimspike minion.
	 */
	private static final Mob SERGEANT_GRIMSPIKE = new DefaultMob(6265, new Position(0, 0));
	
	/**
	 * The strongstack minion.
	 */
	private static final Mob SERGEANT_STRONGSTACK = new DefaultMob(6261, new Position(0, 0));
	
	/**
	 * A collection of all three minions.
	 */
	public static final ImmutableSet<Mob> SERGEANTS = ImmutableSet.of(SERGEANT_STEELWILL, SERGEANT_GRIMSPIKE, SERGEANT_STRONGSTACK);
	
	/**
	 * Constructs a new {@link GeneralGraardor}.
	 */
	public GeneralGraardor() {
		super(6260, new Position(2868, 5360, 2));
		this.setOriginalRandomWalk(true);
		this.getMovementCoordinator().setCoordinate(true);
		this.getMovementCoordinator().setRadius(3);
	}
	
	@Override
	public Mob create() {
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
			MobAggression.AGGRESSIVE.add(sergeant.getId());
			World.get().getMobs().add(sergeant);
		});
		MobAggression.AGGRESSIVE.add(this.getId());
	}
	
	@Override
	public void appendDeath() {
		SERGEANTS.stream().filter(s -> !s.isDead()).forEach(Mob::appendDeath);
		super.appendDeath();
	}
}
