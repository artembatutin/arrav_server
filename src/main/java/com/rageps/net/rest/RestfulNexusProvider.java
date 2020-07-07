package com.rageps.net.rest;


import com.rageps.GameConstants;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Ryley Kimmel on 12/3/2017.
 */
public final class RestfulNexusProvider {

	private final String url;
	private final String apiKey;

	public RestfulNexusProvider(String url, String apiKey) {
		this.url = url;
		this.apiKey = apiKey;
	}

	public static RestfulNexusProvider create() {
		Properties properties = new Properties();
		try {
			properties.load(Files.newBufferedReader(Paths.get(GameConstants.SAVE_DIRECTORY, "etc", "nexus_config.ini")));
		} catch (IOException cause) {
			throw new UncheckedIOException("Unable to load nexus_config.ini properties. Does it exist?", cause);
		}

		return new RestfulNexusProvider(properties.getProperty("url"), properties.getProperty("apiKey"));
	}

	public RestfulNexus provide() {
		return new RestfulNexus(url, apiKey);
	}

}
