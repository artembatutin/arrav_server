package net.edge.net.database.pool;

import com.google.common.util.concurrent.ListenableFuture;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * An interface for asynchronously pooling database {@link Connection}s.
 * @author Whis
 */
public interface ConnectionPool<C extends Connection> {
	/**
	 * Attempts to obtain a {@link Connection} from the pool.
	 */
	ListenableFuture<C> obtainConnection() throws Exception;
	
	/**
	 * Terminates this pool.
	 */
	ListenableFuture<?> terminate() throws Exception;
	
	/**
	 * Gets the main connection.
	 * @return database connection.
	 */
	Connection getConnection() throws SQLException;
}
