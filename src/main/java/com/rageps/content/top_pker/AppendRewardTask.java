package com.rageps.content.top_pker;

import com.rageps.net.sql.top_pkers.TopPkerGenerateRewardsTransaction;
import com.rageps.task.Task;
import com.rageps.task.TaskManager;
import com.rageps.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZonedDateTime;

/**
 * The task responsible for appending the rewards to the winner of the session.
 */
class AppendRewardTask extends Task {

	/**
	 * The Logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(AppendRewardTask.class);

	/**
	 * The winner of the session.
	 */
	private final Winner winner;

	/**
	 * The end date of the task.
	 */
	private final ZonedDateTime endDate;

	/**
	 * The database transaction for generating the rewards
	 */
	private final TopPkerGenerateRewardsTransaction generateRewardsTransaction;

	/**
	 * Creates a new task for the given winner. A new task is instantly submitted to {@link TaskManager} via {@link TaskManager#submit(Task)} that
	 * will try and load the player from the character file.
	 *
	 * @param winner the winner of the session.
	 */
	AppendRewardTask(int session, Winner winner, ZonedDateTime endDate) {
		this.winner = winner;
		this.endDate = endDate;
		this.generateRewardsTransaction = new TopPkerGenerateRewardsTransaction(session);
		World.get().getDatabaseWorker().submit(generateRewardsTransaction);
	}

	@Override
	protected void execute() {
		if (generateRewardsTransaction.isFinished()) {

			//todo add collection box
			/*PlayerFileLoader loader = new PlayerFileLoader(winner.getUsernameAsLong());
			loader.consumeAsync(result -> {
				Player playerWinner = result.getPlayer();

				if (!result.success()) {
					// If the player is currently occupied, wait and try again
					if (result.getResponse() != PlayerFileLoader.PlayerLoadResponse.PLAYER_OCCUPIED_BY_ANOTHER_TASK) {
						logger.error("Winner was: {} but could not be loaded. Response code: {}", winner.getUsernameAsLong(), result.getResponse());
						super.stop();
					}
				} else {
					for (PredefinedReward reward : PredefinedReward.values()) {
						if (reward.appendable(playerWinner, endDate)) {
							reward.append(playerWinner);
						}
					}
					for (OnDemandReward reward : generateRewardsTransaction.getResult()) {
						reward.append(playerWinner);
					}
					super.stop();
				}
			});*/
		}
	}
}
