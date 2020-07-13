package com.rageps.world.entity.actor.player.persist;

import com.google.gson.JsonElement;
import com.rageps.net.codec.login.LoginCode;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerCredentials;
import com.rageps.world.entity.actor.player.persist.impl.PlayerPersistDB;
import com.rageps.world.entity.actor.player.persist.impl.PlayerPersistFile;
import com.rageps.world.entity.actor.player.persist.property.PlayerPersistanceProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Stream;

import static com.rageps.world.entity.actor.player.persist.property.PersistancePropertyType.STRING;

/**
 * Handles all interactions with player persistence and can also load details of players who are not online.
 *
 * If a new property is added to a players account you can just add it's handling to the #PROPERTIES array.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public final class PlayerPersistenceManager {

	/**
	 * Logging for this class.
	 */
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * The method of persistence, will use {@link PlayerPersistDB} if SQL is enabled, otherwise {@link PlayerPersistFile}.
	 */
	private static final PlayerPersistable PERSISTABLE =  World.get().getEnvironment().isSqlEnabled() ? new PlayerPersistDB() : new PlayerPersistFile();

	/**
	 * Attempts to save a player.
	 * @param player The player being saved.
	 */
	public static void save(Player player) {
		//if (player.isBot) {
		//	return;
		//}
		PERSISTABLE.save(player);
	}

	/**
	 * Attempts to load a players save.
	 * @param player The player being loaded.
	 * @return The {@link LoginCode} which will be sent to the player.
	 */
	public static LoginCode load(Player player) {
		//if (player.isBot) {
		//	return LoginCode.COULD_NOT_COMPLETE_LOGIN;
		//}
		return PERSISTABLE.load(player);
	}

	/**
	 * Loads a players account, used for when a player isn't online and needs
	 * modifications.
	 * @param name The name of the player being loaded.
	 * @return The loaded player.
	 */
	public static Player loadPlayer(String name) {
		PlayerCredentials credentials = new PlayerCredentials(name, null);

		final Player other = new Player(credentials);

		try {
			load(other);
		} catch (Exception ex) {
			LOGGER.error("Error using player loader to get another player {}", name);
			ex.printStackTrace();
			return null;
		}
		return other;
	}


	/**
	 * Represents all of the properties present in a save file, along with their handling
	 * for loading/saving.
	 */
	public static final PlayerPersistanceProperty[] PROPERTIES = {

			new PlayerPersistanceProperty("username", STRING) {
				@Override
				public void read(Player player, JsonElement property) {
					player.credentials.setUsername(property.getAsString());
				}

				@Override
				public Object write(Player player) {
					return player.credentials.username;
				}
			}
	};

	/**
	 * Cached names of all the properties in a save.
	 */
	public static final String[] PROPERTY_NAMES = Stream.of(PROPERTIES).map(PlayerPersistanceProperty::getName).toArray(String[]::new);
}
