package com.rageps.content.market;

import com.rageps.content.dialogue.Expression;
import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.content.market.currency.Currency;
import com.rageps.content.market.currency.impl.ItemCurrency;
import com.rageps.content.minigame.rfd.RFDData;
import com.rageps.net.packet.out.SendContainer;
import com.rageps.net.packet.out.SendForceTab;
import com.rageps.net.packet.out.SendShop;
import com.rageps.world.World;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import com.rageps.GameConstants;
import com.rageps.content.TabInterface;
import com.rageps.content.item.Skillcape;
import com.rageps.util.TextUtils;
import com.rageps.util.log.Log;
import com.rageps.util.log.impl.ShopLog;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;

import java.util.Arrays;

/**
 * Represents a single market shop.
 * @author Artem Batutin
 */
public class MarketShop {
	
	/**
	 * Id of this shop.
	 */
	private final int id;
	
	/**
	 * The title of the shop.
	 */
	private final String title;
	
	/**
	 * The currency of this shop.
	 */
	private final Currency currency;
	
	/**
	 * Flag if the iron man access this shop.
	 */
	protected final boolean ironAccess;
	
	/**
	 * The result of our search.
	 */
	private IntArrayList items;
	/**
	 * Creates a {@link MarketShop} out of saved shops.
	 * @param title the tile of this shop.
	 * @param items items in this shop.
	 */
	public MarketShop(int id, String title, Currency currency, boolean ironAccess, int... items) {
		this.id = id;
		this.currency = currency;
		this.title = title;
		this.ironAccess = ironAccess;
		this.items = new IntArrayList(items);
	}
	
	/**
	 * Creates a {@link MarketShop} based on a search.
	 * @param player the player making the search.
	 * @param search the search input.
	 */
	public MarketShop(Player player, String search) {
		id = -1;//none
		clearFromShop(player);
		search = search.replace("_", " ");
		this.currency = Currency.COINS;
		this.title = "Items found for: " + search;
		this.ironAccess = false;
		items = MarketItem.search(player, search);
		openShop(player);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(!(obj instanceof MarketShop))
			return false;
		MarketShop other = (MarketShop) obj;
		if(getTitle() == null) {
			if(other.getTitle() != null)
				return false;
		} else if(!getTitle().equals(other.getTitle()))
			return false;
		return true;
	}

	public static final int SHOP_INTERFACE_ID = 3824;
	public static final int SHOP_CONTAINER_ID = 3900;
	public static final int SHOP_NAME_ID = 3901;

	private static final int INVENTORY_INTERFACE_ID = 3822;
	public static final int INVENTORY_CONTAINER_ID = 3823;

	/**
	 * Opens the shop.
	 */
	public void openShop(Player player) {
		if(!ironAccess) {
			if(player.isIronMan() && !player.isIronMaxed()) {
				player.dialogue(new NpcDialogue(3705, "You're an iron man and you haven't maxed your skills yet.", "You can only open the iron man shop located in the iron", "man building on the second floor."));
				return;
			}
		}
		clearFromShop(player);
		player.setMarketShop(this);
		player.interfaceText(259, getCurrency().ordinal() + "");
		int x = player.getPosition().getX();
		boolean counter = x == 3079 || x == 3080;
		player.getInterfaceManager().openInventory(SHOP_INTERFACE_ID, INVENTORY_INTERFACE_ID);
		player.send(new Shop(SHOP_CONTAINER_ID, getItems()));
		player.interfaceText(SHOP_NAME_ID, getTitle());
		player.send(new ForceTabPacket(TabInterface.INVENTORY));
		player.send(new ItemsOnInterfacePacket(INVENTORY_CONTAINER_ID, player.getInventory()));
		if(player.getMarketShop().getItems() != null) {
			for(int id : player.getMarketShop().getItems()) {
				MarketItem item = MarketItem.get(id);
				if(item != null) {
					item.getViewers().add(player);
				}
			}
		}
	}
	
	/**
	 * Sends the determined selling value of {@code item} to {@code player}.
	 * @param player the player to send the value to.
	 * @param item the item to send the value of.
	 */
	public void sendSellingPrice(Player player, Item item) {
		String itemName = item.getDefinition().getName();
		if(Arrays.stream(GameConstants.INVALID_SHOP_ITEMS).anyMatch(i -> i == item.getId())) {
			player.message("You can't sell " + itemName + " " + "to the store.");
			return;
		}
		if(!canSell(player, item.getId())) {
			player.message("You can't sell " + item.getDefinition().getName() + " here.");
			return;
		}
		String formatPrice = TextUtils.formatPrice((int) Math.floor(determinePrice(player, item) / 2));
		player.message(itemName + ": shop will buy for " + formatPrice + " " + getCurrency() + ".");
	}
	
	/**
	 * Sends the determined purchase value of {@code item} to {@code player}.
	 * @param player the player to send the value to.
	 * @param item the item to send the value of.
	 */
	public void sendPurchasePrice(Player player, Item item) {
		MarketItem shopItem = MarketItem.get(item.getId());
		if(shopItem == null)
			return;
		/*if(player.getRights() == Rights.ADMINISTRATOR) {
			player.getDialogueBuilder().append(new OptionDialogue(t -> {
				if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
					player.send(new EnterAmount(shopItem.getName() + ": set price to:", s -> () -> shopItem.setPrice(Integer.parseInt(s))));
				} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
					player.send(new EnterAmount(shopItem.getName() + ": set stock to?", s -> () -> shopItem.setStock(Integer.parseInt(s))));
				} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
					shopItem.toggleUnlimited();
					openShop(player);
				} else if(t.equals(OptionDialogue.OptionType.FOURTH_OPTION)) {
					if(player.getMarketShop() != null && player.getMarketShop().getId() != -1) {
						int shopId = player.getMarketShop().getId();
						MarketShop shop = MarketCounter.getShops().get(shopId);
						shop.getItems().rem(item.getId());
						openShop(player);
					}
				}
			}, "Change price", "Change stock", "toggle: " + (shopItem.isUnlimitedStock() ? "unlimited stock" : "variable stock"), "Remove from shop"));
			return;
		}*/
		if(shopItem.getStock() <= 0 && !shopItem.isUnlimitedStock()) {
			player.message("There is none of this item left in stock!");
			return;
		}
		player.message(item.getDefinition().getName() + ": " + "shop will sell for " + TextUtils.formatPrice(determinePrice(player, item)) + " " + getCurrency() + ".");
	}
	
	/**
	 * The method that allows {@code player} to purchase {@code item}.
	 * @param player the player who will purchase this item.
	 * @param item the item that will be purchased.
	 * @return {@code true} if the player purchased the item, {@code false}
	 * otherwise.
	 */
	public boolean purchase(Player player, Item item) {
		MarketItem marketItem = MarketItem.get(item.getId());
		if(!RFDData.canBuy(player, marketItem)) {
			player.getDialogueBuilder().append(new NpcDialogue(3400, Expression.MAD, "Are you trying to fool me? You haven't", "completed the wave to buy these pair of gloves."));
			return false;
		}
		if(Skillcape.buy(player, item)) {
			return false;
		}
		if(item.getAmount() == 0) { // indicates the player is getting the price.
			this.sendPurchasePrice(player, item);
			return true;
		}
		if(marketItem.getStock() <= 0 && !marketItem.isUnlimitedStock()) {
			player.message("There is none of this item left in stock!");
			return false;
		}
		int value = item.getValue().getPrice();
		long price = ( (long) value * (long )item.getAmount());
		if(price > Integer.MAX_VALUE || !(getCurrency().getCurrency().currencyAmount(player) >= (value * item.getAmount()))) {
			player.message("You do not have enough " + getCurrency() + " to buy " + item.getAmount() + " of these.");
			return false;
		}
		//buy out all the stock left.
		if(!marketItem.isUnlimitedStock() && item.getAmount() > marketItem.getStock()) {
			item.setAmount(marketItem.getStock());
		}
		boolean tangible = this.getCurrency().getCurrency().tangible();
		int currencyId = tangible ? ((ItemCurrency) this.getCurrency().getCurrency()).getId() : -1;
		World.getLoggingManager().write(Log.create(new ShopLog(player, null, item)));
		int spacesFill = player.getInventory().slotCount(false, false, item);
		int spacesEmpty = tangible ? player.getInventory().slotCount(false, true, new Item(currencyId, item.getAmount() * value)) : 0;
		boolean hasSpace = player.getInventory().remaining() - spacesEmpty >= spacesFill;
		
		if(!hasSpace) {
			item.setAmount(player.getInventory().remaining());
			if(item.getAmount() == 0) {
				player.message("You do not have enough space in your inventory to buy this item!");
				return false;
			}
		}
		getCurrency().getCurrency().takeCurrency(player, item.getAmount() * value);
		player.getInventory().add(item);
		player.send(new ItemsOnInterfacePacket(INVENTORY_CONTAINER_ID, player.getInventory()));
		if(!marketItem.isUnlimitedStock()) {
			marketItem.setStock(marketItem.getStock() - item.getAmount());
		}
		return true;
	}
	
	/**
	 * The method that allows {@code player} to sell {@code item}.
	 * @param player the player who will sell this item.
	 * @param item the item that will be sold.
	 * @return {@code true} if the player sold the item, {@code false}
	 * otherwise.
	 */
	public boolean sell(Player player, Item item, int fromSlot) {
		if(!Item.valid(item)) {
			return false;
		}
		if(player.getInventory().get(fromSlot) == null) {
			return false;
		}
		if(Arrays.stream(GameConstants.INVALID_SHOP_ITEMS).anyMatch(i -> i == item.getId())) {
			player.message("You can't sell " + item.getDefinition().getName() + " here.");
			return false;
		}
		if(!player.getInventory().contains(item.getId())) {
			return false;
		}
		if(!item.getDefinition().isTradable()) {
			player.message("This item cannot be bought by the store owner.");
			return false;
		}
		if(player.getInventory().remaining() == 0 && !getCurrency().getCurrency().canRecieveCurrency(player)) {
			player.message("You do not have enough space in your inventory to sell this item!");
			return false;
		}
		if(!canSell(player, item.getId())) {
			player.message("You can't sell " + item.getDefinition().getName() + " here.");
			return false;
		}
		ItemDefinition def = ItemDefinition.get(item.getId());
		if(def == null)
			return false;
		int amount = player.getInventory().computeAmountForId(item.getId());
		World.getLoggingManager().write(Log.create(new ShopLog(player, item, null)));
		if(item.getAmount() > amount && !item.getDefinition().isStackable()) {
			item.setAmount(amount);
		} else if(item.getAmount() > player.getInventory().get(fromSlot).getAmount() && item.getDefinition().isStackable()) {
			item.setAmount(player.getInventory().get(fromSlot).getAmount());
		}
		int slots = def.isStackable() ? 1 : amount;
		int removed = player.getInventory().remove(item, fromSlot);
		if(removed == slots) {
			getCurrency().getCurrency().recieveCurrency(player, item.getAmount() * ((int) Math.floor(determinePrice(player, item) / 2)));
			MarketItem marketItem = MarketItem.get(item.getId());
			if(!marketItem.isUnlimitedStock()) {
				marketItem.increaseStock(item.getAmount());
				marketItem.updateStock();
			}
		}
		player.send(new ItemsOnInterfacePacket(INVENTORY_CONTAINER_ID, player.getInventory()));
		return true;
	}
	
	public static void clearFromShop(Player player) {
		if(player.getMarketShop() != null) {
			if(player.getMarketShop().getItems() != null) {
				for(int id : player.getMarketShop().getItems()) {
					MarketItem item = MarketItem.get(id);
					if(item != null) {
						item.getViewers().remove(player);
					}
				}
			}
			player.setMarketShop(null);
		}
	}
	
	public boolean canSell(Player player, int item) {
		if(getCurrency() != Currency.COINS)
			return false;
		ItemDefinition def = ItemDefinition.get(item);
		if(def == null)
			return false;
		if(def.isLended())
			return false;
		if(def.isNoted())
			item = def.getNoted();
		MarketItem marketItem = MarketItem.get(item);
		return marketItem != null && marketItem.isSearchable();
	}
	
	/**
	 * Determines the price of {@code item} based on the currency.
	 * @param player the player to check for the flag if price is calculated for the merchant.
	 * @param item the item to determine the price of.
	 * @return the price of the item based on the currency.
	 */
	private int determinePrice(Player player, Item item) {
		return item.getValue().getPrice();
	}
	
	public IntArrayList getItems() {
		return items;
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Currency getCurrency() {
		return currency;
	}
}
