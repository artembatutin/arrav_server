package com.rageps.world.entity.actor.player.assets.group;

public enum DonatorPrivilege implements Privilege {
	PLAYER(0, Crown.NONE),
	ELDER(1, Crown.GREEN_DOLLAR_SIGN),
	ALPHA(2, Crown.RED_DOLLAR_SIGN),
	ROYAL(3, Crown.BLUE_DOLLAR_SIGN),
	DIVINE(4, Crown.GOLD_DOLLAR_SIGN),
	LEGENDARY(5, Crown.PURPLE_DOLLAR_SIGN),
	RENOWNED(6, Crown.ORANGE_DOLLAR_SIGN),
	CELESTIAL(7, Crown.PINK_DOLLAR_SIGN);

	private final int order;

	private final Crown crown;

	DonatorPrivilege(int order, Crown crown) {
		this.order = order;
		this.crown = crown;
	}

	public boolean isDonator() {
		return order > 0;
	}

	@Override
	public int priority() {
		return order;
	}

	@Override
	public Crown getCrown() {
		return crown;
	}


}
