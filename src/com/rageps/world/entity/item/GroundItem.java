package com.rageps.world.entity.item;

import com.rageps.content.market.MarketItem;
import com.rageps.net.packet.out.SendItemNode;
import com.rageps.net.packet.out.SendItemNodeRemoval;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.region.Region;
import com.rageps.world.locale.Position;
import it.unimi.dsi.fastutil.objects.ObjectList;
import com.rageps.util.MutableNumber;
import com.rageps.util.log.impl.DropItemLog;
import com.rageps.world.entity.Entity;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.EntityType;

import java.util.Optional;

/**
 * The node implementation that represents an item on the ground.
 * @author lare96 <http://github.com/lare96>
 * @author Artem Batutin
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
	 * @param item the item concealed within this node.
	 * @param position the position this node is on.
	 * @param player the player attached to this node.
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
				int val = MarketItem.get(item.getId()) != null ? MarketItem.get(item.getId()).getPrice() * item.getAmount() : 0;
				if(val > 1_000)
					World.getLoggingManager().write(new DropItemLog(player, item, player.getPosition(), Optional.empty()));
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
		if(getState() == EntityState.ACTIVE) {
			if(player.getInventory().add(item) != -1) {
				this.setState(EntityState.INACTIVE);
			}
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
	public Region getRegion() {
		return World.getRegions().getRegion(getPosition());
	}
	
	/**
	 * Creates the {@link GroundItem} on the regional map.
	 * @return if successfully created.
	 */
	public boolean create() {
		return create(false);
	}
	
	/**
	 * Creates the {@link GroundItem} on the regional map.
	 * @param stack if stacking ground items.
	 * @return if successfully created.
	 */
	public boolean create(boolean stack) {
		Region reg = getRegion();
		if(reg == null)
			return false;
		return reg.register(this, stack);
	}
	
	/**
	 * Removes from the {@link GroundItem} on the regional map.
	 * @return if successfully removed.
	 */
	public boolean remove() {
		Region reg = getRegion();
		if(reg == null)
			return false;
		return reg.unregister(this);
	}
	
}