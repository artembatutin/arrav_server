package com.rageps.content.lottery;

/**
 * Created by Jason M on 2017-04-27 at 1:32 PM
 *
 * Represents a single phase for the lottery event. Each phase determines what events
 * take place during that period of time that the phase is available.
 */
public enum LotteryPhase {
	SELECTING_LAST_LOTTERY,

	SELECTING_LOTTERY,

	ACTIVE,

	APPENDING_POT,

	CREATE_NEW_SESSION,

	UPDATE_SAVED_STATE,

	UPDATE_WINNER
	;
}
