package com.rageps.content.skill.agility.impl.barb.impl;

import com.rageps.content.skill.agility.obstacle.ObstacleActivity;
import com.rageps.content.skill.agility.obstacle.ObstacleType;
import com.rageps.world.entity.actor.move.ForcedMovement;
import com.rageps.world.entity.actor.move.ForcedMovementManager;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;

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
