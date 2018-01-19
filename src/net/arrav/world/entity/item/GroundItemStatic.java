package net.arrav.world.entity.item;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.net.packet.out.SendItemNode;
import net.arrav.world.World;
import net.arrav.world.entity.EntityState;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.region.Region;
import net.arrav.world.locale.Position;

/**
 * The node implementation that represents an item everyone can see by default
 * on the ground.
 * @author lare96 <http://github.com/lare96>
 */
public final class GroundItemStatic extends GroundItem {
	
	/**
	 * The current item policy of this node.
	 */
	private final GroundItemPolicy policy;
	
	/**
	 * The flag that determines if this item node is awaiting respawn.
	 */
	private boolean needsRespawn;
	
	/**
	 * Creates a new {@link GroundItemStatic}.
	 * @param item     the item concealed within this node.
	 * @param position the position this node is on.
	 * @param policy   the current item policy of this node.
	 */
	public GroundItemStatic(Item item, Position position, GroundItemPolicy policy) {
		super(item, position, null);
		super.setState(GroundItemState.SEEN_BY_EVERYONE);
		this.policy = policy;
	}
	
	/**
	 * Creates a new {@link GroundItemStatic} with the default policy.
	 * @param item     the item concealed within this node.
	 * @param position the position this node is on.
	 */
	public GroundItemStatic(Item item, Position position) {
		this(item, position, GroundItemPolicy.TIMEOUT);
	}
	
	@Override
	public void register() {
		ObjectList<Region> surrounding = World.getRegions().getAllSurroundingRegions(getPosition().getRegion());
		if(surrounding != null) {
			for(Region r : surrounding) {
				if(r == null)
					continue;
				for(Player p : r.getPlayers()) {
					if(p == null)
						continue;
					p.out(new SendItemNode(this));
				}
			}
		}
	}
	
	@Override
	public void onSequence() {
		if(policy == null)
			return;
		switch(policy) {
			case TIMEOUT:
				setState(EntityState.INACTIVE);
				break;
			case RESPAWN:
				if(needsRespawn) {
					super.setState(GroundItemState.SEEN_BY_EVERYONE);
					needsRespawn = false;
					register();
				}
				break;
		}
	}
	
	@Override
	public void onPickup(Player player) {
		if(player.getInventory().add(super.getItem()) != -1) {
			switch(policy) {
				case TIMEOUT:
					getRegion().ifPresent(r -> r.unregister(this));
					break;
				case RESPAWN:
					dispose();
					needsRespawn = true;
					super.getCounter().set(0);
					super.setState(GroundItemState.HIDDEN);
					break;
			}
		}
	}
}