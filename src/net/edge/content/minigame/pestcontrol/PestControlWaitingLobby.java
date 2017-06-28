package net.edge.content.minigame.pestcontrol;

import net.edge.content.minigame.MinigameLobby;
import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Position;
import net.edge.task.Task;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

import static net.edge.content.minigame.Minigame.MinigameSafety.SAFE;

public final class PestControlWaitingLobby extends MinigameLobby {
	
	/**
	 * The pest control lobby.
	 */
	public static final PestControlWaitingLobby PEST_LOBBY = new PestControlWaitingLobby();
	
	private int count;
	private int timer;
	
	public PestControlWaitingLobby() {
		super(10); // 5 minutes
	}
	
	@Override
	public void onCountdown(int current, Task t) {
		timer = current;
		System.out.println(current);
		if(timer % 10 == 0) {
			for(Player p : getPlayers()) {
				p.getMessages().sendString("@whi@Next Departure: " + seconds() + " seconds.", 21120);
			}
		}
	}
	
	@Override
	public void onEnter(Player player) {
		count++;
		getPlayers().add(player);
		player.getMessages().sendWalkable(21119);
		player.getMessages().sendString("@whi@Next Departure: " + seconds() + " seconds", 21120);
		player.getMessages().sendString("@cya@Pest Points: " + player.getPest(), 21123);
		updateCounts();
		player.move(new Position(2661, 2639));
	}
	
	@Override
	public void onLeave(Player player) {
		if(!getPlayers().contains(player))
			return;
		count--;
		getPlayers().remove(player);
		player.getMessages().sendWalkable(-1);
		updateCounts();
		player.move(new Position(2657, 2639));
	}
	
	@Override
	public void onStart() {
		PestControlMinigame game = new PestControlMinigame("Pest control", SAFE);
		for(Player p : getPlayers()) {
			System.out.println("moving to pest: " + p.getFormatUsername());
			game.onEnter(p);
		}
		getPlayers().clear();
		count = 0;
	}
	
	@Override
	public boolean canEnter(Player player) {
		if(player.getFamiliar().isPresent()) {
			player.message("You can't enter with a familiar aboard.");
			return false;
		}
		if(player.getPetManager().getPet().isPresent()) {
			player.message("You can't enter with a pet aboard.");
			return false;
		}
		if(!player.getInventory().isEmpty()) {
			player.message("Your inventory has to be empty.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canStart() {
		//if enough players are in the room.
		return true;
	}
	
	@Override
	public int restartTimer() {
		return 10;
	}
	
	@Override
	public void onDestruct() {
		
	}
	
	public int seconds() {
		return (timer * 600) / 1000;
	}
	
	public void updateCounts() {
		for(Player p : getPlayers()) {
			p.getMessages().sendString("@gre@Players Ready: " + count, 21121);
		}
	}
	
	public static void event() {
		ObjectEvent plank = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				PEST_LOBBY.submit(player);
				return true;
			}
		};
		plank.registerFirst(14315);
		
		ObjectEvent ladder = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				PEST_LOBBY.onLeave(player);
				return true;
			}
		};
		ladder.registerFirst(14314);
	}
	
}
