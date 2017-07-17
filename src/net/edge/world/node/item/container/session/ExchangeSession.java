package net.edge.world.node.item.container.session;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.world.node.item.container.ItemContainer;
import net.edge.world.World;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.Rights;
import net.edge.world.node.item.Item;

import java.util.*;

/**
 * Holds functionality for exchange sessions.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class ExchangeSession {
	
	/**
	 * The limit of players allowed in one session.
	 */
	public static final int PLAYER_LIMIT = 2;
	
	/**
	 * The two players in this session.
	 */
	private final ObjectList<Player> players = new ObjectArrayList<>(PLAYER_LIMIT);
	
	/**
	 * The exchange session type this manager is managing.
	 */
	private final ExchangeSessionType type;
	
	/**
	 * The items which are in this exchange session.
	 */
	private final Object2ObjectArrayMap<Player, ItemContainer> exchangeSession = new Object2ObjectArrayMap<>();
	
	/**
	 * The attachment to the session stage, this will more than likely be a player object
	 * that will be attached when they have confirmed a certain stage.
	 */
	private Object attachment;
	
	/**
	 * The stage of this exchange session.
	 */
	private int stage;
	
	/**
	 * Constructs a new {@link ExchangeSession}.
	 * @param players {@link #players}.
	 * @param stage   {@link #stage}.
	 * @param type    {@link #type}.
	 */
	public ExchangeSession(List<Player> players, int stage, ExchangeSessionType type) {
		this.players.addAll(players);
		this.stage = stage;
		this.type = type;
		this.players.forEach(player -> this.exchangeSession.put(player, new ItemContainer(28, ItemContainer.StackPolicy.STANDARD)));
	}
	
	/**
	 * Checks if the item can be added to the container.
	 * @param player the player who's attempting to add an item.
	 * @param item   the item that was attempted to being added.
	 * @param slot   the slot the item is being added from.
	 * @return <true> if the item can, <false> otherwise.
	 */
	public abstract boolean canAddItem(Player player, Item item, int slot);
	
	/**
	 * Any functionality that should be dealth with when a player sends a request
	 * should be handled in here.
	 * @param player    the player who requested the exchange session.
	 * @param requested the player who was requested by the {@code player}.
	 */
	public abstract void onRequest(Player player, Player requested);
	
	/**
	 * Any functionality that should be dealth when a player clicks a button
	 * should be handled in here.
	 * @param player the player whom clicked the button.
	 * @param button the id that was clicked.
	 */
	public abstract void onClickButton(Player player, int button);
	
	/**
	 * Checks if the item can be removed from the container.
	 * @param item the item that was attempted to being removed.
	 * @return <true> if the item can, <false> otherwise.
	 */
	public abstract boolean canRemoveItem(Player player, Item item);
	
	/**
	 * Accepts the new {@code stage}.
	 * @param player the player who accepted the stage.
	 * @param stage  the possible stages.
	 */
	public abstract void accept(Player player, int stage);
	
	/**
	 * Updates the main components of the interface.
	 */
	public abstract void updateMainComponents();
	
	/**
	 * Updates the offer components of the interface.
	 */
	public abstract void updateOfferComponents();
	
	/**
	 * Any functionality that should be handled when the interface closes.
	 */
	public abstract void onReset();
	
	/**
	 * Attempts to add an item to the container.
	 * @param player the player we're adding this item for.
	 * @param slot   the inventory slot being added onto it.
	 * @param amount the amount being added.
	 * @return <true> if the item was added, <false> otherwise.
	 */
	public final boolean add(Player player, int slot, int amount) {
		Item invItem = player.getInventory().get(slot);
		if(invItem == null) {
			return false;
		}
		if(!Item.valid(invItem) || !player.getInventory().contains(invItem.getId())) {
			return false;
		}
		if(getStage() != OFFER_ITEMS) {
			return false;
		}
		if(player.getInventory().get(slot) == null) {
			return false;
		}
		if(!World.getExchangeSessionManager().inAnySession(player)) {
			return false;
		}
		if(World.getExchangeSessionManager().containsSessionInconsistancies(player)) {
			return false;
		}
		if(!canAddItem(player, invItem, slot)) {
			return false;
		}
		if(!invItem.getDefinition().isTradable() && player.getRights().less(Rights.ADMINISTRATOR)) {
			player.message("You can't offer this item.");
			return false;
		}
		
		Item item = new Item(invItem.getId(), amount);
		int count = player.getInventory().computeAmountForId(item.getId());
		
		if(item.getAmount() > count) {
			item.setAmount(count);
		}
		
		if(exchangeSession.get(player).canAdd(item) && player.getInventory().canRemove(item)) {
			exchangeSession.get(player).add(item);
			player.getInventory().remove(item, slot);
			this.attachment = null;
			updateOfferComponents();
			return true;
		}
		return false;
	}
	
	/**
	 * Attempts to remove an item from the container.
	 * @param player the player we're removing this item for.
	 * @param item   the item being removed.
	 * @return <true> if the item was removed, <false> otherwise.
	 */
	public final boolean remove(Player player, Item item) {
		if(player == null || item == null) {
			return false;
		}
		if(!Item.valid(item) || !this.exchangeSession.get(player).contains(item.getId())) {
			return false;
		}
		if(getStage() != OFFER_ITEMS) {
			return false;
		}
		if(!World.getExchangeSessionManager().inAnySession(player)) {
			return false;
		}
		if(World.getExchangeSessionManager().containsSessionInconsistancies(player)) {
			return false;
		}
		if(!canRemoveItem(player, item)) {
			return false;
		}
		ItemContainer container = this.exchangeSession.get(player);
		int amount = container.computeAmountForId(item.getId());
		if(item.getAmount() > amount) {
			item = item.createWithAmount(amount);
		}
		if(container.canRemove(item) && player.getInventory().canAdd(item)) {
			container.remove(item);
			player.getInventory().add(item);
			this.attachment = null;
			container.shift();
			updateOfferComponents();
			return true;
		}
		return false;
	}
	
	/**
	 * Finalises the exchange session, the premise for receiving items is depicted by the {@code type}.
	 * @param type the type of finalising the session.
	 */
	public final void finalize(ExchangeSessionActionType type) {
		if(stage == FINALIZE) {
			return;
		}
		stage = FINALIZE;
		
		switch(type) {
			case DISPOSE_ITEMS:
				this.getPlayers().forEach(player -> this.getExchangeSession().get(player).clear());
				break;
			case RESTORE_ITEMS:
				this.getPlayers().forEach(player -> {
					ItemContainer items = this.getExchangeSession().get(player);
					items.forEach(item -> {
						player.getInventory().add(item);
					});
					items.clear();
				});
				break;
			case HALT:
				//nothing happens with the items in the container when halted.
				break;
		}
		onReset();
		World.getExchangeSessionManager().remove(this);
	}
	
	public static final int REQUEST = 1;
	public static final int OFFER_ITEMS = 2;
	public static final int CONFIRM_DECISION = 3;
	public static final int FURTHER_INTERACTION = 4;
	public static final int FINALIZE = 5;
	
	/**
	 * The list which contains both players.
	 * @return the list which contains both players.
	 */
	public final ObjectList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Gets the other player in the session.
	 * @param player the player we're <b>NOT</b> trying to retrieve.
	 * @return the other player in the session.
	 */
	public Player getOther(Player player) {
		for(Player p : players) {
			if(player.same(p))
				continue;
			return p;
		}
		return null;
	}
	
	/**
	 * @return the type
	 */
	public final ExchangeSessionType getType() {
		return type;
	}
	
	/**
	 * @return {@link #exchangeSession}.
	 */
	public final Map<Player, ItemContainer> getExchangeSession() {
		return exchangeSession;
	}
	
	/**
	 * Assigns an attachment to this stage object
	 * @param attachment the attachment to be assigned
	 */
	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}
	
	/**
	 * Retrieves the attachment object to this class
	 * @return the attachment
	 */
	public Object getAttachment() {
		return attachment;
	}
	
	/**
	 * Determines if the trade stage has an attachment
	 */
	public boolean hasAttachment() {
		return Objects.nonNull(attachment);
	}
	
	/**
	 * Sets the current stage to the {@code stage}.
	 * @param stage the stage to set.
	 */
	public final void setStage(int stage) {
		this.stage = stage;
	}
	
	/**
	 * @return {@link #stage}.
	 */
	public final int getStage() {
		return stage;
	}
}
