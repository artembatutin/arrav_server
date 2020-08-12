package com.rageps.world.entity.actor.player.persist.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.rageps.net.codec.login.LoginCode;
import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.player.PlayerAccountLoadTransaction;
import com.rageps.net.sql.player.PlayerAccountSaveTransaction;
import com.rageps.net.sql.player.UpdateForumAccountTransaction;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.persist.PlayerPersistable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



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

              try {

                boolean insert = player.firstLogin;
                PlayerAccountSaveTransaction saveTransaction = new PlayerAccountSaveTransaction(player, insert);
                UpdateForumAccountTransaction saveUserGroups = new UpdateForumAccountTransaction(player);
                saveUserGroups.execute(saveUserGroups.getRepresentation().getWrapper().open());
                saveTransaction.execute(saveTransaction.getRepresentation().getWrapper().open());

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

            PlayerAccountLoadTransaction loadTransaction = new PlayerAccountLoadTransaction(player);
            loadTransaction.execute(loadTransaction.getRepresentation().getWrapper().open());

		} catch (Exception ex) {
			logger.error("Error loading player {} member {}", player.getName(), player.credentials.databaseId, ex);
			return LoginCode.COULD_NOT_COMPLETE_LOGIN;
		}
		return LoginCode.NORMAL;
	}

}
