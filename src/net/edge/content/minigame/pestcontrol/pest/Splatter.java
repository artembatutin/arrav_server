package net.edge.content.minigame.pestcontrol.pest;

import net.edge.content.minigame.pestcontrol.PestControlMinigame;
import net.edge.content.minigame.pestcontrol.defence.PestGate;
import net.edge.world.locale.Position;
import net.edge.world.Hit;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.MobDeath;
import net.edge.world.entity.region.Region;

public class Splatter extends Pest {
	
	/**
	 * The nearest gate.
	 */
	private PestGate gate;
	
	/**
	 * Creates a new {@link Mob}.
	 * @param id       the identification for this NPC.
	 * @param position the position of this character in the world.
	 */
	public Splatter(int id, Position position) {
		super(id, position);
	}
	
	@Override
	public void sequence(Mob knight) {
		//when dead, explode and cause damage around, no damage on portals.
		if(gate == null) {
			gate = PestControlMinigame.getNearestGate(getPosition());
		}
	}
	
	@Override
	public boolean aggressive() {
		return true;
	}
	
	@Override
	public void setPosition(Position position) {
		//Updating the region if the entity entered another one.
		if(getSlot() != -1 && getPosition() != null && getPosition().getRegion() != position.getRegion()) {
			regionChanged = getPosition();
		}
		super.setPosition(position);
		if(gate != null && gate.getPos().withinDistance(getPosition(), 1)) {
			//exploding near gates.
			damage(new Hit(getCurrentHealth(), Hit.HitType.NORMAL, Hit.HitIcon.NONE));
		}
	}
	
	@Override
	public void appendDeath() {
		setDead(true);
		World.get().submit(new MobDeath(this));
		getRegion().ifPresent(reg -> {
			//hitting players.
			reg.getPlayers().forEach(p -> {
				if(p.getPosition().withinDistance(getPosition(), 1)) {
					p.damage(new Hit(p.getMaximumHealth() / 5, Hit.HitType.NORMAL, Hit.HitIcon.NONE));
				}
			});
			//hitting npcs.
			reg.getMobs().forEach(n -> {
				int id = n.getId();
				if(id < 6142 || id > 6145) {//ignoring portals.
					if(n.getPosition().withinDistance(getPosition(), 1)) {
						n.damage(new Hit(n.getMaxHealth() / 3, Hit.HitType.NORMAL, Hit.HitIcon.NONE));
					}
				}
			});
		});
	}
	
}
