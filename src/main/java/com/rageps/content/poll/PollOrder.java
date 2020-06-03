package com.rageps.content.poll;

/**
 * Created by Jason MacKeigan on 2017-06-05 at 3:58 PM
 *
 * Represents the order in which this poll is drawn on the interface.
 */
public enum PollOrder {
	FIRST(0), SECOND(1), THIRD(2), FOURTH(3), FIFTH(4);

	private final int index;

	PollOrder(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
