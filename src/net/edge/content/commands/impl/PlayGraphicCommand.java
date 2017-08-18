package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"gfx", "graphic", "graph"}, rights = {Rights.ADMINISTRATOR}, syntax = "Plays a graphic, ::gfx id")
public final class PlayGraphicCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		player.graphic(new Graphic(id));
	}
	
}
