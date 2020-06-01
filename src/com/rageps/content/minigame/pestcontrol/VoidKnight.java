package com.rageps.content.minigame.pestcontrol;

import com.rageps.world.World;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.mob.DefaultMob;
import com.rageps.world.entity.actor.mob.MobDeath;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;

public class VoidKnight extends DefaultMob {
	
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
			if(hit.getHitsplat() == Hitsplat.CRITICAL) {
				hit.set(Hitsplat.NORMAL);
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
		World.get().submit(new MobDeath(this));
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
