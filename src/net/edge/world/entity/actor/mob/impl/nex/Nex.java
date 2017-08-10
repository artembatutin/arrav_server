package net.edge.world.entity.actor.mob.impl.nex;

import net.edge.content.minigame.nexchamber.NexMinigame;
import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.strategy.impl.NexStrategy;
import net.edge.world.entity.actor.player.Player;
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
	 * This should probably be handled on entering the area.
	 */
	public Nex() {
		super(13447, new Position(2924, 5202));
		this.animation(new Animation(6355));
		this.forceChat("AT LAST!");
		setStrategy(Optional.of(new NexStrategy(this)));
		spawn(this);
		for(NexMinion m : minions) {
			m.setOriginalRandomWalk(false);
		}
		NexMinigame.game = new NexMinigame(this);
	}
	
	@Override
	public Mob create() {
		return new Nex();
	}

	public void spawn(Mob mob) {
		mob.getMovementQueue().setLockMovement(true);
		World.get().submit(new Task(6, false) {
			int ticks;
			@Override
			protected void execute() {
				switch(ticks) {
					case 0:
						mob.facePosition(new Position(2913, 5216));
						mob.animation(new Animation(6986));
						World.get().getMobs().add(minions[0]);
						mob.forceChat("Fumus!");
						break;
					case 1:
						mob.facePosition(new Position(2937, 5216));
						mob.animation(new Animation(6986));
						World.get().getMobs().add(minions[1]);
						mob.forceChat("Umbra!");
						break;
					case 2:
						mob.facePosition(new Position(2937, 5191));
						mob.animation(new Animation(6986));
						World.get().getMobs().add(minions[2]);
						mob.forceChat("Cruor!");
						break;
					case 3:
						mob.facePosition(new Position(2913, 5191));
						mob.animation(new Animation(6986));
						World.get().getMobs().add(minions[3]);
						mob.forceChat("Glacies!");
						break;
					case 4:
						mob.forceChat("Fill my soul with smoke!");
						minionStage = 1;
						mob.getMovementQueue().setLockMovement(false);
						break;
				}
				ticks++;
			}
		});

	}
	
}
