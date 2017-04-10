package net.edge.utils.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.utils.json.JsonLoader;
import net.edge.world.model.node.object.ObjectDirection;
import net.edge.world.model.node.object.ObjectNode;
import net.edge.world.World;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.object.ObjectType;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all object nodes.
 * @author lare96 <http://github.com/lare96>
 */
public final class ObjectNodeLoader extends JsonLoader {
	
	/**
	 * Create a new {@link ObjectNodeLoader}.
	 */
	public ObjectNodeLoader() {
		super("./data/json/objects/object_nodes.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int id = reader.get("id").getAsInt();
		Position position = Objects.requireNonNull(builder.fromJson(reader.get("position"), Position.class));
		ObjectDirection face = Objects.requireNonNull(ObjectDirection.valueOf(reader.get("direction").getAsString()));
		ObjectType type = Objects.requireNonNull(ObjectType.valueOf(reader.get("type").getAsString()));
		if(!World.getRegions().getRegion(position).getRegisteredObjects().contains(new ObjectNode(id, position, face, type)))
			World.getRegions().getRegion(position).register(new ObjectNode(id, position, face, type));
	}
}