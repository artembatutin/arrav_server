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

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.LADDER;

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
	 * The pests in the minigame.
	 */
	private ObjectList<Pest> pests = new ObjectArrayList<>();
	
	/**
	 * 10 is 1 minute. so 20 minutes. 10 calls per 1 minute meaning each 6 seconds.
	 */
	private int time = 200;
	
	/**
	 * Amount of respawns there are.
	 */
	private int respawns = 200;
	
	/**
	 * Amount of pests killed.
	 */
	private int killed = 0;
	
	PestControlMinigame(String minigame, MinigameSafety safety) {
		super(minigame, safety);
		voidKnight = new DefaultNpc(RandomUtils.random(3782, 3784, 3785), new Position(2656, 2592));
		portals = new PestPortal[] {
				new PestPortal(6142, new Position(2628, 2591), new Position(2631, 2591)),
				new PestPortal(6145, new Position(2645, 2569), new Position(2645, 2572)),
				new PestPortal(6144, new Position(2669, 2570), new Position(2669, 2573)),
				new PestPortal(6143, new Position(2680, 2588), new Position(2679, 2589))
		};
	}

	@Override
	public void onSequence() {
		System.out.println("pest: " + time);
		if(time == 200)
			start();
		time(--time);
		portals();
		for(PestPortal portal : portals) {
			if(RandomUtils.inclusive(3) == 1)
				portal.spawn(pests);
		}
	}

	@Override
	public void enter(Player player) {
	
	}

	@Override
	public int delay() {
		return 10;//6 seconds.
	}

	@Override
	public void login(Player player) {
		logout(player);
	}

	@Override
	public void logout(Player player) {
		player.move(new Position(2657, 2639));
		getPlayers().remove(player);
		player.setMinigame(Optional.empty());
		player.setInstance(0);
		player.getMessages().sendString("" + respawns, 21115);
		player.getMessages().sendWalkable(-1);
	}

	@Override
	public boolean contains(Player player) {
		return getPlayers().contains(player);
	}
	
	@Override
	public void onTeleportBefore(Player player, Position position) {
		//nothing.
	}
	
	@Override
	public boolean canTeleport(Player player, Position position) {
		//ladders.
		return position.same(new Position(2645, 2601)) || position.same(new Position(2643, 2601)) || position.same(new Position(2668, 2601)) || position
				.same(new Position(2670, 2601)) || position.same(new Position(2647, 2585)) || position.same(new Position(2647, 2587)) || position
				.same(new Position(2666, 2585)) || position.same(new Position(2666, 2587));
	}
	
	
	public void onDeath(Player player) {
		spawn(player);
		player.getDialogueBuilder().append(new NpcDialogue(voidKnight.getId(), Expression.ANGRY, "Try harder!"));
		respawns(--respawns);
		
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
		} else if(other.isNpc()) {
			killed(--killed);
		}
		
		if(!portalsAlive()) {
			//all portals down.
			end(true);
		}
	}
	
	private void end(boolean won) {
		for(Player p : getPlayers()) {
			//dialogue lost.
			logout(p);
			if(won) {
				p.getDialogueBuilder().append(new NpcDialogue(voidKnight.getId(), "Congratulations " + p.getFormatUsername() +"!",  "Your won the pest control match", "You been awarded, well done."));
				//reward
			} else if(voidKnight.getCurrentHealth() > 0) {
				p.getDialogueBuilder().append(new NpcDialogue(voidKnight.getId(), p.getFormatUsername() +" you have Failed.", "You did participate enough to take down", "the portals. ", "Try Harder next time."));
			} else {
				p.getDialogueBuilder().append(new NpcDialogue(voidKnight.getId(), "All is Lost!", "You could not take down the portals in time.", "", "Try Harder next time."));
			}
		}
		World.get().getNpcs().remove(voidKnight);
		for(PestPortal portal : portals) {
			World.get().getNpcs().remove(portal);
		}
		for(Pest pest : pests) {
			World.get().getNpcs().remove(pest);
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
		World.get().getNpcs().add(voidKnight);
		for(Player p : getPlayers()) {
			spawn(p);
		}
		for(PestPortal portal : portals) {
			World.get().getNpcs().add(portal);
			portal.spawn(pests);
		}
	}
	
	private void spawn(Player p) {
		p.move(new Position(2656 + RandomUtils.inclusive(3), 2609 + RandomUtils.inclusive(4)));
		p.getMessages().sendWalkable(21100);
	}
	
	private void message(String message) {
		for(Player p : getPlayers()) {
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
			player.teleport(new Position(player.getPosition().getX() <= 2643 ? 2645 : 2643, 2601), LADDER);
			return false;
		}
		//west north ladder.
		if(object.getId() == 14296 && pos.getX() == 2669) {
			player.teleport(new Position(player.getPosition().getX() >= 2670 ? 2668 : 2670, 2601), LADDER);
			return false;
		}
		//east south ladder
		if(object.getId() == 14296 && pos.getX() == 2647) {
			player.teleport(new Position(2647, player.getPosition().getY() <= 2585 ? 2587 : 2585), LADDER);
			return false;
		}
		//west south ladder
		if(object.getId() == 14296 && pos.getX() == 2666) {
			player.teleport(new Position(2666, player.getPosition().getY() <= 2585 ? 2587 : 2585), LADDER);
			return false;
		}
		return true;
	}
	
	private void respawns(int respawns) {
		this.respawns = respawns;
		for(Player p : getPlayers()) {
			p.getMessages().sendString("" + respawns, 21115);
		}
		if(respawns == 0) {
			//no respawns left.
			end(false);
		}
	}
	
	private void time(int time) {
		this.time = time;
		for(Player p : getPlayers()) {
			p.getMessages().sendString("Time Remaining: " + (time * 6) + " seconds", 21117);
		}
		if(time == 0) {
			//kept void alive, won.
			end(true);
		}
		if(time % 8 == 7) {
			voidKnight.forceChat(RandomUtils.random(YELLS));
		}
	}
	
	private void killed(int killed) {
		this.killed = killed;
		for(Player p : getPlayers()) {
			p.getMessages().sendString("" + killed, 21116);
		}
	}
	
	private void portals() {
		for(Player p : getPlayers()) {
			p.getMessages().sendString("" + portals[0].getCurrentHealth(), 21111);
			p.getMessages().sendString("" + portals[3].getCurrentHealth(), 21112);
			p.getMessages().sendString("" + portals[2].getCurrentHealth(), 21113);
			p.getMessages().sendString("" + portals[1].getCurrentHealth(), 21114);
		}
	}

}
