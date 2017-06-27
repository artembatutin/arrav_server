package net.edge.content.minigame.pestcontrol;

import net.edge.content.minigame.pestcontrol.pest.Brawler;
import net.edge.content.minigame.pestcontrol.pest.Defiler;
import net.edge.content.minigame.pestcontrol.pest.Pest;

import net.edge.locale.Position;
import net.edge.util.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.node.entity.npc.impl.DefaultNpc;

import static net.edge.content.minigame.pestcontrol.pest.PestType.*;

public class PestPortal extends DefaultNpc {
	
	private final Position spawn;
	
	public PestPortal(int id, Position position, Position spawn, int instance) {
		super(id, position);
		setOriginalRandomWalk(false);
		setAutoRetaliate(false);
		setCurrentHealth(250);
		setRespawn(false);
		setInstance(instance);
		this.spawn = spawn;
	}
	
	public Position getSpawn() {
		return spawn;
	}
	
	public void spawn() {
		int type = RandomUtils.inclusive(0, 6);
		Pest pest = null;
		switch(type) {
			case 0:
				pest = new Brawler(BRAWLER.random(), getSpawn(), getInstance());
				break;
			case 1:
				pest = new Defiler(DEFILER.random(), getSpawn(), getInstance());
				break;
		}
		if(pest != null)
			World.get().getNpcs().add(pest);
	}
	
}
