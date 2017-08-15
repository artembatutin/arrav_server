package net.edge.world.entity.item;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.net.packet.out.SendItemNode;
import net.edge.net.packet.out.SendItemNodeRemoval;
import net.edge.util.MutableNumber;
import net.edge.world.locale.Position;
import net.edge.world.World;
import net.edge.world.entity.Entity;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.EntityType;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.region.Region;

import java.util.Optional;

/**
 * The node implementation that represents an item on the ground.
 * @author lare96 <http://github.com/lare96>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class GroundItem extends Entity {
	
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
	private GroundItemState state = GroundItemState.SEEN_BY_OWNER;
	
	/**
	 * The player attached to this node.
	 */
	private Player player;
	
	/**
	 * Creates new {@link GroundItem}.
	 * @param item     the item concealed within this node.
	 * @param position the position this node is on.
	 * @param player   the player attached to this node.
	 */
	public GroundItem(Item item, Position position, Player player) {
		super(position, EntityType.ITEM);
		this.item = item.copy();
		this.player = player;
	}
	
	@Override
	public void register() {
		player.out(new SendItemNode(this));
	}
	
	@Override
	public void dispose() {
		ObjectList<Region> surrounding = World.getRegions().getAllSurroundingRegions(getPosition().getRegion());
		if(surrounding != null) {
			for(Region r : surrounding) {
				if(r == null)
					continue;
				for(Player p : r.getPlayers()) {
					if(p == null)
						continue;
					if(p.getPosition().getZ() == super.getPosition().getZ() && p.getInstance() == super.getInstance())
						p.out(new SendItemNodeRemoval(this));
				}
			}
		}
	}
	
	/**
	 * The method executed on every sequence by the item node manager.
	 * @throws IllegalStateException if the item node is in an incorrect state.
	 */
	public void onSequence() {
		switch(state) {
			case SEEN_BY_OWNER:
				ObjectList<Region> surrounding = World.getRegions().getAllSurroundingRegions(getPosition().getRegion());
				if(surrounding != null) {
					for(Region r : surrounding) {
						if(r == null)
							continue;
						for(Player p : r.getPlayers()) {
							if(p == null)
								continue;
							if(!p.same(player) && p.getPosition().getZ() == super.getPosition().getZ() && p.getInstance() == super.getInstance())
								p.out(new SendItemNode(this));
						}
					}
				}
				state = GroundItemState.SEEN_BY_EVERYONE;
				break;
			case SEEN_BY_EVERYONE:
				this.setState(EntityState.INACTIVE);
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
			this.setState(EntityState.INACTIVE);
		}
	}
	
	/**
	 * Gets the item state of this node.
	 * @return the item state.
	 */
	public final GroundItemState getItemState() {
		return state;
	}
	
	/**
	 * Sets the value for {@link GroundItem#state}.
	 * @param state the new value to set.
	 */
	public final void setState(GroundItemState state) {
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
	 * Sets the value for {@link GroundItem#player}.
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
	public Optional<Region> getRegion() {
		return World.getRegions().getRegion(getPosition());
	}
	
	
}