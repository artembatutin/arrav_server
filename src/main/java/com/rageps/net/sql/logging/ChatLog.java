package com.rageps.net.sql.logging;

import com.google.common.collect.ImmutableSet;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;
import com.rageps.world.World;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Ryley Kimmel on 12/5/2016.
 */
public final class ChatLog {
	private static final TableRepresentation REPRESENTATION = TableRepresentation.LOGGING;

	private static final Queue<Chat> chats = new ConcurrentLinkedQueue<>();

	public static void submit(Chat chat) {
		if (!World.get().getEnvironment().isSqlEnabled()) {
			return;
		}
		chats.offer(chat);
	}

	public static void execute() throws SQLException {
		if (chats.isEmpty()) {
			return;
		}

		Set<Chat> cloned = ImmutableSet.copyOf(chats);
		chats.clear();

		Connection connection = REPRESENTATION.getWrapper().open();
		connection.clearWarnings();

		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "INSERT INTO chats (session_id, username, message, timestamp) VALUES (:session_id, :username, :message, :timestamp)")) {
			for (Chat chat : cloned) {
				statement.setLong("session_id", chat.getSessionId());
				statement.setString("username", chat.getUsername());
				statement.setString("message", chat.getMessage());
				statement.setTimestamp("timestamp", chat.getTimestamp());
				statement.addBatch();
			}

			statement.executeBatch();
			connection.commit();
			connection.close();
		}
	}


	public static final class Chat {
		private final String username;

		private final String message;

		private final long sessionId;

		private final Timestamp timestamp = Timestamp.from(Instant.now(DateTimeUtil.CLOCK));

		public Chat(String username, String message, long sessionId) {
			this.username = username;
			this.sessionId = sessionId;
			this.message = message;
		}

		public long getSessionId() {
			return sessionId;
		}

		public String getUsername() {
			return username;
		}

		public String getMessage() {
			return message;
		}

		public Timestamp getTimestamp() {
			return timestamp;
		}
	}

}
