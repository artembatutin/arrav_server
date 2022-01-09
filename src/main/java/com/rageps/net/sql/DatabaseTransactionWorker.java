package com.rageps.net.sql;


import com.rageps.net.sql.logging.ChatLog;
import com.rageps.net.sql.logging.ClanChatLog;
import com.rageps.net.sql.logging.PrivateMessageLog;
import com.rageps.world.World;
import com.rageps.world.env.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Ryley Kimmel on 10/17/2016.
 */
public final class DatabaseTransactionWorker implements Runnable {

	private static final Logger logger = LogManager.getLogger(DatabaseTransactionWorker.class);

	private final BlockingDeque<DatabaseTransaction> transactions = new LinkedBlockingDeque<>();


	public DatabaseTransactionWorker() {
		for (TableRepresentation value : TableRepresentation.values()) {
			try {
				value.getWrapper().open().close();
			} catch (Exception e) {
				logger.fatal("ERROR connecting to database!! db: {}", value.name(), e);
				System.exit(1);
			}
		}
	}

	public void submit(DatabaseTransaction transaction) {
		if (!World.get().getEnvironment().isSqlEnabled()) {
			return;
		}

		transactions.add(transaction);
	}

	@Override
	public void run() {
		for (; ; ) {
			dequeTransactions();

			try {
				Thread.sleep(10_000);
			} catch (InterruptedException ignored) {
			}
		}
	}

	/**
	 * The size of the worker. The amount of transactions that exist.
	 *
	 * @return the amount of transactions.
	 */
	public final int size() {
		return transactions.size();
	}

	/**
	 * Attempts to drain the remaining transaction in the worker until there is none left.
	 */
	public void drain() {
		dequeTransactions();
	}

	private void dequeTransactions() {
		try {
			while (!transactions.isEmpty()) {
				DatabaseTransaction transaction = null;

				try {
					transaction = transactions.take();
				} catch (Exception ignored) {
				}

				if (transaction == null) {
					return;
				}
				Connection connection = transaction.getRepresentation().getWrapper().open();

				try {
					connection.clearWarnings();
					connection.setAutoCommit(false);
					transaction.execute(connection);
					close(connection);
				} catch (Exception cause) {
					try {
						connection.rollback();
						connection.clearWarnings();
					} catch (SQLException ignored) {
						if (World.get().getEnvironment().getType() != Environment.Type.LIVE && World.get().getEnvironment().isSqlEnabled()) {
							logger.error("[TEST_ENVIRONMENT]: Could not rollback connection!", ignored);
						}
					}

					close(connection);

					if (transaction.getAttempts() <= 5) {
						transactions.add(transaction);
					} else {
						logger.error("Could not execute transaction {} after 5 attempts!", transaction, cause);
					}

					transaction.incrementAttempts();
				}
			}

			ChatLog.execute();
			PrivateMessageLog.execute();
			ClanChatLog.execute();
		} catch (Exception cause) {
			logger.error("Error occurred during transaction loop!", cause);
		}
	}

	private void close(Connection connection) {
		try {
			connection.close();
		} catch (SQLException ignored) {
			if (World.get().getEnvironment().getType() != Environment.Type.LIVE && World.get().getEnvironment().isSqlEnabled()) {
				logger.error("[TEST_ENVIRONMENT]: Could not close connection!", ignored);
			}
		}
	}

}
