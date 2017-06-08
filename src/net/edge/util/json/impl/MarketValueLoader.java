package net.edge.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.util.json.JsonLoader;
import net.edge.content.market.MarketItem;
import net.edge.world.node.item.ItemDefinition;

/**
 * The {@link JsonLoader} implementation that loads all market values.
 * @author Artem Batutin<artembatutin@gmail.com>
 */
public final class MarketValueLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link MarketValueLoader}.
	 */
	public MarketValueLoader() {
		super("./data/json/items/market_values.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		String name = reader.get("name").getAsString();
		int index = reader.get("id").getAsInt();
		int value = reader.get("value").getAsInt();
		int price = reader.get("price").getAsInt();
		int stock = reader.get("stock").getAsInt();
		int demand = reader.get("demand").getAsInt();
		boolean variable = reader.get("variable").getAsBoolean();
		boolean provided = reader.get("unlimited").getAsBoolean();
		boolean searchable = reader.get("searchable").getAsBoolean();
		ItemDefinition def = ItemDefinition.get(index);
		if(def == null) {
			searchable = false;
			stock = 0;
		}
		if(def != null && def.isNoted()) {
			searchable = false;
		}
		MarketItem.VALUES[index] = new MarketItem(index, value, price, stock, demand, variable, provided, searchable);
	}
}