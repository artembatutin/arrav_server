package com.rageps.net.sql;

/**
 * Created by Ryley Kimmel on 10/17/2016.
 */
public enum TableRepresentation {
	LOGGING(HikariDataSourceWrapper.create("logging", 36)),
	SANCTIONS(HikariDataSourceWrapper.create("sanctions", 4)),
	LEADERBOARDS(HikariDataSourceWrapper.create("leaderboards", 4)),
	PLAYER_SHOP(HikariDataSourceWrapper.create("player_shop", 4)),
	DAILY_STATISTICS(HikariDataSourceWrapper.create("daily_statistics", 4)),
	TOP_PKER(HikariDataSourceWrapper.create("top_pkers", 1)),
	LOTTERY(HikariDataSourceWrapper.create("lottery", 1)),
	VOTE_POLL(HikariDataSourceWrapper.create("vote_poll", 1));

	private final HikariDataSourceWrapper wrapper;

	TableRepresentation(HikariDataSourceWrapper wrapper) {
		this.wrapper = wrapper;
	}

	public HikariDataSourceWrapper getWrapper() {
		return wrapper;
	}

}
