package net.edge.content.minigame.pestcontrol.pest;

import net.edge.locale.Position;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Graphic;
import net.edge.world.World;
import net.edge.world.node.actor.npc.Npc;

import java.util.Optional;

public class Shifter extends Pest {
	
	/**
	 * Creates a new {@link Npc}.
	 * @param id       the identification for this NPC.
	 * @param position the position of this character in the world.
	 */
	public Shifter(int id, Position position) {
		super(id, position);
	}
	
	@Override
	public void sequence(Npc knight) {
		//teleporting towards the knight.
		if((!getPosition().withinDistance(knight.getPosition(), 6) && getCombatBuilder().getAggressor() == null) || RandomUtils.inclusive(3) == 1) {
			Position delta = Position.delta(getPosition(), knight.getPosition());
			int x = RandomUtils.inclusive(delta.getX() < 0 ? -delta.getX() : delta.getX());
			int y = RandomUtils.inclusive(delta.getY() < 0 ? -delta.getY() : delta.getY());
			Position move = getPosition().move((delta.getX() < 0 ? -x : x) + RandomUtils.inclusive(3), (delta.getY() < 0 ? -y : y) + RandomUtils.inclusive(3));
			Optional<Position> destination = World.getTraversalMap().getRandomTraversableTile(move, size());
			destination.ifPresent(this::move);
			graphic(new Graphic(308, 100));
			getCombatBuilder().reset();
		}
		
		if(!getCombatBuilder().isAttacking()) {
			if((getCombatBuilder().getAggressor() != null && getCombatBuilder().getAggressor().isPlayer()))
				getCombatBuilder().attack(getCombatBuilder().getAggressor());
			else if(getPosition().withinDistance(knight.getPosition(), 6))
				getCombatBuilder().attack(knight);
		}
	}
	
	@Override
	public boolean aggressive() {
		return true;
	}
	
}
