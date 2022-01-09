package com.rageps.net.sql;

import java.sql.SQLException;
import java.util.Objects;

/**
 * Wraps an {@link SQLException} with the unchecked {@link RuntimeException}.
 */
public final class UncheckedSQLException extends RuntimeException {
	private static final long serialVersionUID = -3977098200405455969L;

	/**
	 * Constructs a new {@link UncheckedSQLException} with the specified cause.
	 *
	 * @param cause The SQLException, may not be {@code null}.
	 */
	public UncheckedSQLException(SQLException cause) {
		this(cause.getMessage(), cause);
	}

	/**
	 * Constructs a new {@link UncheckedSQLException} with the specified detail message and cause.
	 *
	 * @param message The detail message, may be {@code null}.
	 * @param cause The SQLException, may not be {@code null}.
	 */
	public UncheckedSQLException(String message, SQLException cause) {
		super(message, Objects.requireNonNull(cause));
	}

	@Override
	public SQLException getCause() {
		return (SQLException) super.getCause();
	}

}
