package net.edge.content.minigame.pestcontrol;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.minigame.pestcontrol.pest.*;

import net.edge.world.entity.region.TraversalMap;
import net.edge.world.locale.Position;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Hit;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.MobDeath;
import net.edge.world.entity.actor.mob.impl.DefaultMob;
import net.edge.world.entity.actor.player.Player;

import java.util.Optional;

import static net.edge.content.minigame.pestcontrol.pest.PestType.*;

public class PestPortal extends DefaultMob {
	
	
	private final Mob knight;
	
	private final Position spawn;
	
	private final int widget;
	
	private PestControlMinigame game;
	
	public PestPortal(int id, Position position, Position spawn, int widget, Mob knight) {
		super(id, position);
		setOriginalRandomWalk(false);
		setAutoRetaliate(false);
		setCurrentHealth(2500);
		setRespawn(false);
		this.spawn = spawn;
		this.widget = widget;
		this.knight = knight;
	}
	
	public Position getSpawn() {
		return spawn;
	}
	
	public void spawn(ObjectList<Pest> pests) {
		if(getCurrentHealth() == 0)
			return;
		int type = RandomUtils.inclusive(0, 6);
		Pest pest = null;
		Optional<Position> destination = TraversalMap.getRandomTraversableTile(getSpawn(), type == 0 ? 2 : 1);
		if(!destination.isPresent())
			return;
		switch(type) {
			case 0:
				pest = new Brawler(BRAWLER.random(), destination.get());
				break;
			case 1:
				pest = new Defiler(DEFILER.random(), destination.get());
				break;
			case 2:
				pest = new Ravager(RAVAGER.random(), destination.get());
				break;
			case 3:
				pest = new Shifter(SHIFTER.random(), destination.get());
				break;
			case 4:
				pest = new Spinner(SPINNER.random(), this, destination.get());
				break;
			case 5:
				pest = new Splatter(SPLATTER.random(), destination.get());
				break;
			case 6:
				pest = new Torcher(TORCHER.random(), destination.get());
				break;
		}
		if(pest != null) {
			World.get().getMobs().add(pest);
			pests.add(pest);
		}
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
				p.text(widget, "" + getCurrentHealth() / 10);
			}
		}
		return hit;
	}
	
	@Override
	public void appendDeath() {
		setDead(true);
		World.get().submit(new MobDeath(this));
		knight.healEntity(50);
		if(game != null) {
			if(!game.portalsAlive()) {
				//all portals down.
				game.end(true);
			}
		}
	}
	
	public void setGame(PestControlMinigame game) {
		this.game = game;
		for(Player p : game.getPlayers()) {
			p.text(widget, "" + getCurrentHealth() / 10);
		}
	}
	
}
