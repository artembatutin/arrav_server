package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.content.trivia.TriviaTask;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 3-6-2017.
 */
@CommandSignature(alias = {"answer", "trivia", "answertrivia"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.GOLDEN_DONATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.IRON_MAN, Rights.DESIGNER, Rights.YOUTUBER, Rights.HELPER, Rights.PLAYER}, syntax = "Answers to a trivia question by ::trivia answer")
public final class AnswerTriviaCommand implements Command {

	/**
	 * The functionality to be executed as soon as this command is called.
	 *
	 * @param player the player we are executing this command for.
	 * @param cmd    the command that we are executing for this player.
	 */
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		String answer = String.join(" ", cmd);
		answer = answer.substring(answer.indexOf(" ") + 1, answer.length());
		TriviaTask.getBot().entry.answer(player, answer);
	}
}
