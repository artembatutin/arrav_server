package com.rageps.net.sql.forum.account;

import com.rageps.world.entity.actor.player.assets.group.DonatorPrivilege;
import com.rageps.world.entity.actor.player.assets.group.PlayerPrivilege;
import com.rageps.world.entity.actor.player.assets.group.Privilege;
import com.rageps.world.entity.actor.player.assets.group.SpecialPrivilege;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An enumerated type representing all of the different
 * groups on a forum. Used to set the players {@link com.rageps.world.entity.actor.player.assets.group.Privilege}'s.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public enum MemberGroup {
    
    MEMBER(3, PlayerPrivilege.PLAYER),
    DEVELOPER(4, PlayerPrivilege.DEVELOPER),
    FORUM_MODERATOR(6, PlayerPrivilege.FORUM_MOD),
    OWNER(9, PlayerPrivilege.OWNER),
    GAME_MODERATOR(10, PlayerPrivilege.MODERATOR),
    YOUTUBER(11, PlayerPrivilege.NORMAL_YOUTUBER),
    SERVER_SUPPORT(13, PlayerPrivilege.SUPPORT),
    GLOBAL_MODERATOR(14, PlayerPrivilege.MODERATOR),
    ADMINISTRATOR(15, PlayerPrivilege.ADMINISTRATOR),
    ELDER(16, DonatorPrivilege.ELDER),
    ALPHA(17, DonatorPrivilege.ALPHA),
    ROYAL(18, DonatorPrivilege.ROYAL),
    DIVINE(19, DonatorPrivilege.DIVINE),
    EX_STAFF(20, PlayerPrivilege.PLAYER),
    COMMUNITY_MANAGER(21, PlayerPrivilege.COMMUNITY_MANAGER),
    CELESTIAL(23, DonatorPrivilege.CELESTIAL),
    RENOWNED(24, DonatorPrivilege.RENOWNED),
    LEGENDARY(25, DonatorPrivilege.LEGENDARY),
    IRONMAN(26, SpecialPrivilege.IRONMAN),
    HARDCORE_IRONMAN(27, SpecialPrivilege.HARDCORE_IRONMAN),
    ULTIMATE_IRONMAN(28, SpecialPrivilege.ULTIMATE_IRONMAN),
    VETERAN(29, SpecialPrivilege.VETERAN);

    /**
     * A cached map of all the groups and their identifiers.
     */
    private static final Map<Integer, MemberGroup> ALL = EnumSet.allOf(MemberGroup.class).stream().collect(Collectors.toMap(MemberGroup::getId, Function.identity()));

    /**
     * Get's the {@link MemberGroup} from groups id.
     * @param id
     * @return
     */
    public static MemberGroup fromId(int id) {
        return ALL.get(id);
    }

    /**
     * The identifier associated with the group on the forum.
     */
    private final int id;

    /**
     * The {@link Privilege} associated with this group.
     */
    private final Privilege privilege;

    /**
     * Constructs a {@link MemberGroup}.
     * @param id the identifier of the group.
     */
    MemberGroup(int id, Privilege privilege) {
        this.id = id;
        this.privilege = privilege;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    /**
     * Get's the id of the {@link MemberGroup}.
     * @return The id.
     */
    public int getId() {
        return id;
    }
}
