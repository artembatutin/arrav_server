package net.edge.world.node.entity.npc;

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
	 * The comparator used to sort the Npcs in the PriorityQueue.
	 */
	private static final Comparator<Npc> RANDOM_COMPARATOR = (first, second) -> RandomUtils.inclusive(2) - 1;
	
	/**
	 * The Queue of Npcs.
	 */
	private final Queue<Npc> npcs = new PriorityQueue<>(RANDOM_COMPARATOR);
	
	/**
	 * Creates a new {@link NpcMovementTask}.
	 */
	public NpcMovementTask() {
		super(2, false);
	}
	
	@Override
	protected void execute() {
		
		//System.out.println(npcs.size());
		
		int count = RandomUtils.inclusive(npcs.size());
		
		for(int iterator = 0; iterator < npcs.size(); iterator++) {
			
			Npc npc = npcs.poll();
			
			if(npc == null)
				break;
			
			if(!npc.isActive())
				break;
			
			NpcMovementCoordinator move = npc.getMovementCoordinator();
			
			if(!move.isCoordinate() || npc.getCombatBuilder().isAttacking() || npc.getCombatBuilder().isBeingAttacked()) {
				return;
			}
			
			if(npc.getMovementQueue().isMovementDone()) {
				if(!move.getBoundary().inside(npc.getPosition(), npc.size())) {
					Path pathHome = World.getAStarPathFinder().find(npc, npc.getOriginalPosition());
					if(pathHome.isPossible())
						npc.getMovementQueue().walk(pathHome.getMoves());
				} else {
					Direction dir = RandomUtils.random(Direction.values());
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
			npcs.offer(npc);
		}
	}
	
	private int randomSteps(int size) {
		return RandomUtils.inclusive(0, size);
	}
	
	Queue<Npc> getNpcs() {
		return npcs;
	}
}
