package com.rageps.content.minigame.pestcontrol.pest;

import com.rageps.util.rand.RandomUtils;

public enum PestType {
	
	BRAWLER(3772, 3776),
	
	DEFILER(3762, 3771),
	
	RAVAGER(3742, 3746),
	
	SHIFTER(3732, 3741),
	
	SPINNER(3747, 3751),
	
	SPLATTER(3727, 3731),
	
	TORCHER(3752, 3761);
	
	private final int min;
	private final int max;
	
	PestType(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public int random() {
		return RandomUtils.inclusive(min, max);
	}
	
}
