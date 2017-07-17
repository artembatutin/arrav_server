package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.Rights;
import tool.mapviewer.MapTool;

@CommandSignature(alias = {"mapviewer"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::tool.mapviewer")
public final class MapViewer implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		MapTool.start();
	}

}
