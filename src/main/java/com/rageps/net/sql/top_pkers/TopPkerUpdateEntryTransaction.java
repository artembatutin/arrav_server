package com.rageps.net.sql.top_pkers;

import com.google.common.hash.Hashing;
import com.rageps.content.top_pker.Entry;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.StringUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Jason M on 2017-03-14 at 11:56 PM
 */
public class TopPkerUpdateEntryTransaction extends DatabaseTransactionFuture<Integer> {

	private final int session;

	private final Entry entry;

	public TopPkerUpdateEntryTransaction(int session, Entry entry) {
		super(TableRepresentation.TOP_PKER);
		this.session = session;
		this.entry = entry;
	}

	@Override
	public Integer onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 		"INSERT IGNORE INTO top_pker_entries (session, session_hash, username, kills, deaths) VALUES (?, ?, ?, ?, ?) "
				 + "ON DUPLICATE KEY UPDATE kills=VALUES(kills), deaths=VALUES(deaths);")) {
			statement.setInt(1, session);
			statement.setLong(2, Hashing.md5().newHasher().putLong(entry.getUsernameAsLong()).putInt(session).hash().asLong());
			statement.setString(3, StringUtil.longToString(entry.getUsernameAsLong()));
			statement.setInt(4, entry.getKills());
			statement.setInt(5, entry.getDeaths());
			statement.executeUpdate();
			connection.commit();
			return 1;
		}
	}
}
