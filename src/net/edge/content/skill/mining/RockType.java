package net.edge.content.skill.mining;

/**
 * The enumerated type which elements ore color types.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
enum RockType {
	TYPE1(47766, 47768, 47767),
	TYPE2(63299, 63301, 63300),
	TYPE3(56835, 56837, 56836),
	TYPE4(57585, 57587, 57586),
	TYPE5(61006, 61008, 61007),
	TYPE6(59029, 59031, 59030),
	TYPE7(79703, 79705, 79704),
	TYPE8(52583, 52585, 52584),
	TYPE9(50831, 50833, 50832),
	TYPE10(53558, 53560, 53559),
	TYPE11(52947, 52947, 52948),
	TYPE12(59010, 59012, 59011),
	TYPE13(51726, 51728, 51727),
	TYPE14(57252, 57254, 57253),
	TYPE15(61030, 61032, 61031),
	TYPE16(67374, 67376, 67375),
	TYPE18(52588, 52589, 52590),
	TYPE19(73062, 73064, 73063),
	TYPE20(74450, 74450, 74451);

	private final int big, crushed, small;

	RockType(int big, int crushed, int small) {
		this.big = big;
		this.crushed = crushed;
		this.small = small;
	}

	public int getBig() {
		return big;
	}

	public int getCrushed() {
		return crushed;
	}

	public int getSmall() {
		return small;
	}
}