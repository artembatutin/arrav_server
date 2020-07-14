package com.rageps.net.sql;

import com.rageps.GameConstants;
import com.rageps.world.World;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Ryley Kimmel on 10/17/2016.
 */
public final class HikariDataSourceWrapper {
	private static final Logger logger = LogManager.getLogger(HikariDataSourceWrapper.class);

	private final HikariDataSource dataSource;

	public HikariDataSourceWrapper(HikariDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static HikariDataSourceWrapper create(String name, int minimumConnections) {
		//if (!World.get().getEnvironment().isSqlEnabled()) {
		//	return null;
		//}

		Properties properties = new Properties();
		try {
			properties.load(Files.newBufferedReader(Paths.get(GameConstants.SAVE_DIRECTORY, "etc", "jdbc", name + ".ini")));
		} catch (IOException cause) {
			throw new UncheckedIOException("Unable to load " + name + ".ini properties. Does it exist?", cause);
		}

		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://" + properties.getProperty("host") + "/" + properties.getProperty("database") + "?useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false");
		config.setAutoCommit(false);
		config.setRegisterMbeans(true);
		config.setPoolName(name);
		config.setMinimumIdle(minimumConnections);
		config.setConnectionTimeout(2_000);
		config.setMaximumPoolSize(512);
		config.setLeakDetectionThreshold(2_000);
		config.setUsername(properties.getProperty("username"));
		config.setPassword(properties.getProperty("password"));
		config.validate();

		try {
			return new HikariDataSourceWrapper(new HikariDataSource(config));
		}catch (Exception e) {
			logger.warn("Unable to create connection with database name={} host={} database={}", name, properties.getProperty("host"), properties.getProperty("database"));
			e.printStackTrace();
		}
		return null;
	}

	public Connection open() {
		SQLException exception = null;

		for (int attempt = 0; attempt < 5; attempt++) {
			try {
				return dataSource.getConnection();
			} catch (SQLException cause) {
				logger.error("Error occurred while fetching Hikari connection!", cause);
				exception = cause;
			}
		}

		throw new RuntimeException(exception);
	}

}
