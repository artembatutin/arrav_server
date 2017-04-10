package net.edge.world.model.node;

import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.ItemNode;
import net.edge.world.model.node.object.ObjectNode;
import net.edge.world.model.node.region.Region;

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