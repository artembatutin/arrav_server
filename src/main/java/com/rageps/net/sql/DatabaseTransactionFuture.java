package com.rageps.net.sql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Jason M on 2017-03-13 at 3:32 PM
 */
public abstract class DatabaseTransactionFuture<T> extends DatabaseTransaction {

	private T result;

	private boolean finished;

	public DatabaseTransactionFuture(TableRepresentation representation) {
		super(representation);
	}

	@Override
	public final void execute(Connection connection) throws SQLException {
		result = onExecute(connection);
		finished = true;
	}

	public abstract T onExecute(Connection connection) throws SQLException;

	public boolean isFinished() {
		return finished;
	}

	public T getResult() {
		return result;
	}
}
