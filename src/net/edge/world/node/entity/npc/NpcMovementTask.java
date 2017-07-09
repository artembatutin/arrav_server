package net.edge.world.node.entity.npc;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.locale.Boundary;
import net.edge.locale.Position;
import net.edge.world.Direction;
import net.edge.world.World;
import net.edge.world.node.entity.move.path.Path;
import net.edge.world.node.region.TraversalMap;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * An implementation of a {@link Task} that handles randomized {@link Npc} movements.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class NpcMovementTask extends Task {
	
	/**
	 * The Queue of Npcs.
	 */
	private final ObjectList<Npc> npcs = new ObjectArrayList<>();
	
	/**
	 * Creates a new {@link NpcMovementTask}.
	 */
	public NpcMovementTask() {
		super(2, false);
	}
	
	@Override
	protected void execute() {
		int size = npcs.size();
		int count = RandomUtils.inclusive(size);
		for(int iterator = 0; iterator < count; iterator++) {
			Npc npc = npcs.get(RandomUtils.inclusive(size - 1));
			if(npc == null)
				break;
			if(!npc.active())
				break;
			NpcMovementCoordinator move = npc.getMovementCoordinator();
			if(!move.isCoordinate() || npc.getCombatBuilder().isAttacking() || npc.getCombatBuilder().isBeingAttacked()) {
				return;
			}
			if(npc.getMovementQueue().isMovementDone()) {
				if(!move.getBoundary().inside(npc.getPosition(), npc.size())) {
					Path pathHome = npc.getAStarPathFinder().find(npc.getOriginalPosition());
					if(pathHome != null && pathHome.isPossible())
						npc.getMovementQueue().walk(pathHome.getMoves());
				} else {
					Direction dir = Direction.random();
					int random_x = npc.toNpc().getPosition().getX() + randomSteps(npc.size());
					int random_y = npc.toNpc().getPosition().getY() + randomSteps(npc.size());
					Position generated_random_position = new Position(random_x, random_y);
					Boundary boundary = new Boundary(generated_random_position, npc.size());
					TraversalMap traverse = new TraversalMap();
					boolean traversable = traverse.isTraversable(generated_random_position, boundary, dir, npc.size());
					if(traversable) {
						Path pathHome = World.getSimplePathFinder().find(npc, generated_random_position);
						if(pathHome.isPossible())
							npc.getMovementQueue().walk(pathHome.getMoves());
						else
							break;
					}
				}
			}
		}
	}
	
	private int randomSteps(int size) {
		return RandomUtils.inclusive(0, size);
	}
	
	ObjectList<Npc> getNpcs() {
		return npcs;
	}
}
