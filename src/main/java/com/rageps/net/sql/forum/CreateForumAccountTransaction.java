package com.rageps.net.sql.forum;

import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.forum.account.MemberGroup;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CreateForumAccountTransaction extends DatabaseTransactionFuture<Boolean> {


    private final String username;

    private final String passwordHash;

    private final String passwordSalt;

    private final String ipAddress;

    private final String QUERY =
            "INSERT INTO core_members (`name`, member_group_id, joined, ip_address, `language`, members_seo_name, members_pass_hash, members_pass_salt) VALUES (?,?,?,?,?,?,?,?)";



    public CreateForumAccountTransaction(String username, String passwordHash, String passwordSalt, String ipAddress) {
        super(TableRepresentation.FORUM);
        this.username = username;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.ipAddress = ipAddress;
    }

    @Override
    public Boolean onExecute(Connection connection) throws SQLException {

        try(NamedPreparedStatement statement = NamedPreparedStatement.create(connection, QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, username);
            statement.setInt(2, MemberGroup.MEMBER.getId());
            statement.setLong(3, System.currentTimeMillis()/1000);
            statement.setString(4, ipAddress);
            statement.setInt(5, 1);//language
            statement.setString(6, username);
            statement.setString(7, passwordHash);
            statement.setString(8, passwordSalt);
            // Execute and validate query success
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to create user [" + username + "]: no rows affected");
            }


            // User creation succeeded, let's create the user with the newly generated unique ID.
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                System.out.println("Created user with id "+id);
            }


        }

        return null;
    }
}
