package com.rageps.world.entity.actor.player.persist.property;

import com.google.gson.JsonElement;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.persist.impl.PlayerPersistDB;
import com.rageps.world.entity.actor.player.persist.impl.PlayerPersistFile;

/**
 * Represents a property associated with a players account.
 * And the data used for persisting them.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public abstract class PlayerPersistanceProperty {

	/**
	 * The name of the property, this is the same representation
	 * as it is saved, either column name if using {@link PlayerPersistDB} or
	 * the {@link JsonElement} name if using {@link PlayerPersistFile}.
	 */
	public final String name;

	/**
	 * The type of object this property is, so we known what is should be persisted as.
	 */
	final PersistancePropertyType type;

	/**
	 * Constructs a {@link PlayerPersistanceProperty}.
	 * @param name The name of this property.
	 * @param type The type of property this is.
	 */
	public PlayerPersistanceProperty(String name, PersistancePropertyType type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Get's the type of property this is.
	 * @return the type.
	 */
	public PersistancePropertyType getType() {
		return type;
	}

	/**
	 * Get's the name of this property.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Handles reading and setting this property.
	 * @param player The player who this property belongs to.
	 * @param property The element this is represented in.
	 */
	public abstract void read(Player player, JsonElement property);

	/**
	 * What this property represents when persisting.
	 * @param player The player who this property belongs to.
	 * @return This properties representation.
	 */
	public abstract Object write(Player player);

}
