package com.rageps.world.entity.actor.player.assets.group;

/**
 * Created by Jason M on 2017-05-02 at 2:08 PM
 */
public enum SpecialPrivilege implements Privilege {
    PLAYER(0, Crown.NONE),
    TOP_PKER(0, Crown.TOP_PKER),
    VETERAN(0, Crown.VETERAN),
    IRONMAN(0, Crown.IRONMAN),
    HARDCORE_IRONMAN(0, Crown.HARDCORE_IRONMAN),
    ULTIMATE_IRONMAN(0, Crown.ULTIMATE_IRONMAN),
    KOTS_IRONMAN(0, Crown.KOTS_IRONMAN),
    EXTREME(0, Crown.SKULL),
    PK_MODE(0, Crown.NONE),
    ;

    private final int priority;

    private final Crown crown;

    SpecialPrivilege(int priority, Crown crown) {
        this.priority = priority;
        this.crown = crown;
    }

    /**
     * The priority of the rank
     *
     * @return The priority of the rank
     */
    @Override
    public int priority() {
        return priority;
    }

    /**
     * The displayed Crown of this Privilege.
     *
     * @return The displayed Crown of this Privilege.
     */
    @Override
    public Crown getCrown() {
        return crown;
    }


}
