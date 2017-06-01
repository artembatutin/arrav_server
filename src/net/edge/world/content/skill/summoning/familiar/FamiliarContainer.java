package net.edge.world.content.skill.summoning.familiar;

import com.google.common.collect.ImmutableList;
import net.edge.world.World;
import net.edge.world.content.container.ItemContainer;
import net.edge.world.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemDefinition;
import net.edge.world.node.item.ItemNodeStatic;
import net.edge.world.region.Region;

/**
 * Holds functionality for abilities which can hold items such as
 * Beast of burden or the forager ability.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class FamiliarContainer extends FamiliarAbility {
	
	/**
	 * The container which holds all the respectable items.
	 */
	private ItemContainer container;
	
	/**
	 * Constructs a new {@link FamiliarContainer}.
	 * @param type the ability type of this familiar.
	 * @param size the size of this container.
	 */
	public FamiliarContainer(FamiliarAbilityType type, int size) {
		super(type);
		container = new ItemContainer(size, ItemContainer.StackPolicy.STANDARD);
	}
	
	/**
	 * Items that are restricted from storing in the container should be added here.
	 */
	private static final ImmutableList<Item> RESTRICTED_ITEMS = ImmutableList.of(new Item(995), new Item(11694));
	
	/**
	 * Any functionality to check for if this item can be stored.
	 * @return <true> if the item could be stored, <false> otherwise.
	 */
	public abstract boolean canStore(Player player, Item item);
	
	/**
	 * Attempts to utilise any extra functionality when this item
	 * is stored to the container.
	 * @param player the player to utilise extra functionality for.
	 */
	public abstract void onStore(Player player);
	
	/**
	 * Any functionality to check for if this item can be withdrawed.
	 * @return <true> if the item could be withdrawed, <false> otherwise.
	 */
	public abstract boolean canWithdraw(Player player, Item item);
	
	/**
	 * Attempts to utilise any extra functionality when this item
	 * is withdrawed from the container.
	 * @param player the player to utilise extra functionality for.
	 */
	public abstract void onWithdraw(Player player);
	
	/**
	 * Attempts to store the item with the specified {@code amount} in the container.
	 * @param player the player we're storing this item for.
	 * @param slot   the slot we're storing from.
	 */
	public final void store(Player player, Item item, int slot) {
		if(player == null || item == null) {
			return;
		}
		if(!Item.valid(item) || !player.getInventory().contains(item.getId())) {
			return;
		}
		if(!canStore(player, item)) {
			return;
		}
		if(item.getValue().getValue() > 5_000_000) {
			player.message("This item is too valuable to trust to this familiar.");
			return;
		}
		if(!item.getDefinition().isTradable()) {
			player.message("You cannot store untradable items in your familiar.");
			return;
		}
		if(RESTRICTED_ITEMS.contains(item)) {
			player.message("You cannot store this item inside a familiar.");
			return;
		}
		if(item.getDefinition().isNoted()) {
			player.message("You cannot store noted items in your beast of burden.");
			return;
		}
		if(this.getContainer().remaining() < 1) {
			player.message("Your familiar's inventory is currently full.");
			return;
		}
		
		if(item.getAmount() > player.getInventory().computeAmountForId(item.getId()) && !item.getDefinition().isStackable()) {
			item.setAmount(player.getInventory().computeAmountForId(item.getId()));
		} else if(item.getAmount() > player.getInventory().get(slot).getAmount() && item.getDefinition().isStackable()) {
			item.setAmount(player.getInventory().get(slot).getAmount());
		}
		
		int remain = this.getContainer().remaining();
		if(item.getAmount() > remain) {
			item.setAmount(remain);
		}
		
		/** if the item can be added to the container, remove the item from the inventory. */
		if(this.getContainer().add(item)) {
			this.getContainer().shift();
			player.getInventory().remove(item);
			this.onStore(player);
			this.getContainer().refresh(player, 2702);
			player.getMessages().sendItemsOnInterface(5064, player.getInventory().toArray());
		}
	}
	
	/**
	 * Attempts to store the item with the specified {@code amount} in the container.
	 * @param player the player we're withdrawing this item for.
	 * @param item   the item we are withdrawing.
	 */
	public final void withdraw(Player player, Item item) {
		if(player == null || item == null)
			return;
		if(!Item.valid(item) || !this.getContainer().contains(item.getId()))
			return;
		if(!this.canWithdraw(player, item))
			return;
		ItemDefinition def = item.getDefinition();
		if(player.getInventory().remaining() < 1 && !def.isStackable()) {
			player.message("Your inventory is currently full.");
			return;
		}
		int amount = container.computeAmountForId(item.getId());
		if(item.getAmount() > amount) {
			item.setAmount(amount);
		}
		/* if the item is added, remove it from the container. */
		if(player.getInventory().add(item)) {
			this.getContainer().remove(item);
			this.getContainer().shift();
			this.onWithdraw(player);
			this.getContainer().refresh(player, 2702);
			player.getMessages().sendItemsOnInterface(5064, player.getInventory().toArray());
		}
	}
	
	/**
	 * Attempts to drop all the items in the container.
	 * @param position the position on which we are dropping the items.
	 */
	public final void dropAll(Position position) {
		Region region = World.getRegions().getRegion(position);
		if(region != null) {
			container.forEach(item -> {
				ItemNodeStatic ground = new ItemNodeStatic(item, position);
				region.register(ground);
			});
		}
		container.clear();
	}
	
	@Override
	public boolean isHoldableContainer() {
		return true;
	}
	
	/**
	 * @return {@link #container}.
	 */
	public final ItemContainer getContainer() {
		return container;
	}
	
	public void setContainer(ItemContainer container) {
		this.container = container;
	}
	
}
