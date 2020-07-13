package com.rageps.world.entity.actor.player.persist.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.rageps.net.codec.login.LoginCode;
import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.player.UpdateForumUserGroupsTransaction;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DatabaseUtils;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.persist.PlayerPersistable;
import com.rageps.world.entity.actor.player.persist.property.PlayerPersistanceProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.rageps.world.entity.actor.player.persist.PlayerPersistenceManager.PROPERTIES;
import static com.rageps.world.entity.actor.player.persist.PlayerPersistenceManager.PROPERTY_NAMES;

/**
 * Handles persistence using a MySQL database.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public final class PlayerPersistDB implements PlayerPersistable {

    /**
     * Logging for this class.
     */
	private static final Logger logger = LogManager.getLogger();

	@Override
	public void save(Player player) {

	 // if(player.doingTutorial) {
     //  player.saved.set(true);
     //  return;
     // }

              UpdateForumUserGroupsTransaction saveUserGroups = new UpdateForumUserGroupsTransaction(player);
              try {

                boolean insert = player.firstLogin;

                DatabaseTransactionFuture<Long> saveTransaction = new DatabaseTransactionFuture<Long>(TableRepresentation.PLAYER) {
                  @Override
                  public Long onExecute(Connection connection) throws SQLException {
                    String stmnt = (insert ? DatabaseUtils.generateInsertStatement("player", PROPERTY_NAMES)
                            : ("update `player` set " + DatabaseUtils.getUpdateStrucutre(PROPERTY_NAMES) + " where `member_id` = " + player.credentials.databaseId));
                    try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, stmnt, Statement.RETURN_GENERATED_KEYS)) {
                      statement.setLong("database_id", player.credentials.databaseId);
                      statement.setString("user_name", player.credentials.username);

                      for (PlayerPersistanceProperty property : PROPERTIES) {
                        statement.setObject(property.name, property.write(player));
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
                              logger.info("fuck", e);
                          }
                          logger.error(
                                  "Error saving {} member id {} new player {}",
                                  player.getName(),
                                  player.credentials.databaseId,
                                  player.firstLogin, e);
                      }
                    return null;
                  }
                };
                World.get().getDatabaseWorker().submit(saveTransaction);
                World.get().getDatabaseWorker().submit(saveUserGroups);

              } catch (Exception e) {
                logger.error("Error saving {} member id {}", player.getName(), player.credentials.databaseId);
                e.printStackTrace();
              }
	}

	@Override
	public LoginCode load(Player player) {
			//if (!MysqlUserDao.INSTANCE.playerSaveExist(player.getUser())) {
			//	player.firstSession = true;
			//	player.doingTutorial = true;
			//	return LoginResponse.NORMAL;
			//}
		try {
          String query = "SELECT * FROM player WHERE member_id = ?";

          final JsonParser parser = new JsonParser();

          DatabaseTransaction loadTransaction = new DatabaseTransaction(TableRepresentation.PLAYER) {

            @Override
            public void execute(Connection connection) throws SQLException {

              try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong("member_id", player.credentials.databaseId);

                ResultSet rs = statement.executeQuery();

                while(rs.next()) {
                  for (int i = 0; i < PROPERTIES.length; i++) {
                    int column = i + 1;
                    PlayerPersistanceProperty property = PROPERTIES[i];
                    Object value = rs.getObject(property.name);
                    if (value == null) continue;
                    JsonElement element = new JsonPrimitive(value.toString());
                    if (rs.getMetaData().getColumnTypeName(column).equals("JSON")) {
                      property.read(player, parser.parse(rs.getString(property.name)));
                      continue;
                    }
                    property.read(player, element);
                  }
                }
              }
              connection.commit();
            }
          };
          loadTransaction.execute(TableRepresentation.PLAYER.getWrapper().open());

		} catch (Exception ex) {
			logger.error("Error loading player {} member {}", player.getName(), player.credentials.databaseId, ex);
			return LoginCode.COULD_NOT_COMPLETE_LOGIN;
		}
		return LoginCode.NORMAL;
	}

}
