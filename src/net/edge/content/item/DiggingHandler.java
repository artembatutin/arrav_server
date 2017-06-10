package net.edge.content.item;

import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;

import java.util.function.Consumer;

/**
 * Holds functionality which will execute after the player attempts to dig.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class DiggingHandler extends Task {
	
	/**
	 * The player whom is digging.
	 */
	private final Player player;
	
	/**
	 * The destination after digging.
	 */
	private final Consumer<Player> action;
	
	/**
	 * Constructs a new {@link DiggingHandler}.
	 * @param player {@link #player}.
	 * @param action {@link #action}.
	 */
	public DiggingHandler(Player player, Consumer<Player> action) {
		super(1, false);
		this.player = player;
		this.action = action;
	}
	
	@Override
	public void onSubmit() {
		player.animation(new Animation(831));
	}
	
	@Override
	public void execute() {
		cancel();
		player.animation(null);
	}
	
	@Override
	public void onCancel() {
		action.accept(player);
	}
	
}
