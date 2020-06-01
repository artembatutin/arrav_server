package com.rageps.util.json.impl;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rageps.world.Direction;
import com.rageps.world.World;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.locale.Position;
import com.rageps.util.json.JsonLoader;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all npc nodes.
 * @author lare96 <http://github.com/lare96>
 */
public final class MobNodeLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link MobNodeLoader}.
	 */
	public MobNodeLoader() {
		super("./data/def/mob/mob_nodes.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int id = reader.get("id").getAsInt();
		Position position = Objects.requireNonNull(builder.fromJson(reader.get("position").getAsJsonObject(), Position.class));
		Direction dir = reader.get("face") != null ? builder.fromJson(reader.get("face"), Direction.class) : Direction.NONE;
		boolean coordinate = reader.get("random-walk").getAsBoolean();
		int radius = 0;
		if(reader.has("walk-radius")) {
			radius = reader.get("walk-radius").getAsInt();
		}
		Preconditions.checkState(!(coordinate && radius == 0));
		Preconditions.checkState(!(!coordinate && radius > 0));
		Preconditions.checkState(!(dir != Direction.NONE && radius > 0));
		Mob mob = Mob.getNpc(id, position);
		mob.setOriginalRandomWalk(coordinate);
		mob.getMovementCoordinator().setCoordinate(coordinate);
		mob.getMovementCoordinator().setRadius(radius);
		if(dir != Direction.NONE) {
			mob.getMovementCoordinator().setFacingDirection(dir);
		}
		mob.setRespawn(true);
		if(!World.get().getMobs().add(mob))
			throw new IllegalStateException(mob.toString() + " could not be added to the world!");
	}
	
}
