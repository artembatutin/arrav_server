package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.content.minigame.nexchamber.NexMinigame;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.locale.Position;

@CommandSignature(alias = {"nex"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR}, syntax = "Use this command as ::nex")
public final class NexCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		NexMinigame.game.onEnter(player);
	}

}
