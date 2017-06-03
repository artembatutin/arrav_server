package net.edge.content.trivia;

import net.edge.task.Task;
import net.edge.world.node.entity.player.Player;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 3-6-2017.
 */
public final class TriviaTask extends Task {

    /**
     * The entry of this trivia task.
     */
    public final TriviaEntry entry = new TriviaEntry();

    /**
     * Creates a new {@link TriviaTask}.
     */
    public TriviaTask() {
        super(10, false);
    }

    /**
     * Determines if the world has been notified by the trivia question.
     */
    private boolean notified;

    /**
     * Determines if the world has been reminded of the trivia question.
     */
    private boolean reminded;

    /**
     * A function executed when the {@code counter} reaches the {@code delay}.
     */
    @Override
    protected void execute() {
        if(!notified) {
            entry.ask();
            notified = true;
        } else if(!reminded) {
            entry.reminder();
            reminded = true;
        } else {
            entry.reset();
            notified = false;
            reminded = false;
        }
    }

    /**
     * The trivia question that should be send to the player on login.
     * @param player    the player to send the question to.
     */
    public void onLogin(Player player) {
        entry.onLogin(player);
    }
}
