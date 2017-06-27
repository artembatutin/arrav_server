package net.edge.content.minigame.pestcontrol;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.dialogue.Expression;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.minigame.SequencedMinigame;
import net.edge.content.minigame.pestcontrol.pest.Pest;
import net.edge.locale.Position;
import net.edge.util.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.impl.DefaultNpc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

import java.util.Optional;

public final class PestControlMinigame extends SequencedMinigame {
	
	/**
	 * The strings that the knight yells out.
	 */
	private static final String[] YELLS = {
			"We must not fail!",
			"Take down the portals",
			"The Void Knights will not fall!",
			"Hail the Void Knights!",
			"We are beating these scums!"
	};
	
	/**
	 * The middle void knight.
	 */
	private final Npc voidKnight;
	
	/**
	 * All of the portals.
	 * 0 purple
	 * 1 red
	 * 2 yellow
	 * 3 gray
	 */
	private final PestPortal[] portals;
	
	/**
	 * Playing in the minigame.
	 */
	private ObjectList<Player> players = new ObjectArrayList<>();
	
	/**
	 * The pests in the minigame.
	 */
	private ObjectList<Pest> pests = new ObjectArrayList<>();
	
	/**
	 * The instance the game is in.
	 */
	private final int instance;
	
	/**
	 * 10 is 1 minute. so 20 minutes. 10 calls per 1 minute meaning each 6 seconds.
	 */
	private int time = 200;
	
	PestControlMinigame(String minigame, MinigameSafety safety) {
		super(minigame, safety);
		instance = World.getInstanceManager().closeNext();
		voidKnight = new DefaultNpc(RandomUtils.random(3782, 3784, 3785), new Position(2656, 2592));
		portals = new PestPortal[] {
				new PestPortal(6142, new Position(2628, 2951), new Position(2631, 2591), instance),
				new PestPortal(6145, new Position(2645, 2569), new Position(2645, 2572), instance),
				new PestPortal(6144, new Position(2669, 2570), new Position(2669, 2573), instance),
				new PestPortal(6143, new Position(2680, 2588), new Position(2679, 2589), instance)
		};
	}

	@Override
	public void onSequence(Player player) {
		if(time == 200)
			start();
		time--;
		if(time == 0) {
			//kept void alive, won.
			end(true);
		}
		if(time % 8 == 7) {
			voidKnight.forceChat(RandomUtils.random(YELLS));
		}
	}

	@Override
	public void enter(Player player) {
		player.setMinigame(Optional.of(this));
		players.add(player);
		player.setInstance(instance);
	}

	@Override
	public int delay() {
		return 10;//6 seconds.
	}

	@Override
	public void login(Player player) {
		players.remove(player);
	}

	@Override
	public void logout(Player player) {
		player.move(new Position(2659, 2649));
		players.remove(player);
		player.setMinigame(Optional.empty());
		player.setInstance(0);
	}

	@Override
	public boolean contains(Player player) {
		return players.contains(player);
	}
	
	
	public void onDeath(Player player) {
		spawn(player);
		player.getDialogueBuilder().append(new NpcDialogue(voidKnight.getId(), Expression.ANGRY, "Try harder!"));
		
	}
	
	public void onKill(Player player, EntityNode other) {
		if(other.same(voidKnight)) {
			//lost because knight is dead.
			end(false);
		} else if(other.same(portals[0])) {
			voidKnight.setCurrentHealth(voidKnight.getCurrentHealth() + 50);
			message("The purple portal has been destroyed.");
		} else if(other.same(portals[1])) {
			voidKnight.setCurrentHealth(voidKnight.getCurrentHealth() + 50);
			message("The red portal has been destroyed.");
		} else if(other.same(portals[2])) {
			voidKnight.setCurrentHealth(voidKnight.getCurrentHealth() + 50);
			message("The yellow portal has been destroyed.");
		} else if(other.same(portals[3])) {
			voidKnight.setCurrentHealth(voidKnight.getCurrentHealth() + 50);
			message("The grey portal has been destroyed.");
		}
		
		if(!portalsAlive()) {
			//all portals down.
			end(true);
		}
	}
	
	private void end(boolean won) {
		World.getInstanceManager().open(instance);
		for(Player p : players) {
			//dialogue lost.
			logout(p);
			if(won) {
				p.getDialogueBuilder().append(new NpcDialogue(voidKnight.getId(), "Congratulations " + p.getFormatUsername() +"!", "You took down all the portals whilst keeping", "the void knight alive.", "You been awarded, well done."));
				//reward
			} else if(voidKnight.getCurrentHealth() > 0) {
				p.getDialogueBuilder().append(new NpcDialogue(voidKnight.getId(), p.getFormatUsername() +" you have Failed.", "You did participate enough to take down", "the portals. ", "Try Harder next time."));
			} else {
				p.getDialogueBuilder().append(new NpcDialogue(voidKnight.getId(), "All is Lost!", "You could not take down the portals in time.", "", "Try Harder next time."));
			}
		}
		destruct();
	}
	
	private boolean portalsAlive() {
		for(PestPortal portal : portals) {
			if(portal.getCurrentHealth() > 0)
				return true;
		}
		return false;
	}
	
	private void start() {
		for(Player p : players) {
			spawn(p);
		}
	}
	
	private void spawn(Player p) {
		p.setPosition(new Position(2656 + RandomUtils.inclusive(3), 2609 + RandomUtils.inclusive(4)));
	}
	
	private void message(String message) {
		for(Player p : players) {
			p.message(message);
		}
	}
	
	/*
	 * Ladders, barricades and gates.
	 */
	public boolean onFirstClickObject(Player player, ObjectNode object) {
		Position pos = object.getGlobalPos();
		//east north ladder.
		if(object.getId() == 14296 && pos.getX() == 2644) {
			player.move(new Position(player.getPosition().getX() <= 2643 ? 2645 : 2643, 2601));
			return true;
		}
		//west north ladder.
		if(object.getId() == 14296 && pos.getX() == 2669) {
			player.move(new Position(player.getPosition().getX() >= 2670 ? 2668 : 2670, 2601));
			return true;
		}
		//east south ladder
		if(object.getId() == 14296 && pos.getX() == 2647) {
			player.move(new Position(2647, player.getPosition().getY() <= 2585 ? 2587 : 2585));
			return true;
		}
		//west south ladder
		if(object.getId() == 14296 && pos.getX() == 2666) {
			player.move(new Position(2666, player.getPosition().getY() <= 2585 ? 2587 : 2585));
			return true;
		}
		player.message("What are you doing? Go fight soldier!");
		return false;
	}

}
