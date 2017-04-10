package net.edge.world.content.minigame.pestcontrol;

import net.edge.world.content.minigame.MinigameLobby;
import net.edge.world.model.node.entity.player.Player;

import java.util.List;

public final class PestControlWaitingLobby extends MinigameLobby {

	public PestControlWaitingLobby() {
		super(540); // 5 minutes
	}

	@Override
	public void onEnter(Player player) {
		//send interface with timer etc.
	}

	@Override
	public void onStart(List<Player> player) {
		//start a {@link PestControlMinigame}
	}

	@Override
	public boolean canEnter(Player player) {
		//if player has no familiar, no dwarf multi cannon etc.
		return false;
	}

	@Override
	public boolean canStart(List<Player> players) {
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
