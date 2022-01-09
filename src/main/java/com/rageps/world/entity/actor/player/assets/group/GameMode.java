package com.rageps.world.entity.actor.player.assets.group;

import com.rageps.util.StringUtil;

public enum GameMode {

	NORMAL(0),
	HARDCORE_IRONMAN(1),
	IRONMAN(2),
	ULTIMATE_IRONMAN(3),
	PK_MODE(4),
	GROUP_IRONMAN(5);

	private int value;

	GameMode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	public boolean isIronman() {
		return this == IRONMAN || this == HARDCORE_IRONMAN || this == ULTIMATE_IRONMAN || this == GROUP_IRONMAN;
	}

	@Override
	public String toString() {
		return StringUtil.formatEnumString(this);
	}
}
