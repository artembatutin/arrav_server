package com.rageps.content.item;

import com.rageps.task.Task;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.player.Player;

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
