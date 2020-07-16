package com.rageps.net.sql.player;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DatabaseUtils;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.persist.property.PlayerPersistanceProperty;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.rageps.world.entity.actor.player.persist.PlayerPersistenceManager.PROPERTIES;
import static com.rageps.world.entity.actor.player.persist.PlayerPersistenceManager.PROPERTY_NAMES;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class PlayerAccountSaveTransaction extends DatabaseTransaction {

    private final boolean insert;

    private final Player player;

    public PlayerAccountSaveTransaction(Player player, boolean insert) {
        super(TableRepresentation.PLAYER);
        this.player = player;
        this.insert = insert;
    }
        @Override
        public void execute(Connection connection) throws SQLException {
            String stmnt = (insert ? DatabaseUtils.generateInsertStatement("player", PROPERTY_NAMES)
                    : ("update `player` set " + DatabaseUtils.getUpdateStrucutre(PROPERTY_NAMES) + " where `member_id` = " + player.credentials.databaseId));
            try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, stmnt, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong("database_id", player.credentials.databaseId);
                statement.setString("user_name", player.credentials.username);

                for (PlayerPersistanceProperty property : PROPERTIES) {
                    statement.setObject(property.name, property.write(player), property.getType());
                }

                statement.execute();
                connection.commit();
                boolean saved = (statement.getUpdateCount()) > 0;


                if(saved) {
                    if (player.firstLogin)
                        player.firstLogin = false;
                    player.saved.set(true);//don't ever remove this.
                } else {
                    throw new SQLException("Couldn't save player:"+player.getName()+" id:"+player.credentials.databaseId+" host:"+player.getSession().getHost());
                }
            } catch (Exception e) {
                if(e instanceof MySQLIntegrityConstraintViolationException) {
                    LOGGER.info("fuck", e);
                }
                LOGGER.error(
                        "Error saving {} member id {} new player {}",
                        player.getName(),
                        player.credentials.databaseId,
                        player.firstLogin, e);
            }
        }

}
