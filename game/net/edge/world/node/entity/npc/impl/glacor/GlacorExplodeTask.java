package net.edge.world.node.entity.npc.impl.glacor;

import com.google.common.collect.Lists;
import net.edge.task.Task;
import net.edge.World;
import net.edge.locale.Boundary;
import net.edge.world.Hit;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

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
	 * The npc who is exploding, this should always be an unstable glacor or glacyte.
	 */
	private final Npc npc;
	
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
	 * @param npc   {@link #npc}.
	 * @param death {@link #death}.
	 */
	public GlacorExplodeTask(Player player, Npc npc, boolean death) {
		super(3);
		this.player = player;
		this.npc = npc;
		this.radius = npc.getId() == 14301 ? 5 : 3;
		this.death = death;
	}
	
	@Override
	protected void onSubmit() {
		npc.resetSpecial();
		
		if(!death) {
			npc.getCombatBuilder().reset();
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
		
		int npcDamage = (int) ((float) npc.getCurrentHealth() / npc.getMaxHealth() * 90);
		
		npc.damage(new Hit(npcDamage));
		
		for(Player t : Lists.newArrayList(World.get().getLocalPlayers(npc))) {
			if(t.isDead()) {
				continue;
			}
			
			if(new Boundary(t.getPosition(), t.size()).inside(npc.getPosition(), radius)) {
				int hit = t.getMaximumHealth() / 3;
				t.damage(new Hit(hit));
			}
		}
		;
		
		initialRun = true;
	}
	
	@Override
	public void onCancel() {
		if(!death && !npc.getCombatBuilder().inCombat()) {
			npc.getCombatBuilder().attack(player);
		}
	}
}
