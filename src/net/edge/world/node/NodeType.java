package net.edge.world.node;

import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.ItemNode;
import net.edge.world.node.object.ObjectNode;
import net.edge.world.node.region.Region;

/**
 * The enumerated type whose elements represent the different types of {@link Node} implementations.
 * @author lare96 <http://github.com/lare96>
 */
public enum NodeType {
	
	/**
	 * The element used to represent the {@link ItemNode} implementation.
	 */
	ITEM,
	
	/**
	 * The element used to represent the {@link ObjectNode} implementation.
	 */
	OBJECT,
	
	/**
	 * The element used to represent the {@link Player} implementation.
	 */
	PLAYER,
	
	/**
	 * The element used to represent the {@link Npc} implementation.
	 */
	NPC,
	
	/**
	 * The element used to represent the {@link Region} implementation.
	 */
	REGION
}