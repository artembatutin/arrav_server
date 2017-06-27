package net.edge.content.minigame.pestcontrol;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.minigame.MinigameLobby;
import net.edge.task.Task;
import net.edge.world.node.entity.player.Player;

import static net.edge.content.minigame.Minigame.MinigameSafety.SAFE;

public final class PestControlWaitingLobby extends MinigameLobby {
	
	public PestControlWaitingLobby() {
		super(540); // 5 minutes
	}
	
	/**
	 * Any functionality that should occur while the task is running.
	 * @param player the player in the lobby.
	 * @param t      the task running for this lobby.
	 */
	public void onCountdown(ObjectList<Player> player, Task t) {
	
	}
	
	@Override
	public void onEnter(Player player) {
		//send interface with timer etc.
	}
	
	@Override
	public void onStart(ObjectList<Player> player) {
		PestControlMinigame game = new PestControlMinigame("Pest control", SAFE);
		for(Player p : player) {
			game.enter(p);
		}
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
		return false;
	}
	
	@Override
	public boolean canStart(ObjectList<Player> players) {
		//if enough players are in the room.
		return false;
	}
	
	@Override
	public int restartTimer() {
		return 540;
	}
	
	@Override
	public void onDestruct() {
		
	}
	
}
