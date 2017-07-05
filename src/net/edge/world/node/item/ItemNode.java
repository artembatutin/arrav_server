package net.edge.world.node.item;

import net.edge.util.MutableNumber;
import net.edge.locale.Position;
import net.edge.world.World;
import net.edge.world.node.Node;
import net.edge.world.node.NodeState;
import net.edge.world.node.NodeType;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.region.Region;

/**
 * The node implementation that represents an item on the ground.
 * @author lare96 <http://github.com/lare96>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class ItemNode extends Node {
	
	/**
	 * The item concealed within this node.
	 */
	private final Item item;
	
	/**
	 * The counter that contains the amount of ticks this node has.
	 */
	private final MutableNumber counter = new MutableNumber();
	
	/**
	 * The item state of this node.
	 */
	private ItemState state = ItemState.SEEN_BY_OWNER;
	
	/**
	 * The player attached to this node.
	 */
	private Player player;
	
	/**
	 * Creates new {@link ItemNode}.
	 * @param item     the item concealed within this node.
	 * @param position the position this node is on.
	 * @param player   the player attached to this node.
	 */
	public ItemNode(Item item, Position position, Player player) {
		super(position, NodeType.ITEM);
		this.item = item.copy();
		this.player = player;
	}
	
	@Override
	public void register() {
		player.getMessages().sendGroundItem(this);
	}
	
	@Override
	public void dispose() {
		World.getRegions().getAllSurroundingRegions(getPosition().getRegion()).forEach(r -> r.getPlayers().forEach(p -> {
			if(p.getPosition().getZ() == super.getPosition().getZ() && p.getInstance() == super.getInstance())
				p.getMessages().sendRemoveGroundItem(this);
		}));
	}
	
	/**
	 * The method executed on every sequence by the item node manager.
	 * @throws IllegalStateException if the item node is in an incorrect state.
	 */
	public void onSequence() {
		switch(state) {
			case SEEN_BY_OWNER:
				World.getRegions().getAllSurroundingRegions(getPosition().getRegion()).forEach(r -> r.getPlayers().forEach(p -> {
					if(!p.same(player) && p.getPosition().getZ() == super.getPosition().getZ() && p.getInstance() == super.getInstance())
						p.getMessages().sendGroundItem(new ItemNode(item, super.getPosition(), null));
				}));
				player = null;
				state = ItemState.SEEN_BY_EVERYONE;
				break;
			case SEEN_BY_EVERYONE:
				this.setState(NodeState.INACTIVE);
				break;
			default:
				throw new IllegalStateException("Invalid item node state!");
		}
	}
	
	/**
	 * The method executed when {@code player} attempts to pickup this item.
	 * @param player the player attempting to pickup this item.
	 */
	public void onPickup(Player player) {
		if(player.getInventory().add(item) != -1) {
			this.setState(NodeState.INACTIVE);
		}
	}
	
	/**
	 * Gets the item state of this node.
	 * @return the item state.
	 */
	public final ItemState getItemState() {
		return state;
	}
	
	/**
	 * Sets the value for {@link ItemNode#state}.
	 * @param state the new value to set.
	 */
	public final void setState(ItemState state) {
		this.state = state;
	}
	
	/**
	 * Gets the player attached to this node.
	 * @return the player attached.
	 */
	public final Player getPlayer() {
		return player;
	}
	
	/**
	 * Sets the value for {@link ItemNode#player}.
	 * @param player the new value to set.
	 */
	public final void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * Gets the item concealed within this node.
	 * @return the item concealed.
	 */
	public final Item getItem() {
		return item;
	}
	
	/**
	 * Gets the counter that contains the amount of ticks this node has.
	 * @return the counter that contains the ticks.
	 */
	public final MutableNumber getCounter() {
		return counter;
	}
	
	/**
	 * Gets the region on which the item is standing.
	 * @return the region of this item.
	 */
	public Region getRegion() {
		return World.getRegions().getRegion(getPosition());
	}
	
	
}