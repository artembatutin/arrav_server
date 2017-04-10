package net.edge.world.content.commands.impl;

import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.Rights;
import tools.map.MapTool;

@CommandSignature(alias = {"map"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::map")
public final class MapViewer implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		MapTool.start();
	}

}
