package com.rageps.content.lottery;

import com.rageps.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * The task responsible for appending the rewards to the winner of the session.
 */
class LotteryRewardTask extends Task {

	/**
	 * The maximum attempts per tick.
	 */
	private static final int MAXIMUM_ATTEMPTS = 10;

	/**
	 * The Logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(LotteryRewardTask.class);

	/**
	 * The session we're rewarding players for.
	 */
	private final LotterySession session;

	/**
	 * The total pot after being taxed.
	 */
	private final int pot;


	LotteryRewardTask(LotterySession session) {
		this.session = session;
		this.pot = session.getPotAfterTax();

		for (LotterySessionWinner winner : session.getWinners().values()) {
			int percentageOfPot = (int) (pot * winner.getPlacement().getPercentageOfPot());
			long bankNotes = percentageOfPot / 500_000_000;
			//todo - implement the offline collections box and add here.
		}
	}

	@Override
	protected void execute() {

		//todo add a check here that it is one and cancel it
		if (super.getCounter() > 100) {
			super.cancel();
		}
	}
}
