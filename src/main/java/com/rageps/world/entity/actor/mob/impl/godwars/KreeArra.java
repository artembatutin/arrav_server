package com.rageps.world.entity.actor.mob.impl.godwars;

import com.google.common.collect.ImmutableSet;
import com.rageps.combat.strategy.npc.boss.godwars.armadyl.KreeArraStrategy;
import com.rageps.world.World;
import com.rageps.world.locale.Position;
import com.rageps.world.locale.loc.SquareArea;
import com.rageps.world.entity.actor.mob.DefaultMob;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.mob.MobAggression;

/**
 * The class which represents a single kree'arra boss with it's minions.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class KreeArra extends Mob {
	
	/**
	 * The chamber of this boss.
	 */
	public static final SquareArea CHAMBER = new SquareArea(2824, 5296, 2842, 5308, 2);
	
	/**
	 * The skree minion.
	 */
	private static final Mob WINGMAN_SKREE = new DefaultMob(6223, new Position(0, 0, 0));
	
	/**
	 * The gerin minion.
	 */
	private static final Mob FLOCKLEADER_GERIN = new DefaultMob(6225, new Position(0, 0, 0));
	
	/**
	 * The kilisa minion.
	 */
	private static final Mob FLIGHT_KILISA = new DefaultMob(6227, new Position(0, 0, 0));
	
	/**
	 * A collection of all three minions.
	 */
	public static final ImmutableSet<Mob> AVIANSIES = ImmutableSet.of(WINGMAN_SKREE, FLOCKLEADER_GERIN, FLIGHT_KILISA);
	
	/**
	 * Constructs a new {@link KreeArra}.
	 */
	public KreeArra() {
		super(6222, new Position(2832, 5302, 2));
		this.setOriginalRandomWalk(true);
		this.getMovementCoordinator().setCoordinate(true);
		this.getMovementCoordinator().setRadius(3);
		this.setStrategy(new KreeArraStrategy());
	}
	
	@Override
	public Mob create() {
		return new KreeArra();
	}
	
	@Override
	public void register() {
		WINGMAN_SKREE.setPosition(new Position(2831, 5298, 2));
		FLOCKLEADER_GERIN.setPosition(new Position(2840, 5299, 2));
		FLIGHT_KILISA.setPosition(new Position(2834, 5305, 2));
		AVIANSIES.forEach(aviantese -> {
			aviantese.setRespawn(false);
			aviantese.setOriginalRandomWalk(true);
			aviantese.getMovementCoordinator().setCoordinate(true);
			aviantese.getMovementCoordinator().setRadius(3);
			MobAggression.AGGRESSIVE.add(aviantese.getId());
			World.get().getMobs().add(aviantese);
		});
		MobAggression.AGGRESSIVE.add(this.getId());
	}
	
	@Override
	public void appendDeath() {
		AVIANSIES.stream().filter(a -> !a.isDead()).forEach(Mob::appendDeath);
		super.appendDeath();
	}
}
