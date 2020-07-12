package com.rageps.world.entity.actor.player.persist.property;

import com.google.gson.JsonElement;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.persist.property.PersistancePropertyType;

public abstract class PlayerPersistanceProperty {

	public final String name;

	final PersistancePropertyType type;

	public PlayerPersistanceProperty(String name, PersistancePropertyType type) {
		this.name = name;
		this.type = type;
	}

	public PersistancePropertyType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public abstract void read(Player player, JsonElement property);

	public abstract Object write(Player player);

}
