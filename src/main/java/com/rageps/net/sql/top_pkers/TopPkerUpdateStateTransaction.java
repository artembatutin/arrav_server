package com.rageps.net.sql.top_pkers;

import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Jason M on 2017-03-13 at 2:16 PM
 */
public class TopPkerUpdateStateTransaction extends DatabaseTransactionFuture<Integer> {

	/**
	 * The state table id that we use by default for state, unless otherwise indicated.
	 */
	private static final int DEFAULT_ID = 1;

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

	public TopPkerUpdateStateTransaction(int session, int lastSession) {
		this(DEFAULT_ID, session, lastSession);
	}

	public TopPkerUpdateStateTransaction(int id, int session, int lastSession) {
		super(TableRepresentation.TOP_PKER);
		this.id = id;
		this.session = session;
		this.lastSession = lastSession;
	}

	@Override
	public Integer onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "INSERT INTO top_pker_state (session, last_session, id) VALUES(:session, :last_session, :id) ON DUPLICATE KEY UPDATE session=VALUES(session), last_session=VALUES(last_session), id=VALUES(id);")) {
			statement.setInt("session", session);
			statement.setInt("last_session", lastSession);
			statement.setInt("id", id);
			statement.executeUpdate();
			connection.commit();
			return 1;
		}
	}
}
