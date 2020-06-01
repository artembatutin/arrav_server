package com.rageps.content.minigame.pestcontrol.pest;

import com.rageps.content.minigame.pestcontrol.defence.PestGate;
import com.rageps.world.World;
import com.rageps.content.minigame.pestcontrol.PestControlMinigame;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.HitIcon;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.mob.MobDeath;
import com.rageps.world.entity.region.Region;
import com.rageps.world.locale.Position;

public class Splatter extends Pest {
	
	/**
	 * The nearest gate.
	 */
	private PestGate gate;
	
	/**
	 * Creates a new {@link Mob}.
	 * @param id the identification for this NPC.
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
			damage(new Hit(getCurrentHealth(), Hitsplat.NORMAL, HitIcon.NONE));
		}
	}
	
	@Override
	public void appendDeath() {
		setDead(true);
		World.get().submit(new MobDeath(this));
		Region reg = getRegion();
		if(reg != null) {
			//hitting players.
			reg.getPlayers().forEach(p -> {
				if(p.getPosition().withinDistance(getPosition(), 1)) {
					p.damage(new Hit(p.getMaximumHealth() / 5, Hitsplat.NORMAL, HitIcon.NONE));
				}
			});
			//hitting npcs.
			reg.getMobs().forEach(n -> {
				int id = n.getId();
				if(id < 6142 || id > 6145) {//ignoring portals.
					if(n.getPosition().withinDistance(getPosition(), 1)) {
						n.damage(new Hit(n.getMaxHealth() / 3, Hitsplat.NORMAL, HitIcon.NONE));
					}
				}
			});
		}
	}
	
}
