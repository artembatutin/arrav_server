package net.edge.content.minigame.pestcontrol;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.minigame.MinigameLobby;
import net.edge.world.node.entity.player.Player;

public final class PestControlWaitingLobby extends MinigameLobby {
	
	public PestControlWaitingLobby() {
		super(540); // 5 minutes
	}
	
	@Override
	public void onEnter(Player player) {
		//send interface with timer etc.
	}
	
	@Override
	public void onStart(ObjectList<Player> player) {
		//start a {@link PestControlMinigame}
	}
	
	@Override
	public boolean canEnter(Player player) {
		//if player has no familiar, no dwarf multi cannon etc.
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
