package net.edge.content.minigame.pestcontrol;

import net.edge.locale.Position;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Hit;
import net.edge.world.World;
import net.edge.world.node.entity.npc.NpcDeath;
import net.edge.world.node.entity.npc.impl.DefaultNpc;
import net.edge.world.node.entity.player.Player;

public class VoidKnight extends DefaultNpc {
	
	private PestControlMinigame game;
	
	public VoidKnight() {
		super(RandomUtils.random(3782, 3784, 3785), new Position(2656, 2592));
		setOriginalRandomWalk(false);
		setAutoRetaliate(false);
		setCurrentHealth(2000);
		setRespawn(false);
	}
	
	@Override
	public Hit decrementHealth(Hit hit) {
		if(hit.getDamage() > getCurrentHealth()) {
			hit.setDamage(getCurrentHealth());
			if(hit.getType() == Hit.HitType.CRITICAL) {
				hit.setType(Hit.HitType.NORMAL);
			}
		}
		setCurrentHealth(getCurrentHealth() - hit.getDamage());
		if(getCurrentHealth() <= 0) {
			setCurrentHealth(0);
		}
		if(game != null) {
			for(Player p : game.getPlayers()) {
				p.text(21115, "" + getCurrentHealth() / 10);
			}
		}
		return hit;
	}
	
	@Override
	public void appendDeath() {
		setDead(true);
		World.get().submit(new NpcDeath(this));
		//void knight is dead, lost.
		if(game != null)
			game.end(false);
	}
	
	public void setGame(PestControlMinigame game) {
		this.game = game;
		for(Player p : game.getPlayers()) {
			p.text(21115, "" + getCurrentHealth() / 10);
		}
	}
	
}
