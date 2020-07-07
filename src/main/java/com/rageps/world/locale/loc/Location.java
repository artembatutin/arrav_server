package com.rageps.world.locale.loc;

import com.rageps.world.entity.Entity;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public enum Location {

    PEST_LOBBY(new SquareArea(2659, 2637, 2664, 2644, 0), false, false, false, false, false, false),
    PEST_CONTROL(new SquareArea(2618, 2556, 2692, 2624, 0), true, false, true, true, true, true),
    CLAN_WARS(new SquareArea(3264, 3672, 3279, 3695, 0), true, false, true, true, true, true),
    DUEL_ARENA(new SquareArea(3328, 3203, 3394, 3282, 0), false, false, true, false, false, false),
    CORPOREAL_BEAST(new SquareArea(2974, 4367, 3003, 4400, 2), true, true, true, true, true, true),
    KING_BLACK_DRAGON(new SquareArea(2292, 4675, 2252, 4713, 0), true, true, true, true, true, true),
    KALPHITE_TUNNEL(new SquareArea(3449, 9467, 3521, 9537, 2), true, true, true, true, true, true),
    KALPHITE_QUEEN(new SquareArea(3456, 9477, 3519, 9533, 0), true, true, true, true, true, true),
    FUN_PVP(new SquareArea(3076, 3508, 3083, 3516, 0), true, false, true, false, false, true),
    WILDERNESS(new SquareArea(2941, 3525, 3392, 3966, 0), true, false, true, false, false, true, 0, 1, 2, 3, 4),
    HOME(new SquareArea(7743), false, true, true, false, false, true),

    ;


    private Area area;

    private boolean multi;

    private boolean summonAllowed;

    private boolean followingAllowed;

    private boolean cannonAllowed;

    private boolean firemakingAllowed;

    private boolean aidingAllowed;

    Location(Area area, boolean multi, boolean summonAllowed, boolean followingAllowed, boolean cannonAllowed, boolean firemakingAllowed, boolean aidingAllowed, int... z) {
        this.area = area;
        this.multi = multi;
        this.summonAllowed = summonAllowed;
        this.followingAllowed = followingAllowed;
        this.cannonAllowed = cannonAllowed;
        this.firemakingAllowed = firemakingAllowed;
        this.aidingAllowed = aidingAllowed;
    }

    public Area getArea() {
        return area;
    }

    public boolean isAidingAllowed() {
        return aidingAllowed;
    }

    public boolean isCannonAllowed() {
        return cannonAllowed;
    }

    public boolean isFiremakingAllowed() {
        return firemakingAllowed;
    }

    public boolean isFollowingAllowed() {
        return followingAllowed;
    }

    public boolean isMulti() {
        return multi;
    }

    public boolean isSummonAllowed() {
        return summonAllowed;
    }


    public void process(Player player) {

    }

    public boolean canTeleport(Player player) {
        return true;
    }

    /**
     * Denotes whether or not a Player in this location can receive rewards from voting, etc
     */
    public boolean canReceiveItemRewards() {
        return true;
    }

    public void login(Player player) {

    }

    public void enter(Player player) {

    }

    public void leave(Player player) {

    }

    public void logout(Player player) {

    }

    public void onDeath(Player player, Actor killer) {

    }

    public boolean handleKilledMob(Player killer, Mob npc) {
        return false;
    }

    /**
     * Handles npc death from unnatural causes (another npc, environment, but never a player)
     *
     * @param mob The npc that has died.
     */
    public void mobDeath(Mob mob) {
    }

    public boolean canAttack(Player player, Player target) {
        return false;
    }

    public boolean canBank() {
        return true;
    }

    public boolean canReceiveExperience(int skill) {
        return true;
    }

    public boolean canLogout(Player player) {
        return true;
    }

    /**
     * Determines if {@code entity} is in any of the fun pvp areas.
     * @return {@code true} if the entity is in any of these areas,
     * {@code false} otherwise.
     */
    public boolean inFunPvP() {
        return this == FUN_PVP;
    }

    /**
     * Determines if {@code entity} is in any of the wilderness areas.
     * @return {@code true} if the entity is in any of these areas,
     * {@code false} otherwise.
     */
    public boolean inWilderness() {
       return this == WILDERNESS || this == CLAN_WARS;
    }

    /**
     * Determines if this {@link Location} is in the duel area.
     * @return {@code true} if this is the duel area.
     * {@code false} otherwise.
     */
    public boolean inDuelArena() {
        return this == DUEL_ARENA;
    }


    /**
     * Determines if this {@link Location} is in the home area.
     * @return {@code true} if this is the home area.
     * {@code false} otherwise.
     */
    public boolean isAtHome() {
        return this == HOME;
    }

}
