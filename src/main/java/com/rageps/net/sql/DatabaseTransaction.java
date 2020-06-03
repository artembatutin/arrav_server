package com.rageps.net.sql;

import com.google.common.base.MoreObjects;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Ryley Kimmel on 10/17/2016.
 */
public abstract class DatabaseTransaction {
	private final TableRepresentation representation;

	private int attempts;

	public DatabaseTransaction(TableRepresentation representation) {
		this.representation = representation;
	}

	public abstract void execute(Connection connection) throws SQLException;

	public int getAttempts() {
		return attempts;
	}

	public void incrementAttempts() {
		attempts++;
	}

	public TableRepresentation getRepresentation() {
		return representation;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("attempts", attempts).add("representation", representation).toString();
	}
}
