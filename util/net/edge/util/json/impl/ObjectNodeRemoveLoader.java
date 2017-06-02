package net.edge.util.json.impl;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.util.json.JsonLoader;
import net.edge.world.World;
import net.edge.locale.Position;
import net.edge.world.object.ObjectNode;
import net.edge.world.object.ObjectType;
import net.edge.world.region.Region;

import java.util.Objects;
import java.util.Set;

/**
 * The {@link JsonLoader} implementation that loads the removal of object nodes.
 * @author lare96 <http://github.com/lare96>
 */
public final class ObjectNodeRemoveLoader extends JsonLoader {
	
	/**
	 * Create a new {@link ObjectNodeRemoveLoader}.
	 */
	public ObjectNodeRemoveLoader() {
		super("./data/json/objects/object_remove.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		//TODO REMOVE THIS
		Position position = Objects.requireNonNull(builder.fromJson(reader.get("position"), Position.class));
	}
}
