package net.edge.content.commands.impl;

import net.edge.GameServer;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"event"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::event")
public final class EventsCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		GameServer.loadEvents();
	}

}
