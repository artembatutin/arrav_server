package com.rageps.content.minigame.pestcontrol.pest;

import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.region.TraversalMap;
import com.rageps.world.locale.Position;

import java.util.Optional;

public class Defiler extends Pest {

	/**
	 * Creates a new {@link Mob}.
	 * @param id the identification for this NPC.
	 * @param position the position of this character in the world.
	 */
	public Defiler(int id, Position position) {
		super(id, position);
		getAttr().get("master_archery").set(true);
	}

	@Override
	public void sequence(Mob knight) {
		if(getCombat().getLastAttacker() != null && getCombat().getLastAttacker().isPlayer()) {
			getCombat().attack(getCombat().getLastAttacker());
		} else if(!getPosition().withinDistance(knight.getPosition(), 15) && !getCombat().isAttacking()) {
			Position delta = Position.delta(getPosition(), knight.getPosition());
			int x = RandomUtils.inclusive(delta.getX() < 0 ? -delta.getX() : delta.getX());
			int y = RandomUtils.inclusive(delta.getY() < 0 ? -delta.getY() : delta.getY());
			Position move = getPosition().move((delta.getX() < 0 ? -x : x) + RandomUtils.inclusive(3), (delta.getY() < 0 ? -y : y) + RandomUtils.inclusive(3));
			Optional<Position> destination = TraversalMap.getRandomTraversableTile(move, size());
			destination.ifPresent(d -> getMovementQueue().smartWalk(d));
		}
	}

	@Override
	public boolean aggressive() {
		return true;
	}

	@Override
	public boolean ranged() {
		return true;
	}
}
