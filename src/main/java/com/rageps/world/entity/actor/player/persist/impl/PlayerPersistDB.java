package com.rageps.world.entity.actor.player.persist.impl;

import com.rageps.net.refactor.codec.login.LoginConstants;
import com.rageps.net.sql.player.PlayerAccountLoadTransaction;
import com.rageps.net.sql.player.PlayerAccountSaveTransaction;
import com.rageps.net.sql.player.UpdateForumAccountTransaction;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerCredentials;
import com.rageps.world.entity.actor.player.persist.PlayerLoaderResponse;
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
	public PlayerLoaderResponse load(PlayerCredentials credentials) {
			//if (!MysqlUserDao.INSTANCE.playerSaveExist(player.getUser())) {
			//	player.firstSession = true;
			//	player.doingTutorial = true;
			//	return LoginResponse.NORMAL;
			//}
        Player player = new Player(credentials);
        PlayerLoaderResponse response = new PlayerLoaderResponse(LoginConstants.STATUS_OK, player);
		try {
            PlayerAccountLoadTransaction loadTransaction = new PlayerAccountLoadTransaction(player);
            loadTransaction.execute(loadTransaction.getRepresentation().getWrapper().open());

		} catch (Exception ex) {
			logger.error("Error loading player {} member {}", credentials.username, credentials.databaseId, ex);
			return new PlayerLoaderResponse(LoginConstants.STATUS_COULD_NOT_COMPLETE);
		}
		return response;
	}

}
