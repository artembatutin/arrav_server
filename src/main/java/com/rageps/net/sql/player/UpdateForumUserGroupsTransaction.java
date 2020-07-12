package com.rageps.net.sql.player;

import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.world.entity.actor.player.Player;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class UpdateForumUserGroupsTransaction extends DatabaseTransaction {

    private final Player player;


    public UpdateForumUserGroupsTransaction(Player player) {
        super(TableRepresentation.PLAYER);
        this.player = player;
    }


    @Override
    public void execute(Connection connection) throws SQLException {

    }
}
