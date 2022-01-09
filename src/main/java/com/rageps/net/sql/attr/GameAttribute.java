package com.rageps.net.sql.attr;

public enum GameAttribute {
	FIRST_MAXED(0),
	FIRST_COMPLETIONIST(1),
	FIRST_MAXED_IRONMAN(2),
	FIRST_MAXED_HCI(3),
	FIRST_MAXED_UIM(4),
	FIRST_MAXED_EXTREME(5),
	FIRST_300_KILLS(6),
	FIRST_200M_NON_CB(7),
	FIRST_200M_CB(8),
	INFERNAL_CAPES(9);

	private final int id;

	GameAttribute(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
