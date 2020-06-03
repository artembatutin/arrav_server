package com.rageps.world;

import com.rageps.util.StringUtil;

public enum GameMode {

	NORMAL(0),
	HARDCORE_IRONMAN(1),
	IRONMAN(2),
	ULTIMATE_IRONMAN(3),
	PK_MODE(4),
	KOTS_IRONMAN(5);

	private int value;

	GameMode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return StringUtil.formatEnumString(this);
	}
}
