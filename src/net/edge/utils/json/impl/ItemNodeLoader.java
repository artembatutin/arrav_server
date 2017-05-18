package net.edge.utils.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.utils.json.JsonLoader;
import net.edge.world.World;
import net.edge.world.locale.Position;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemNodeStatic;
import net.edge.world.node.item.ItemPolicy;
import net.edge.world.node.region.Region;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all item nodes.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemNodeLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link ItemNodeLoader}.
	 */
	public ItemNodeLoader() {
		super("./data/json/items/item_nodes.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int id = reader.get("id").getAsInt();
		int amount = reader.get("amount").getAsInt();
		Position position = Objects.requireNonNull(builder.fromJson(reader.get("position"), Position.class));
		Region region = World.getRegions().getRegion(position);
		if(region != null) {
			region.register(new ItemNodeStatic(new Item(id, amount), position, ItemPolicy.RESPAWN));
		}
	}
}
