package net.arrav.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.arrav.util.json.JsonLoader;
import net.arrav.world.entity.item.GroundItem;
import net.arrav.world.entity.item.GroundItemPolicy;
import net.arrav.world.entity.item.GroundItemStatic;
import net.arrav.world.entity.item.Item;
import net.arrav.world.locale.Position;

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
		super("./data/def/item/item_nodes.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int id = reader.get("id").getAsInt();
		int amount = reader.get("amount").getAsInt();
		Position position = Objects.requireNonNull(builder.fromJson(reader.get("position"), Position.class));
		GroundItem item = new GroundItemStatic(new Item(id, amount), position, GroundItemPolicy.RESPAWN);
		item.getRegion().ifPresent(r -> r.register(item));
	}
}
