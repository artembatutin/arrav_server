package net.edge.content.commands.impl;

import net.edge.world.World;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 3-6-2017.
 */
@CommandSignature(alias = {"answer", "trivia", "answertrivia"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR, Rights.SUPER_MODERATOR, Rights.MODERATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.RESPECTED_MEMBER, Rights.DESIGNER, Rights.PLAYER}, syntax = "Use this command as ::trivia or ::answer answer here")
public final class AnswerTriviaCommand implements Command {

    /**
     * The functionality to be executed as soon as this command is called.
     *
     * @param player  the player we are executing this command for.
     * @param cmd     the command that we are executing for this player.
     * @param command
     */
    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        String answer = String.join(" ", cmd);

        answer = answer.substring(answer.indexOf(" ") + 1, answer.length());

        System.out.println(answer);
        World.get().getTriviaBot().entry.answer(player, answer);
    }
}