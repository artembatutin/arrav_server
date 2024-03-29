package net.arrav.content.skill.agility.impl.barb.impl;

import net.arrav.content.skill.agility.obstacle.ObstacleActivity;
import net.arrav.content.skill.agility.obstacle.ObstacleType;
import net.arrav.world.entity.actor.move.ForcedMovement;
import net.arrav.world.entity.actor.move.ForcedMovementManager;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

public final class BalanceBeam extends ObstacleActivity {

	public BalanceBeam() {
		super(new Position(2533, 3553, 3), new Position(2536, 3553, 3), ObstacleType.BALANCE_BEAM.getAnimation(), 90, 15);
	}

	@Override
	public boolean canExecute(Player player) {
		if(!player.getPosition().same(getStart())) {
			player.message("You can't cross this obstacle from this position.");
			return false;
		}
		return true;
	}

	public void start(Player player) {
		ForcedMovement movement = new ForcedMovement(player);
		movement.setFirst(getStart());
		movement.setSecond(new Position(2535, 3553, 3));
		movement.setFirstSpeed(10);
		movement.setSecondSpeed(65);
		movement.setAnimation(getAnimation());
		ForcedMovementManager.submit(player, movement);
	}

	@Override
	public void onSubmit(Player player) {
		start(player);
	}
}
