package net.edge.world.content.market;

import net.edge.utils.json.JsonSaver;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.ItemDefinition;

import java.util.HashSet;
import java.util.Set;

/**
 * The container that represents an item definition.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class MarketItem {
	
	/**
	 * The array that contains all of the item values.
	 */
	public static final MarketItem[] VALUES = new MarketItem[22322];
	
	/**
	 * The identification of this item.
	 */
	private final int id;
	
	/**
	 * The value of this item.
	 */
	private final int value;
	
	/**
	 * The price of this item.
	 */
	private int price;
	
	/**
	 * The stock of this item.
	 */
	private int stock;
	
	/**
	 * The demand of this item.
	 */
	private int demand;
	
	/**
	 * Condition if this item's price gets changed with demand.
	 */
	private boolean variable;
	
	/**
	 * Condition if this item is on unlimited stock as an unlimitedStock item.
	 */
	private boolean unlimitedStock;
	
	/**
	 * Condition if this item can be bought of the counter.
	 */
	private final boolean isSearchable;
	
	/**
	 * Players viewing this item in a shop.
	 */
	private final Set<Player> viewers;
	
	/**
	 * Creating the {@link MarketItem}.
	 * @param id             the id of this item.
	 * @param value          the value of this item.
	 * @param price          the price of this item.
	 * @param stock          the stock amount of this item.
	 * @param demand         the demand of this item.
	 * @param variable       the variable condition.
	 * @param unlimitedStock the unlimitedStock condition.
	 * @param isSearchable   the isSearchable condition.
	 */
	public MarketItem(int id, int value, int price, int stock, int demand, boolean variable, boolean unlimitedStock, boolean isSearchable) {
		this.id = id;
		this.value = value;
		this.price = price;
		this.stock = stock;
		this.demand = demand;
		this.variable = variable;
		this.unlimitedStock = unlimitedStock;
		this.isSearchable = isSearchable;
		viewers = new HashSet<>();
	}
	
	/**
	 * Gets the item value
	 * @param id the id searching for.
	 * @return the value instance of the item.
	 */
	public static MarketItem get(int id) {
		return VALUES[id];
	}
	
	/**
	 * Gets the requested item values.
	 * @param search the searching name.
	 * @return requested items based on name.
	 */
	public static int[] search(Player player, String search) {
		//searching by id.
		if(search.matches("[0-9]+") && search.length() > 0) {
			int[] item = new int[1];
			int id = Integer.parseInt(search);
			if(!valid(id)) {
				player.message("Can't find this item.");
				return item;
			}
			if(!VALUES[id].isSearchable()) {
				player.message("You can't buy this item.");
				return item;
			}
			item[0] = id;
			return item;
		}
		//searching by name.
		final int max = player.getRights().isStaff() ? 500 : 30;
		int count = 0;
		boolean found = false;
		int[] items = new int[max];
		for(MarketItem v : VALUES) {
			if(v == null || !valid(v.getId()))
				continue;
			if(!v.isSearchable())
				continue;
			if(v.getName().toLowerCase().contains(search.toLowerCase())) {
				items[count] = v.getId();
				count++;
				found = true;
				if(count == max) {
					player.message("Your search is too vague.");
					break;//max cap reached.
				}
			}
		}
		if(!found)
			player.message("No items found under that name.");
		int[] res = new int[count];
		System.arraycopy(items, 0, res, 0, count);
		return res;
	}
	
	public void updateStock() {
		for(Player p : viewers) {
			if(p == null)
				continue;
			p.getMessages().sendShopItemStock(this);
		}
	}
	
	public void updatePrice() {
		for(Player p : viewers) {
			if(p == null)
				continue;
			p.getMessages().sendShopItemPrice(this);
		}
	}
	
	/**
	 * Serializes the market items.
	 */
	public static void serializeMarketItems() {
		JsonSaver item_values_saver = new JsonSaver();
		
		for(MarketItem v : VALUES) {
			if(v == null) {
				continue;
			}
			
			item_values_saver.current().addProperty("id", v.getId());
			item_values_saver.current().addProperty("name", v.getName());
			item_values_saver.current().addProperty("stock", v.getStock());
			item_values_saver.current().addProperty("value", v.getValue());
			item_values_saver.current().addProperty("price", v.getPrice());
			item_values_saver.current().addProperty("demand", v.getDemand());
			item_values_saver.current().addProperty("variable", v.isVariable());
			item_values_saver.current().addProperty("unlimited", v.isUnlimitedStock());
			item_values_saver.current().addProperty("searchable", v.isSearchable());
			item_values_saver.split();
		}
		
		item_values_saver.publish("./data/json/items/market_values.json");
	}
	
	
	/* setters and getters */
	
	public static boolean valid(int id) {
		if(id < 0 || id > VALUES.length)
			return false;
		MarketItem i = MarketItem.get(id);
		if(i == null)
			return false;
		if(!i.isSearchable())
			return false;
		String name = i.getName();
		if(name == null)
			return false;
		if(name.length() == 0)
			return false;
		if(name.equals("null"))
			return false;
		return true;
	}
	
	public String getName() {
		ItemDefinition def = ItemDefinition.get(getId());
		return def == null ? "" : def.getName();
	}
	
	public int getId() {
		return id;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
		updatePrice();
	}
	
	public void increasePrice(double amount) {
		this.price += price * amount;
		updatePrice();
	}
	
	public void decreasePrice(double amount) {
		this.price -= price * amount;
		updatePrice();
	}
	
	public int getStock() {
		return stock;
	}
	
	public void setStock(int stock) {
		this.stock = stock;
		updateStock();
	}
	
	public void increaseStock(int amount) {
		this.stock += amount;
		updateStock();
		//sold
		demand -= amount;
		if(demand <= -100) {
			demand += 100;
			decreasePrice(0.01);
		}
	}
	
	public void decreaseStock(int amount) {
		this.stock -= amount;
		updateStock();
		//bought
		demand += amount;
		if(demand >= 100) {
			demand -= 100;
			increasePrice(0.01);
		}
	}
	
	public int getDemand() {
		return demand;
	}
	
	public void setDemand(int demand) {
		this.demand = demand;
	}
	
	public void increaseDemand(int amount) {
		this.demand += amount;
	}
	
	public void decreaseDemand(int amount) {
		this.demand -= amount;
	}
	
	public boolean isVariable() {
		return variable;
	}
	
	public boolean isUnlimitedStock() {
		return unlimitedStock;
	}
	
	public boolean isSearchable() {
		return isSearchable;
	}
	
	public Set<Player> getViewers() {
		return viewers;
	}
	
	public void toggleVariable() {
		variable = !variable;
	}
	
	public void toggleUnlimited() {
		unlimitedStock = !unlimitedStock;
	}
}
