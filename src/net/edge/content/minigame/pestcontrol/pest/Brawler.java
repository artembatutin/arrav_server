package net.edge.content.minigame.pestcontrol.pest;

import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.locale.Position;

public class Brawler extends Pest {

	public Brawler(int id, Position position) {
		super(id, position);
		setOriginalRandomWalk(true);
	}

	@Override
	public void sequence(Mob knight) {
		//none
	}

	@Override
	public boolean aggressive() {
		return true;
	}
	
	/*@Override
	public void setPosition(Position position) {
		//removing clipped positions of this brawler.
		//if(getPosition() != null) {
		//	Region prev = World.getRegions().getRegion(getPosition());
		//	TraversalMap.markOccupant(prev, 0, getPosition().getX(), getPosition().getY(), size(), size(), true, false);
		//}
		//updating region, might not even need.
		if(getSlot() != -1 && getPosition() != null && getPosition().getRegion() != position.getRegion()) {
			World.getRegions().getRegion(getPosition().getRegion()).remove(this);
			World.getRegions().getRegion(position.getRegion()).add(this);
		}
		//setting new position.
		super.setPosition(position);
		
		//Clipping the brawler positions.
		//Region prev = World.getRegions().getRegion(getPosition());
		//TraversalMap.markOccupant(prev, 0, getPosition().getX(), getPosition().getY(), size(), size(), true, true);
	}*/

}
