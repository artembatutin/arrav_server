package com.rageps.content.trivia;

import com.rageps.world.World;
import com.rageps.world.text.ColorConstants;
import com.rageps.world.text.MessageBuilder;
import com.rageps.task.Task;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 3-6-2017.
 */
public final class TriviaTask extends Task {
	
	/**
	 * The trivia task bot.
	 */
	private static final TriviaTask TRIVIA_BOT = new TriviaTask();
	
	/**
	 * The entry of this trivia task.
	 */
	public final TriviaEntry entry = new TriviaEntry();
	
	/**
	 * Creates a new {@link TriviaTask}.
	 */
	public TriviaTask() {
		super(2000, false);
	}
	
	/**
	 * Determines if the world has been reminded of the trivia question.
	 */
	private boolean reminded = false;
	
	/**
	 * A function executed when the {@code counter} reaches the {@code delay}.
	 */
	@Override
	protected void execute() {
		if(entry.current == null) {
			entry.current = TriviaData.random();
			entry.ask();
			return;
		}
		if(!reminded) {
			entry.reminder();
			reminded = true;
			return;
		}

		MessageBuilder mb = new MessageBuilder();
		mb.appendShade().appendPrefix("Trivia Bot", ColorConstants.BLACK, ColorConstants.PURPLE).append(": ").
				append("The last trivia question hasn't been answered and has expired!", ColorConstants.MAGENTA).terminateShade();
		entry.reset();
		reminded = false;
	}
	
	/**
	 * The trivia question that should be send to the player on login.
	 * @param player the player to send the question to.
	 */
	public void onLogin(Player player) {
		entry.onLogin(player);
	}
	
	/**
	 * Returns the trivia bot handler.
	 */
	public static TriviaTask getBot() {
		return TRIVIA_BOT;
	}
}
