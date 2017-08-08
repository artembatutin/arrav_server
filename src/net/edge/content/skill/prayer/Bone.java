package net.edge.content.skill.prayer;

public enum Bone {
	BONES(526, 4.5),
	BAT_BONES(530, 5.2),
	MONKEY_BONES(3179, 5),
	WOLF_BONES(2859, 4.5),
	BIG_BONES(532, 15),
	BABYDRAGON_BONES(534, 30),
	DRAGON_BONES(536, 72),
	OURG_BONES(4834, 140),
	DAGANNOTH_BONES(6729, 125);

	private final int id;
	private final double experience;

	Bone(int id, double experience) {
		this.id = id;
		this.experience = experience;
	}

	public int getId() {
		return id;
	}

	public double getExperience() {
		return experience;
	}

	@Override
	public String toString() {
		return name().toLowerCase().replaceAll("_", " ");
	}

}