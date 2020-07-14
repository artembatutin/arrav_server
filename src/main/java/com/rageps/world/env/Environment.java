package com.rageps.world.env;

/**
 * Created by Ryley Kimmel on 12/8/2016.
 */
public final class Environment {

	public enum Type {
		LIVE("live"),
		LOCAL("local"),
		TEST("test");

		private final String gameSaveLocation;

		Type(String gameSaveLocation) {
			this.gameSaveLocation = gameSaveLocation;
		}

		public String getGameSaveLocation() {
			return gameSaveLocation;
		}
	}

	private final String name;

	private final int port;

	private final boolean sqlEnabled;

	private final int version;

	private final Type type;

	private final boolean debug;

	public Environment(String name, int port, boolean sqlEnabled, boolean debug, int version, Type type) {
		this.name = name;
		this.port = port;
		this.sqlEnabled = sqlEnabled;
		this.debug = debug;
		this.version = version;
		this.type = type;
	}

	public boolean isDebug() {
		return debug;
	}

	public String getName() {
		return name;
	}

	public int getPort() {
		return port;
	}

	public boolean isSqlEnabled() {
		return sqlEnabled;
	}

	public int getVersion() {
		return version;
	}

	public Type getType() {
		return type;
	}

}
