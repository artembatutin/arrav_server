package com.rageps.net.rest;

public final class RestfulHttpConfig {
	private final String host;
	private final int port;
	private final String apiKey;

	public RestfulHttpConfig(String host, int port, String apiKey) {
		this.host = host;
		this.port = port;
		this.apiKey = apiKey;
	}

	public int getPort() {
		return port;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getHost() {
		return host;
	}
}
