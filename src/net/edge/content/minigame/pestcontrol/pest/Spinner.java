package net.edge.content.minigame.pestcontrol.pest;

import net.edge.content.minigame.pestcontrol.PestPortal;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.hit.HitIcon;
import net.edge.content.combat.hit.Hitsplat;
import net.edge.world.locale.Position;
import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.PoisonType;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;

public class Spinner extends Pest {
	
	/**
	 * The portal protecting.
	 */
	private final PestPortal portal;
	
	/**
	 * Creates a new {@link Mob}.
	 * @param id       the identification for this NPC.
	 * @param portal   the portal to defend.
	 */
	public Spinner(int id, PestPortal portal, Position spawn) {
		super(id, spawn);
		this.portal = portal;
	}
	
	@Override
	public void sequence(Mob knight) {
		//heal portals and explode when portal is down.
		portal.healEntity(portal.getMaxHealth() / 10);
		if(portal.isDead()) {
			damage(new Hit(getCurrentHealth(), Hitsplat.NORMAL, HitIcon.NONE));
			//hitting players.
			animation(new Animation(3907));
			World.get().submit(new Task(1, false) {
				@Override
				protected void execute() {
					getRegion().ifPresent(reg -> {
						reg.getPlayers().forEach(p -> {
							if(p.getPosition().withinDistance(getPosition(), 2)) {
								p.poison(PoisonType.DEFAULT_NPC);
								p.damage(new Hit(50, Hitsplat.POISON, HitIcon.NONE));
							}
						});
					});
					cancel();
				}
			});
		}
	}
	
	@Override
	public boolean aggressive() {
		return true;
	}
	
}
