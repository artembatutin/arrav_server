package com.rageps.net.rest.account;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum MemberGroup {
    MEMBER(3),
    DEVELOPER(4),
    FORUM_MODERATOR(6),
    OWNER(9),
    GAME_MODERATOR(10),
    YOUTUBER(11),
    SERVER_SUPPORT(13),
    GLOBAL_MODERATOR(14),
    ADVISOR(15),
    ELDER(16),
    ALPHA(17),
    ROYAL(18),
    DIVINE(19),
    EX_STAFF(20),
    COMMUNITY_MANAGER(21),
    CELESTIAL(23),
    RENOWNED(24),
    LEGENDARY(25);

    private static final Map<Integer, MemberGroup> ALL = EnumSet.allOf(MemberGroup.class).stream().collect(Collectors.toMap(MemberGroup::getId, Function.identity()));

    public static MemberGroup fromId(int id) {
        return ALL.get(id);
    }

    private final int id;

    MemberGroup(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
