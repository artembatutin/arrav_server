package net.edge.world.node.item;

import net.edge.locale.Position;
import net.edge.world.World;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.player.Player;

/**
 * The node implementation that represents an item everyone can see by default
 * on the ground.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemNodeStatic extends ItemNode {
	
	/**
	 * The current item policy of this node.
	 */
	private final ItemPolicy policy;
	
	/**
	 * The flag that determines if this item node is awaiting respawn.
	 */
	private boolean needsRespawn;
	
	/**
	 * Creates a new {@link ItemNodeStatic}.
	 * @param item     the item concealed within this node.
	 * @param position the position this node is on.
	 * @param policy   the current item policy of this node.
	 */
	public ItemNodeStatic(Item item, Position position, ItemPolicy policy) {
		super(item, position, null);
		super.setState(ItemState.SEEN_BY_EVERYONE);
		this.policy = policy;
	}
	
	/**
	 * Creates a new {@link ItemNodeStatic} with the default policy.
	 * @param item     the item concealed within this node.
	 * @param position the position this node is on.
	 */
	public ItemNodeStatic(Item item, Position position) {
		this(item, position, ItemPolicy.TIMEOUT);
	}
	
	@Override
	public void register() {
		World.getRegions().getAllSurroundingRegions(getPosition().getRegion()).forEach(r -> r.getPlayers().forEach((i, p) -> p.getMessages().sendGroundItem(this)));
	}
	
	@Override
	public void onSequence() {
		switch(policy) {
			case TIMEOUT:
				setState(NodeState.INACTIVE);
				break;
			case RESPAWN:
				if(needsRespawn) {
					super.setState(ItemState.SEEN_BY_EVERYONE);
					needsRespawn = false;
					register();
				}
				break;
		}
	}
	
	@Override
	public void onPickup(Player player) {
		if(player.getInventory().add(super.getItem())) {
			switch(policy) {
				case TIMEOUT:
					ItemNodeManager.register(this);
					break;
				case RESPAWN:
					dispose();
					needsRespawn = true;
					super.getCounter().set(0);
					super.setState(ItemState.HIDDEN);
					break;
			}
		}
	}
}