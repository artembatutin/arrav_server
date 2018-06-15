package net.arrav.content.minigame.pestcontrol.pest;

import net.arrav.util.rand.RandomUtils;
import net.arrav.world.Graphic;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.region.TraversalMap;
import net.arrav.world.locale.Position;

import java.util.Optional;

public class Shifter extends Pest {
	
	/**
	 * Creates a new {@link Mob}.
	 * @param id the identification for this NPC.
	 * @param position the position of this character in the world.
	 */
	public Shifter(int id, Position position) {
		super(id, position);
	}
	
	@Override
	public void sequence(Mob knight) {
		//teleporting towards the knight.
		if((!getPosition().withinDistance(knight.getPosition(), 6) && getCombat().getLastAttacker() == null) || RandomUtils.inclusive(3) == 1) {
			Position delta = Position.delta(getPosition(), knight.getPosition());
			int x = RandomUtils.inclusive(delta.getX() < 0 ? -delta.getX() : delta.getX());
			int y = RandomUtils.inclusive(delta.getY() < 0 ? -delta.getY() : delta.getY());
			Position move = getPosition().move((delta.getX() < 0 ? -x : x) + RandomUtils.inclusive(3), (delta.getY() < 0 ? -y : y) + RandomUtils.inclusive(3));
			Optional<Position> destination = TraversalMap.getRandomTraversableTile(move, size());
			destination.ifPresent(this::move);
			graphic(new Graphic(308, 100));
			getCombat().reset(false, true);
		}
		
		if(!getCombat().isAttacking()) {
			if(getCombat().getLastAttacker().isPlayer())
				getCombat().attack(getCombat().getLastAttacker());
			else if(getPosition().withinDistance(knight.getPosition(), 6))
				getCombat().attack(knight);
		}
	}
	
	@Override
	public boolean aggressive() {
		return true;
	}
	
}
