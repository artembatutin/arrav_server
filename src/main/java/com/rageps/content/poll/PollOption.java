package com.rageps.content.poll;

/**
 * Created by Jason MacKeigan on 2017-06-05 at 3:02 PM
 *
 * A poll option is something a player selects when determining how they want
 * to vote on a given poll.
 */
public enum PollOption {
	FIRST(0), SECOND(1), THIRD(2), FOURTH(3)
	;

	private final int config;

	PollOption(int config) {
		this.config = config;
	}

	public int getConfig() {
		return config;
	}
}
