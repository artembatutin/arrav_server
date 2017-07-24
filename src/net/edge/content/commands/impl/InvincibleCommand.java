package net.edge.content.commands.impl;

import net.edge.content.trivia.TriviaTask;
import net.edge.world.World;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

/**
 * @author Cad
 * @since 7-21-2017.
 */
@CommandSignature(alias = {"invincible"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::invincible")
public final class InvincibleCommand implements Command {

    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        player.getAttr().get("invincible").set(true);
    }
}
