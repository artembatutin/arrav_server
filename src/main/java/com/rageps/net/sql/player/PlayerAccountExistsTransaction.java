package com.rageps.net.sql.player;

import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class PlayerAccountExistsTransaction extends DatabaseTransactionFuture<Boolean> {

    private final int memberId;

    private final String QUERY = "SELECT EXISTS(SELECT 1 FROM player WHERE member_id = ?)";

    public PlayerAccountExistsTransaction(int memberId) {
        super(TableRepresentation.PLAYER);
        this.memberId = memberId;
    }

    @Override
    public Boolean onExecute(Connection connection) throws SQLException {
            try(NamedPreparedStatement statement = NamedPreparedStatement.create(connection, QUERY)) {
                statement.setLong(1, memberId);
                ResultSet rset = statement.executeQuery();
                if (rset.next())
                    return rset.getBoolean(1);
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return false;
    }
}
