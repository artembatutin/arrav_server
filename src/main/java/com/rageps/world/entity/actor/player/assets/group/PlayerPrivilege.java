package com.rageps.world.entity.actor.player.assets.group;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public enum PlayerPrivilege implements Privilege {
	PLAYER(0, Crown.NONE),
	SPAWN_YOUTUBER(0, Crown.YOUTUBE),
	NORMAL_YOUTUBER(0, Crown.YOUTUBE),
	FORUM_MOD(0, Crown.NEON_GREEN_M),
	SUPPORT(1, Crown.INFORMATION),
	MODERATOR(2, Crown.SILVER_M),
	GLOBAL_MOD(2, Crown.PURPLE_M),
	ADMINISTRATOR(3, Crown.GOLD_A),
	COMMUNITY_MANAGER(3, Crown.ORANGE_M),
	DEVELOPER(4, Crown.JAVA),
	OWNER(4, Crown.RED_M);

	private static final ImmutableSet<PlayerPrivilege> STAFF = Sets.immutableEnumSet(SUPPORT, MODERATOR, GLOBAL_MOD, ADMINISTRATOR, COMMUNITY_MANAGER, OWNER, DEVELOPER);

	private static final ImmutableSet<PlayerPrivilege> SPAWN = Sets.immutableEnumSet(DEVELOPER, SPAWN_YOUTUBER, OWNER);

	private final int order;

	private final Crown crown;

	PlayerPrivilege(int order, Crown crown) {
		this.order = order;
		this.crown = crown;
	}

	public boolean isStaff() {
		return STAFF.contains(this);
	}

	public boolean isSpawnRank() {
		return SPAWN.contains(this);
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
