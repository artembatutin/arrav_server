package net.edge.utils.json.impl;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.utils.json.JsonLoader;
import net.edge.world.World;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.object.ObjectNode;
import net.edge.world.model.node.object.ObjectType;
import net.edge.world.model.node.region.Region;

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
		Position position = Objects.requireNonNull(builder.fromJson(reader.get("position"), Position.class));
		ObjectType type = null;
		if(reader.has("type")) {
			type = Objects.requireNonNull(builder.fromJson(reader.get("type"), ObjectType.class));
		}
		Region reg = World.getRegions().getRegion(position);
		final ObjectType typef = type;
		reg.getObjects(position).forEach(o -> {
			if(typef == null) {
				reg.addDeletedObj(o);
			} else if(typef == o.getObjectType()) {
				reg.addDeletedObj(o);
			}
		});
		
		//Deleting all.
		Set<ObjectNode> objects = ImmutableSet.copyOf(reg.getObjects(position));
		for(ObjectNode o : objects) {
			if(typef == null) {
				World.getTraversalMap().markObject(o, false, true);
			} else if(typef == o.getObjectType()) {
				World.getTraversalMap().markObject(o, false, true);
			}
		}
	}
}
