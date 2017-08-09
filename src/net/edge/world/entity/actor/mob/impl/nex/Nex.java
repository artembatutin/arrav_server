package net.edge.world.entity.actor.mob.impl.nex;

import net.edge.content.minigame.nexchamber.NexMinigame;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.strategy.impl.NexStrategy;
import net.edge.world.locale.Position;

import java.util.Optional;

/**
 * The class which represents the nex boss in chamber.
 * @author Artem Batutin <artembatutin@gmail.com>
 * https://www.youtube.com/watch?v=_cJxB5j1HR8
 */
public final class Nex extends Mob {
	
	/**
	 * The nex minion stage.
	 */
	public int minionStage;
	
	private final NexMinion[] minions = {
		new NexMinion(13451, new Position(2914, 5215, 0)),
		new NexMinion(13452, new Position(2936, 5215, 0)),
		new NexMinion(13453, new Position(2936, 5191, 0)),
		new NexMinion(13454, new Position(2914, 5192, 0)),
	};
	
	/**
	 * Constructs a new {@link Nex}.
	 */
	public Nex() {
		super(9177, new Position(3386, 3517));
		setStrategy(Optional.of(new NexStrategy(this)));
		for(NexMinion m : minions) {
			World.get().getMobs().add(m);
			m.setOriginalRandomWalk(false);
		}
		NexMinigame.game = new NexMinigame(this);
	}
	
	@Override
	public Mob create() {
		return new Nex();
	}
	
}
