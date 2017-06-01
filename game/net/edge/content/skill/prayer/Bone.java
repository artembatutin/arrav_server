package net.edge.content.skill.prayer;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.EnumSet;
import java.util.Optional;

public enum Bone {
	BONES(526, 4.5),
	BAT_BONES(530, 5.2),
	MONKEY_BONES(3179, 5),
	WOLF_BONES(2859, 4.5),
	BIG_BONES(532, 15),
	BABYDRAGON_BONES(534, 30),
	DRAGON_BONES(536, 72),
	OURG_BONES(4834, 140);

	public static final ImmutableSet<Bone> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Bone.class));

	private final int id;
	private final double experience;

	private Bone(int id, double experience) {
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

	public static Optional<Bone> getBone(int id) {
		return VALUES.stream().filter(it -> it.id == id).findFirst();
	}
}