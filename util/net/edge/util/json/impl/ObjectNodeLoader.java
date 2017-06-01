package net.edge.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.util.json.JsonLoader;
import net.edge.World;
import net.edge.locale.Position;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectType;

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
		DynamicObject obj = new DynamicObject(id, position, face, type, false, 0, 0);
		if(!World.getRegions().getRegion(position).getDynamicObjects().contains(obj))
			obj.register();
	}
}