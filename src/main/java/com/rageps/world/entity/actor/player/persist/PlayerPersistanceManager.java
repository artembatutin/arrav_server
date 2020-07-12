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

public final class PlayerPersistanceManager {

	private static final Logger LOGGER = LogManager.getLogger();

	private static final PlayerPersistable PERSISTABLE =  World.get().getEnvironment().isSqlEnabled() ? new PlayerPersistDB() : new PlayerPersistFile();

	public static void save(Player player) {
		//if (player.isBot) {
		//	return;
		//}
		PERSISTABLE.save(player);
	}

	public static LoginCode load(Player player) {
		//if (player.isBot) {
		//	return LoginCode.COULD_NOT_COMPLETE_LOGIN;
		//}
		return PERSISTABLE.load(player);
	}
	
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

	public static final String[] PROPERTY_NAMES = Stream.of(PROPERTIES).map(PlayerPersistanceProperty::getName).toArray(String[]::new);
}
