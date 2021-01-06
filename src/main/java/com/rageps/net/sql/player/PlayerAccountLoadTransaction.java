package com.rageps.net.sql.player;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerCredentials;
import com.rageps.world.entity.actor.player.persist.property.PlayerPersistanceProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.rageps.world.entity.actor.player.persist.PlayerPersistenceManager.PROPERTIES;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class PlayerAccountLoadTransaction extends DatabaseTransaction {

    private final PlayerCredentials player;

    private final JsonParser parser = new JsonParser();

    private final String query = "SELECT * FROM player WHERE member_id = ?";


    public PlayerAccountLoadTransaction(PlayerCredentials player) {
        super(TableRepresentation.PLAYER);
        this.player = player;
    }


        @Override
        public void execute(Connection connection) throws SQLException {

            try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong("member_id", player.databaseId);

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

}
