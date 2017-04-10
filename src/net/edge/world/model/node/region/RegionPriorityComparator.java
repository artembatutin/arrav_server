package net.edge.world.model.node.region;

import net.edge.world.model.locale.Position;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.player.Player;

import java.util.Comparator;

/**
 * A {@link Comparator} implementation that compares {@link EntityNode}s being added to the local lists of {@link Player}s.
 * The purpose of this is to prevent the loss of functionality when staggering updating.
 * @author lare96 <http://github.org/lare96>
 */
public final class RegionPriorityComparator implements Comparator<EntityNode> {
	
	/**
	 * The {@link Player} being updated.
	 */
	private final Player player;
	
	/**
	 * Creates a new {@link RegionPriorityComparator}.
	 * @param player The {@link Player} being updated.
	 */
	public RegionPriorityComparator(Player player) {
		this.player = player;
	}
	
	@Override
	public int compare(EntityNode o1, EntityNode o2) {
		int oneScore = 0;
		int twoScore = 0;
		Position pos = player.getPosition();
		
		if(o1.getPosition().getDistance(pos) > o2.getPosition().getDistance(pos)) {
			oneScore = 1;
		} else {
			twoScore = 1;
		}
		
		if(player.getCombatBuilder() != null) {
			if(player.getCombatBuilder().getVictim() == o1) {
				twoScore = 2;
			} else if(player.getCombatBuilder().getVictim() == o2) {
				oneScore = 2;
			}
		}
		
		return Integer.compare(oneScore, twoScore);
	}
}