package com.rageps.world.entity.actor.player.persist.impl;

import com.google.gson.*;
import com.rageps.net.refactor.codec.login.LoginConstants;
import com.rageps.util.json.GsonUtils;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerCredentials;
import com.rageps.world.entity.actor.player.persist.PlayerLoaderResponse;
import com.rageps.world.entity.actor.player.persist.PlayerPersistable;
import com.rageps.world.entity.actor.player.persist.PlayerPersistenceManager;
import com.rageps.world.entity.actor.player.persist.property.PlayerPersistanceProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles persistence using JSON files.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public final class PlayerPersistFile implements PlayerPersistable {

	/**
	 * Logging for this class.
	 */
	public static final Logger logger = LogManager.getLogger();

	/**
	 * Path to the directory that players are saved.
	 */
	public static final Path FILE_DIR = Paths.get("data", "profile", "save");

	/**
	 * Gson parser for this class.
	 */
	public static final Gson GSON = GsonUtils.JSON_PRETTY_ALLOW_NULL;

	@Override
	public void save(Player player) {
		//if (player.isBot) {
		//	return;
		//}

		//HighscoreService.saveHighscores(player);




		new Thread(() -> {
			try {
				JsonObject properties = new JsonObject();

				for (PlayerPersistanceProperty property : PlayerPersistenceManager.PROPERTIES) {
					properties.add(property.name, GSON.toJsonTree(property.write(player)));
				}

				try {
					Files.write(FILE_DIR.resolve(player.getName() + ".json"), GSON.toJson(properties).getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (Exception ex) {
				logger.error(String.format("Error while saving player=%s", player.getName()), ex);
			}

			player.saved.set(true);
		}).start();
	}

	@Override
	public PlayerLoaderResponse load(PlayerCredentials credentials) {
		final File dir = FILE_DIR.toFile();

		if (!dir.exists()) {
			dir.mkdirs();
		}


		try {
			Path path = FILE_DIR.resolve(credentials.getUsername() + ".json");

			if (!Files.exists(path)) {
				//player.firstLogin = true; todo fix
				return new PlayerLoaderResponse(LoginConstants.STATUS_OK);
			}
			Player player = new Player(credentials);
			PlayerLoaderResponse response = new PlayerLoaderResponse(LoginConstants.STATUS_OK, player);

			try (Reader reader = new FileReader(path.toFile())) {
				JsonElement parsed = new JsonParser().parse(reader);
				if(parsed instanceof JsonNull)
					logger.error("Fatal! JsonNull loading player file");
				JsonObject jsonReader = (JsonObject) parsed;


				for (PlayerPersistanceProperty property : PlayerPersistenceManager.PROPERTIES) {
					if (jsonReader.has(property.name)) {
						if (jsonReader.get(property.name).isJsonNull())
							continue;
						property.read(player, jsonReader.get(property.name));
					}
				}
			}

			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new PlayerLoaderResponse(LoginConstants.STATUS_COULD_NOT_COMPLETE);
	}
}
