package com.rageps.world.entity.actor.player.assets.group;

/**
 * Created by Ryley Kimmel on 2/7/2017.
 */
public enum Crown {
	NONE(-1, CrownType.NONE),
	IRONMAN(0, CrownType.SPECIAL),
	HARDCORE_IRONMAN(1, CrownType.SPECIAL),
	YOUTUBE(2, CrownType.PLAYER),
	VETERAN(3, CrownType.SPECIAL),
	GREEN_DOLLAR_SIGN(4, CrownType.DONATOR),
	RED_DOLLAR_SIGN(5, CrownType.DONATOR),
	BLUE_DOLLAR_SIGN(6, CrownType.DONATOR),
	GOLD_DOLLAR_SIGN(7, CrownType.DONATOR),
	INFORMATION(8, CrownType.PLAYER),
	SILVER_M(9, CrownType.PLAYER),
	GOLD_A(10, CrownType.PLAYER),
	RED_M(11, CrownType.PLAYER),
	JAVA(12, CrownType.PLAYER),
	ULTIMATE_IRONMAN(13, CrownType.SPECIAL),
	TURQUOISE_M(14, CrownType.PLAYER),
	PURPLE_M(15, CrownType.PLAYER),
	BLUE_M(16, CrownType.PLAYER),
	NEON_GREEN_M(17, CrownType.PLAYER),
	SKULL(18, CrownType.SPECIAL),
	GOLD_M(19, CrownType.PLAYER),
	TOP_PKER(20, CrownType.SPECIAL),
	DISCORD(21, CrownType.SPECIAL),
	PURPLE_DOLLAR_SIGN(22, CrownType.DONATOR),
	ORANGE_DOLLAR_SIGN(23, CrownType.DONATOR),
	PINK_DOLLAR_SIGN(24, CrownType.DONATOR),
	ORANGE_M(25, CrownType.PLAYER),
	KOTS_IRONMAN(26, CrownType.SPECIAL);

	private final CrownType type;

	private final int id;

	Crown(int id, CrownType type) {
		this.id = id;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public CrownType getType() {
		return type;
	}
}
