package net.edge.content.skill.agility.test.obstacle.impl;

import net.edge.content.skill.agility.test.obstacle.Obstacle;
import net.edge.world.Animation;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.update.UpdateFlag;
import net.edge.world.locale.Position;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 7-8-2017.
 */
public class WalkableObstacle extends Obstacle {

    public WalkableObstacle(int[] ids, Position[] start, Position end, Animation animation, int requirement, double experience, boolean travelback) {
        super(ids, start, end, animation, requirement, experience, 0, travelback);
    }

    public WalkableObstacle(int id, Position[] start, Position end, Animation animation, int requirement, double experience, boolean travelback) {
        super(new int[]{id}, start, end, animation, requirement, experience, 0, travelback);
    }

    public WalkableObstacle(int id, Position start, Position end, Animation animation, int requirement, double experience, boolean travelback) {
        super(new int[]{id}, new Position[]{start}, end, animation, requirement, experience, 0, travelback);
    }

    public WalkableObstacle(int id, Position start, Position end, int animationId, int requirement, double experience, boolean travelback) {
        super(new int[]{id}, new Position[]{start}, end, new Animation(animationId), requirement, experience, 0, travelback);
    }

    @Override
    public void initialize(Player player) {
        int animation = this.animation.getId();
        player.setWalkIndex(animation);
        player.setRunIndex(animation);
        player.setStandIndex(animation);
        player.setTurn180Index(animation);
        player.setTurn90CWIndex(animation);
        player.setTurn90CCWIndex(animation);
        player.setTurnIndex(animation);
        player.getFlags().flag(UpdateFlag.APPEARANCE);
        player.getMovementQueue().walk(end);
    }

    @Override
    public void onStop(Player player) {
        int animation = -1;
        player.setWalkIndex(animation);
        player.setRunIndex(animation);
        player.setStandIndex(animation);
        player.setTurn180Index(animation);
        player.setTurn90CWIndex(animation);
        player.setTurn90CCWIndex(animation);
        player.setTurnIndex(animation);
        player.getFlags().flag(UpdateFlag.APPEARANCE);
    }
}
