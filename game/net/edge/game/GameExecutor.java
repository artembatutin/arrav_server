package net.edge.game;

import net.edge.util.ThreadUtil;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.PlayerSerialization;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executes tasks in parallel for faster response.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class GameExecutor {
	
	/**
	 * The {@link ExecutorService} to which workers are submitted.
	 */
	private final ExecutorService executor = Executors.newCachedThreadPool(ThreadUtil.create("GameExecutor"));
	
	/**
	 * Serialize a player.
	 * @param player player to be saved.
	 */
	public void savePlayer(Player player) {
		if(player != null)
			executor.submit(() -> new PlayerSerialization(player).serialize());
	}
	
	/**
	 * Deserialize a player.
	 * @param player player to be loaded.
	 * @param password password entered.
	 * @param login flag if on login check.
	 */
	public void loadPlayer(Player player, String password, boolean login) {
		executor.submit(() -> new PlayerSerialization(player).deserialize(password, login));
	}
	
	public void submit(Runnable r) {
		executor.submit(r);
	}
	
	public ExecutorService get() {
		return executor;
	}
	
}
