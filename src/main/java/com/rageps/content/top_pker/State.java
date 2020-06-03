package com.rageps.content.top_pker;

/**
 * The state of the current session. This defines the current action that is taking place
 * when sequencing.
 */
public enum State {
	SELECTING_LAST_SESSION,

	SELECTING_SESSION,

	ACTIVE,

	UPDATING_WINNER,

	APPENDING_REWARDS,

	CREATING_NEW_SESSION,

	UPDATING_STATE,

	DISABLED;
}
