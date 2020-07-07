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
public final class RestfulHttpConfigProvider {

	private final String host;

	private final int port;

	private final String apiKey;

	public RestfulHttpConfigProvider(String host, int port, String apiKey) {
		this.host = host;
		this.port = port;
		this.apiKey = apiKey;
	}

	public static RestfulHttpConfigProvider create() {
		Properties properties = new Properties();
		try {
			properties.load(Files.newBufferedReader(Paths.get(GameConstants.SAVE_DIRECTORY, "etc", "http_restful_config.ini")));
		} catch (IOException cause) {
			throw new UncheckedIOException("Unable to load http_restful_config.ini properties. Does it exist?", cause);
		}

		return new RestfulHttpConfigProvider(properties.getProperty("host"), Integer.valueOf(properties.getProperty("port")), properties.getProperty("apiKey"));
	}

	public RestfulHttpConfig get() {
		return new RestfulHttpConfig(host, port, apiKey);
	}

}
