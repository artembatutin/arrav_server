package com.rageps.net.sql.forum;

import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.forum.account.ForumAccount;
import com.rageps.net.sql.forum.account.ForumCredentials;
import com.rageps.net.sql.forum.account.MultifactorAuthentication;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerCredentials;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SelectForumAccountTransaction extends DatabaseTransactionFuture<ForumAccount> {

    //
    private final String QUERY_NAME = "SELECT member_id, member_group_id, mgroup_others, email, members_pass_hash, members_pass_salt, mfa_details FROM core_members WHERE UPPER(name) = ?";
    private final String QUERY_EMAIL = "SELECT member_id, member_group_id, mgroup_others, email, members_pass_hash, members_pass_salt, mfa_details FROM core_members WHERE UPPER(email) = ?";
    private final String QUERY_ID = "SELECT member_id, member_group_id, mgroup_others, email, members_pass_hash, members_pass_salt, mfa_details, mgroup_others FROM core_members WHERE member_id = ?";


    private final PlayerCredentials credentials;

    public SelectForumAccountTransaction(PlayerCredentials credentials) {
        super(TableRepresentation.FORUM);
        this.credentials = credentials;
    }

    @Override
    public ForumAccount onExecute(Connection connection) throws SQLException {

        try(NamedPreparedStatement statement = NamedPreparedStatement.create(connection, QUERY_NAME)) {
            statement.setString(1, credentials.username);

            ResultSet rs = statement.executeQuery();

            if(rs.next()) {
               return ForumAccount.fromCredentials(credentials.username, unwrap(rs));
            }
        }catch (Exception e) {
         throw new SQLException(e);
        }
        return null;
    }

    private ForumCredentials unwrap(ResultSet rs) throws SQLException{
        int id = rs.getInt("member_id");
        int groupId = rs.getInt("member_group_id");
        String secondaryGroups = rs.getString("mgroup_others");
        String email = rs.getString("email");
        String passwordHash = rs.getString("members_pass_hash");
        String passwordSalt = rs.getString("members_pass_salt");
        MultifactorAuthentication authentication = new MultifactorAuthentication(rs.getString("mfa_details"));
        return new ForumCredentials(id, groupId, secondaryGroups, email, passwordHash, passwordSalt, authentication);
    }
}
