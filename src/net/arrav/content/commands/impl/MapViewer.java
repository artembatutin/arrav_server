package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import tool.mapviewer.MapTool;

@CommandSignature(alias = {"mapviewer"}, rights = {Rights.ADMINISTRATOR}, syntax = "Opens a map viewer teleportation, mapviewer")
public final class MapViewer implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		MapTool.start();
	}
	
}
