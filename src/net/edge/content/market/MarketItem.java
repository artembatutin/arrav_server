package net.edge.content.market;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.net.packet.out.SendShopPrice;
import net.edge.net.packet.out.SendShopStock;
import net.edge.util.json.JsonSaver;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.ItemDefinition;

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
	 * Condition if this item is on unlimited stock as an unlimitedStock item.
	 */
	private boolean unlimitedStock;
	
	/**
	 * Condition if this item can be bought of the counter.
	 */
	private boolean searchable;
	
	/**
	 * Players viewing this item in a shop.
	 */
	private final ObjectList<Player> viewers;
	
	/**
	 * Creating the {@link MarketItem}.
	 * @param id             the id of this item.
	 * @param value          the value of this item.
	 * @param price          the price of this item.
	 * @param stock          the stock amount of this item.
	 * @param unlimitedStock the unlimitedStock condition.
	 * @param searchable     the searchable condition.
	 */
	public MarketItem(int id, int value, int price, int stock, boolean unlimitedStock, boolean searchable) {
		this.id = id;
		this.value = value;
		this.price = price;
		this.stock = stock;
		this.unlimitedStock = unlimitedStock;
		this.searchable = searchable;
		viewers = new ObjectArrayList<>();
	}
	
	/**
	 * Gets the item value
	 * @param id the id searching for.
	 * @return the value instance of the item.
	 */
	public static MarketItem get(int id) {
		if(id <= 0 || id >= VALUES.length || VALUES[id] == null)
			return VALUES[0];
		return VALUES[id];
	}
	
	/**
	 * Gets the requested item values.
	 * @param search the searching name.
	 * @return requested items based on name.
	 */
	static IntArrayList search(Player player, String search) {
		IntArrayList out = new IntArrayList();
		//searching by id.
		if(search.matches("[0-9]+") && search.length() > 0) {
			int id = Integer.parseInt(search);
			if(!valid(id)) {
				player.message("Can't find this item.");
				return out;
			}
			if(!VALUES[id].isSearchable()) {
				player.message("You can't buy this item.");
				return out;
			}
			out.add(id);
			return out;
		}
		//searching by name.
		final int max = player.getRights().isStaff() ? 500 : 30;
		int count = 0;
		boolean found = false;
		for(MarketItem m : VALUES) {
			if(count >= max) {
				player.message("Your search is too vague.");
				break;//max cap reached.
			}
			if(m == null || !valid(m.getId()))
				continue;
			if(!m.isSearchable())
				continue;
			if(m.getName().toLowerCase().contains(search.toLowerCase())) {
				out.add(m.getId());
				found = true;
				count++;
			}
		}
		if(!found)
			player.message("No items found under that name.");
		else
			player.message("Found " + count + " items under the name: " + search);
		return out;
	}
	
	void updateStock() {
		for(Player p : viewers) {
			if(p == null)
				continue;
			p.out(new SendShopStock(this));
		}
	}
	
	private void updatePrice() {
		for(Player p : viewers) {
			if(p == null)
				continue;
			p.out(new SendShopPrice(this));
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
			item_values_saver.current().addProperty("unlimited", v.isUnlimitedStock());
			item_values_saver.current().addProperty("searchable", v.isSearchable());
			item_values_saver.split();
		}
		item_values_saver.publish("./data/def/item/market_values.json");
	}
	
	
	/* setters and getters */
	
	public static boolean valid(int id) {
		if(id < 0 || id > VALUES.length)
			return false;
		MarketItem i = MarketItem.get(id);
		if(i == null)
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
	}
	
	public void decreaseStock(int amount) {
		this.stock -= amount;
		updateStock();
	}
	
	public boolean isUnlimitedStock() {
		return unlimitedStock;
	}
	
	public boolean isSearchable() {
		return searchable;
	}
	
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
	
	public ObjectList<Player> getViewers() {
		return viewers;
	}
	
	public void toggleUnlimited() {
		unlimitedStock = !unlimitedStock;
	}
}
