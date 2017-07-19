package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 9-7-2017.
 */
@CommandSignature(alias = {"gamble", "gamblezone", "dice", "dicezone"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.GOLDEN_DONATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.IRON_MAN, Rights.DESIGNER, Rights.YOUTUBER, Rights.HELPER, Rights.PLAYER}, syntax = "Use this command as ::dice, ::dicezone, ::gamble or ::gamblezone")
public final class GamblingZoneCommand implements Command {
    /**
     * The functionality to be executed as soon as this command is called.
     *
     * @param player  the player we are executing this command for.
     * @param cmd     the command that we are executing for this player.
     * @param command
     */
    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        player.teleport(new Position(2442, 3089));
    }
}
