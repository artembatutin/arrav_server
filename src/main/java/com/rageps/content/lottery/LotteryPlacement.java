package com.rageps.content.lottery;

/**
 * Created by Jason M on 2017-04-27 at 3:06 PM
 */
public enum LotteryPlacement {
	FIRST(0, .6), SECOND(1, .3), THIRD(2, .1)
	;

	private final int order;

	private final double percentageOfPot;

	LotteryPlacement(int order, double percentageOfPot) {
		this.order = order;
		this.percentageOfPot = percentageOfPot;
	}

	public double getPercentageOfPot() {
		return percentageOfPot;
	}
}
