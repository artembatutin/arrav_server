package net.edge.content.minigame.pestcontrol;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.minigame.pestcontrol.pest.*;

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
	
	public void spawn(ObjectList<Pest> pests) {
		if(getCurrentHealth() == 0)
			return;
		int type = RandomUtils.inclusive(0, 6);
		Pest pest = null;
		switch(type) {
			case 0:
				pest = new Brawler(BRAWLER.random(), getSpawn(), getInstance());
				break;
			case 1:
				pest = new Defiler(DEFILER.random(), getSpawn(), getInstance());
				break;
			case 2:
				pest = new Ravager(RAVAGER.random(), getSpawn(), getInstance());
				break;
			case 3:
				pest = new Shifter(SHIFTER.random(), getSpawn(), getInstance());
				break;
			case 4:
				pest = new Spinner(SPINNER.random(), this);
				break;
			case 5:
				pest = new Splatter(SPLATTER.random(), getSpawn(), getInstance());
				break;
		}
		if(pest != null) {
			World.get().getNpcs().add(pest);
			pests.add(pest);
		}
	}
	
}
