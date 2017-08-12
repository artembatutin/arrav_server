package net.edge.world.entity.actor.mob.impl.glacor;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.edge.task.Task;
import net.edge.world.locale.Boundary;
import net.edge.world.Hit;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

/**
 * The class which represents the task which handles the explosing of glacors.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class GlacorExplodeTask extends Task {
	
	/**
	 * The player whom is the victim of the glacor.
	 */
	private final Player player;
	
	/**
	 * The mob who is exploding, this should always be an unstable glacor or glacyte.
	 */
	private final Mob mob;
	
	/**
	 * The amount of radius to search for players to hit.
	 */
	private final int radius;
	
	/**
	 * Determines if this explosion is caused by death.
	 */
	private final boolean death;
	
	/**
	 * Constructs a new {@link GlacorExplodeTask}.
	 * @param mob   {@link #mob}.
	 * @param death {@link #death}.
	 */
	public GlacorExplodeTask(Player player, Mob mob, boolean death) {
		super(3);
		this.player = player;
		this.mob = mob;
		this.radius = mob.getId() == 14301 ? 5 : 3;
		this.death = death;
	}
	
	@Override
	protected void onSubmit() {
		mob.resetSpecial();
		
		if(!death) {
			mob.getCombat().reset();
		}
	}
	
	/**
	 * Determines if this task should be ran again.
	 */
	private boolean initialRun;
	
	@Override
	protected void execute() {
		if(initialRun) {
			this.cancel();
			return;
		}
		
		int npcDamage = (int) ((float) mob.getCurrentHealth() / mob.getMaxHealth() * 90);
		
		mob.damage(new Hit(npcDamage));
		
		for(Player t : mob.getLocalPlayers()) {
			if(t == null) {
				continue;
			}
			if(t.isDead()) {
				continue;
			}
			if(new Boundary(t.getPosition(), t.size()).inside(mob.getPosition(), radius)) {
				int hit = t.getMaximumHealth() / 3;
				t.damage(new Hit(hit));
			}
		}
		;
		
		initialRun = true;
	}
	
	@Override
	public void onCancel() {
		if(!death && !mob.getCombat().inCombat()) {
			mob.getCombat().attack(player);
		}
	}
}
