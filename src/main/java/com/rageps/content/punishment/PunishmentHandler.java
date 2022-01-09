package com.rageps.content.punishment;

import com.rageps.net.codec.login.LoginCode;
import com.rageps.net.sql.punishments.QuerySanctionsTransaction;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.env.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class PunishmentHandler {

    private static final Logger logger = LogManager.getLogger();

    public static LoginCode getBlockResponse(Player player) {
        if (World.get().getEnvironment().getType() != Environment.Type.LIVE || !World.get().getEnvironment().isSqlEnabled()) {
            return LoginCode.NORMAL;
        }

        String host = player.credentials.getHostAddress();
        String serialNumber = player.credentials.getUid();

        try {
            LoginCode result = QuerySanctionsTransaction.execute(player.credentials.username, host, serialNumber);
            if (result == LoginCode.MUTED) {
                player.muted = true;
                result = LoginCode.NORMAL;
            }
            return result;
        } catch (SQLException cause) {
            logger.error("Unable to get result for sanction future!", cause);
            return LoginCode.PLEASE_TRY_AGAIN;
        }
    }


}
