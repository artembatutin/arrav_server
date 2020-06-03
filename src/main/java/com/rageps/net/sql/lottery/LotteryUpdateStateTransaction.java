package com.rageps.net.sql.lottery;

import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.SQLException;

public class LotteryUpdateStateTransaction extends DatabaseTransactionFuture<Integer> {

	/**
	 * The state table id that we use by default for state, unless otherwise indicated.
	 */
	private static final int DEFAULT_ID = 0;

	/**
	 * The id of the state row.
	 */
	private final int id;

	/**
	 * The new session value.
	 */
	private final int session;

	/**
	 * The last session;
	 */
	private final int lastSession;

	public LotteryUpdateStateTransaction(int session, int lastSession) {
		this(DEFAULT_ID, session, lastSession);
	}

	public LotteryUpdateStateTransaction(int id, int session, int lastSession) {
		super(TableRepresentation.LOTTERY);
		this.id = id;
		this.session = session;
		this.lastSession = lastSession;
	}

	@Override
	public Integer onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 "INSERT INTO lottery_state (session_id, last_session_id, id) VALUES(:session_id, :last_session_id, :id) ON DUPLICATE KEY UPDATE session_id=VALUES(session_id), last_session_id=VALUES(last_session_id), id=VALUES(id);")) {
			statement.setInt("session_id", session);
			statement.setInt("last_session_id", lastSession);
			statement.setInt("id", id);
			statement.executeUpdate();
			connection.commit();
			return 1;
		}
	}
}
