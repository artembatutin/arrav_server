package net.edge.content.minigame.pestcontrol.pest;

import net.edge.content.minigame.pestcontrol.PestPortal;
import net.edge.content.minigame.pestcontrol.defence.PestGate;
import net.edge.locale.Position;
import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.Hit;
import net.edge.world.PoisonType;
import net.edge.world.World;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.NpcDeath;
import net.edge.world.node.region.Region;

public class Spinner extends Pest {
	
	/**
	 * The portal protecting.
	 */
	private final PestPortal portal;
	
	/**
	 * Creates a new {@link Npc}.
	 * @param id       the identification for this NPC.
	 * @param portal   the portal to defend.
	 */
	public Spinner(int id, PestPortal portal, Position spawn) {
		super(id, spawn);
		this.portal = portal;
	}
	
	@Override
	public void sequence(Npc knight) {
		//heal portals and explode when portal is down.
		portal.healEntity(portal.getMaxHealth() / 10);
		if(portal.isDead()) {
			damage(new Hit(getCurrentHealth(), Hit.HitType.NORMAL, Hit.HitIcon.NONE));
			//hitting players.
			animation(new Animation(3907));
			World.get().submit(new Task(1, false) {
				@Override
				protected void execute() {
					Region reg = getRegion();
					reg.getPlayers().forEach(p -> {
						if(p.getPosition().withinDistance(getPosition(), 2)) {
							p.poison(PoisonType.DEFAULT_NPC);
							p.damage(new Hit(50, Hit.HitType.POISON, Hit.HitIcon.NONE));
						}
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
