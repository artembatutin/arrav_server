package com.rageps.content.lottery;

/**
 * Created by Jason M on 2017-04-27 at 3:06 PM
 */
public class LotterySessionWinner extends LotterySessionEntry {

	private final LotteryPlacement placement;

	public LotterySessionWinner(LotteryPlacement placement, String name, int tickets) {
		super(name, tickets);
		this.placement = placement;
	}

	public final LotteryPlacement getPlacement() {
		return placement;
	}
}
