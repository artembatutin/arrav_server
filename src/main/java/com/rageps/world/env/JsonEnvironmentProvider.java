package com.rageps.world.env;

import com.google.gson.Gson;
import com.rageps.GameConstants;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Ryley Kimmel on 12/8/2016.
 */
public final class JsonEnvironmentProvider implements EnvironmentProvider {

	private static final Gson GSON = new Gson();

	private static final Path ROOT = Paths.get(GameConstants.SAVE_DIRECTORY, "etc", "environment.json");

	public static Environment provide() {
		JsonEnvironmentProvider provider = new JsonEnvironmentProvider();
		return provider.get();
	}

	@Override
	public Environment get() {
		try {
			return GSON.fromJson(Files.newBufferedReader(ROOT), Environment.class);
		} catch (IOException cause) {
			throw new UncheckedIOException("Unable to initialize environment.json", cause);
		}
	}

}
