package net.arrav.content.skill.agility.test.obstacle.impl;

import net.arrav.content.skill.agility.test.obstacle.Obstacle;
import net.arrav.task.Task;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 7-8-2017.
 */
public class ClimbableObstacle extends Obstacle {
	
	public ClimbableObstacle(Position[] start, Position end, Animation animation, int requirement, double experience) {
		super(start, end, animation, requirement, experience, 3);
	}
	
	public ClimbableObstacle(Position start, Position end, Animation animation, int requirement, double experience) {
		super(new Position[]{start}, end, animation, requirement, experience, 3);
	}
	
	public ClimbableObstacle(Position start, Position end, int animationId, int requirement, double experience) {
		super(new Position[]{start}, end, new Animation(animationId), requirement, experience, 3);
	}
	
	public ClimbableObstacle(Position[] start, Position end, int animationId, int requirement, double experience) {
		this(start, end, new Animation(animationId), requirement, experience);
	}
	
	@Override
	public void initialize(Player player) {
		player.animation(animation);
	}
	
	@Override
	public void execute(Player player, Task t) {
		player.move(travelback() && player.getPosition().same(end) ? start[0] : end);
		player.setTeleportStage(10);
		player.task(2, p -> p.setTeleportStage(0));
		t.cancel();
	}
}
