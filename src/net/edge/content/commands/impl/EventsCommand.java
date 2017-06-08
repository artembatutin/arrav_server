package net.edge.content.commands.impl;

import net.edge.Server;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.net.database.connection.use.Donating;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"event"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::event")
public final class EventsCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Server.loadEvents();
	}

}
